package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.entity.User;
import com.warehouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        if (loginUser != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("user", loginUser);
            data.put("token", "token_" + loginUser.getId());
            return Result.success(data);
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        user.setRole("user");
        userService.register(user);
        return Result.success(user);
    }

    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody User user) {
        userService.update(user);
        return Result.success();
    }

    @PutMapping("/password")
    public Result<?> updatePassword(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String newPassword = params.get("newPassword").toString();
        User user = userService.getById(id);
        user.setPassword(newPassword);
        userService.update(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }
}
