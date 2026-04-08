package com.warehouse.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.warehouse.entity.User;
import com.warehouse.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    // BCrypt密码加密器
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 用户登录 - 使用BCrypt密码校验
     * @param username 用户名
     * @param password 密码（明文）
     * @return 登录成功的用户对象，失败返回null
     */
    public User login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = userMapper.selectOne(wrapper);
        // 使用BCrypt校验密码
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    /**
     * 用户注册 - 密码加密存储
     * @param user 用户信息
     * @return 注册后的用户
     */
    public User register(User user) {
        // 密码加密存储
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        return user;
    }

    public List<User> list() {
        return userMapper.selectList(null);
    }

    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 是否成功
     */
    public boolean update(User user) {
        // 如果包含密码字段，需要加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            // 检查是否已经是加密密码（BCrypt加密后以$2a$开头）
            if (!user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }
        return userMapper.updateById(user) > 0;
    }

    public boolean delete(Long id) {
        return userMapper.deleteById(id) > 0;
    }
}
