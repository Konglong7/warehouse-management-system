package com.warehouse.controller;

import com.warehouse.common.JwtUtils;
import com.warehouse.common.Result;
import com.warehouse.entity.User;
import com.warehouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /** 登录失败次数记录，用于防暴力破解 */
    private final ConcurrentHashMap<String, LoginAttempt> loginAttempts = new ConcurrentHashMap<>();
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final long LOCK_DURATION_MS = 15 * 60 * 1000; // 15分钟锁定

    /** 登录尝试记录 */
    private static class LoginAttempt {
        int count = 0;
        long lockTime = 0;
    }

    /**
     * 用户登录（含暴力破解防护）
     * @param user 登录信息（用户名、密码）
     * @return 登录结果和JWT令牌
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        String username = user.getUsername();

        // 检查账户是否被锁定
        if (isAccountLocked(username)) {
            return Result.error(429, "登录失败次数过多，请15分钟后重试");
        }

        User loginUser = userService.login(username, user.getPassword());
        if (loginUser != null) {
            // 登录成功，清除失败记录
            clearFailedAttempts(username);

            // 生成JWT令牌
            String token = jwtUtils.generateToken(loginUser.getId(), loginUser.getUsername(), loginUser.getRole());

            Map<String, Object> data = new HashMap<>();
            data.put("user", loginUser);
            data.put("token", token);
            return Result.success(data);
        }

        // 登录失败，记录尝试次数
        recordFailedAttempt(username);
        return Result.error(401, "用户名或密码错误");
    }

    /**
     * 用户注册
     * @param user 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return Result.error(400, "用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return Result.error(400, "密码不能为空");
        }
        user.setRole("user");
        try {
            userService.register(user);
            return Result.success("注册成功");
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 获取用户列表（仅管理员）
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('admin')")
    public Result<?> list() {
        return Result.success(userService.list());
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<?> update(@RequestBody User user) {
        userService.update(user);
        return Result.success();
    }

    /**
     * 修改密码（需验证旧密码 + 权限校验）
     */
    @PutMapping("/password")
    public Result<?> updatePassword(@RequestBody Map<String, Object> params) {
        // 获取当前登录用户
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.getByUsername(currentUsername);
        if (currentUser == null) {
            return Result.error(401, "请先登录");
        }

        // 安全解析用户ID
        if (params.get("id") == null) {
            return Result.error(400, "用户ID不能为空");
        }
        Long id;
        try {
            id = Long.valueOf(params.get("id").toString());
        } catch (NumberFormatException e) {
            return Result.error(400, "用户ID格式错误");
        }

        // 权限校验：只能修改自己的密码，管理员可修改所有人
        if (!currentUser.getRole().equals("admin") && !currentUser.getId().equals(id)) {
            return Result.error(403, "无权修改他人密码");
        }

        // 验证旧密码（管理员修改他人密码时也需要验证自己的密码）
        String oldPassword = params.get("oldPassword") != null ? params.get("oldPassword").toString() : null;
        if (oldPassword == null || oldPassword.isEmpty()) {
            return Result.error(400, "旧密码不能为空");
        }
        if (!userService.checkPassword(currentUser.getId(), oldPassword)) {
            return Result.error(400, "旧密码错误");
        }

        // 验证新密码
        String newPassword = params.get("newPassword") != null ? params.get("newPassword").toString() : null;
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return Result.error(400, "新密码不能为空");
        }
        if (newPassword.length() < 6) {
            return Result.error(400, "密码长度不能少于6位");
        }

        User targetUser = userService.getById(id);
        if (targetUser == null) {
            return Result.error(404, "用户不存在");
        }

        targetUser.setPassword(newPassword);
        userService.update(targetUser);
        return Result.success();
    }

    /**
     * 删除用户（仅管理员）
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public Result<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }

    /**
     * 检查账户是否因多次登录失败被锁定
     */
    private boolean isAccountLocked(String username) {
        LoginAttempt attempt = loginAttempts.get(username);
        if (attempt == null) return false;
        if (attempt.count >= MAX_LOGIN_ATTEMPTS) {
            if (System.currentTimeMillis() - attempt.lockTime < LOCK_DURATION_MS) {
                return true;
            }
            loginAttempts.remove(username);
        }
        return false;
    }

    /**
     * 记录登录失败次数
     */
    private void recordFailedAttempt(String username) {
        loginAttempts.compute(username, (k, v) -> {
            if (v == null) v = new LoginAttempt();
            v.count++;
            if (v.count >= MAX_LOGIN_ATTEMPTS) {
                v.lockTime = System.currentTimeMillis();
            }
            return v;
        });
    }

    /**
     * 清除登录失败记录
     */
    private void clearFailedAttempts(String username) {
        loginAttempts.remove(username);
    }
}
