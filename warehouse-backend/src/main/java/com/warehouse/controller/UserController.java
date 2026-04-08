package com.warehouse.controller;

import com.warehouse.common.JwtUtils;
import com.warehouse.common.Result;
import com.warehouse.entity.User;
import com.warehouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 用户登录
     * @param user 登录信息（用户名、密码）
     * @return 登录结果和JWT令牌
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        if (loginUser != null) {
            // 生成JWT令牌
            String token = jwtUtils.generateToken(loginUser.getId(), loginUser.getUsername(), loginUser.getRole());

            Map<String, Object> data = new HashMap<>();
            data.put("user", loginUser);
            data.put("token", token);
            return Result.success(data);
        }
        return Result.error("用户名或密码错误");
    }

    /**
     * 用户注册
     * @param user 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        // 验证必填字段
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return Result.error(400, "用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return Result.error(400, "密码不能为空");
        }
        // 默认角色为普通用户
        user.setRole("user");
        userService.register(user);
        return Result.success("注册成功");
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
     * 修改密码
     */
    @PutMapping("/password")
    public Result<?> updatePassword(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String newPassword = params.get("newPassword").toString();

        // 验证新密码
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return Result.error(400, "新密码不能为空");
        }
        if (newPassword.length() < 6) {
            return Result.error(400, "密码长度不能少于6位");
        }

        User user = userService.getById(id);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        user.setPassword(newPassword);
        userService.update(user);
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
}
