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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    /**
     * 用户注册 - 密码加密存储，检查用户名重复
     * @param user 用户信息
     * @return 注册后的用户
     */
    public User register(User user) {
        // 检查用户名是否已存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        if (userMapper.selectOne(wrapper) != null) {
            throw new RuntimeException("用户名已存在");
        }
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
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    public User getByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return userMapper.selectOne(wrapper);
    }

    /**
     * 校验用户密码是否正确
     * @param userId 用户ID
     * @param rawPassword 明文密码
     * @return 是否匹配
     */
    public boolean checkPassword(Long userId, String rawPassword) {
        User user = userMapper.selectById(userId);
        return user != null && passwordEncoder.matches(rawPassword, user.getPassword());
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 是否成功
     */
    public boolean update(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
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
