package com.warehouse.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.warehouse.entity.User;
import com.warehouse.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).eq("password", password);
        return userMapper.selectOne(wrapper);
    }

    public User register(User user) {
        userMapper.insert(user);
        return user;
    }

    public List<User> list() {
        return userMapper.selectList(null);
    }

    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    public boolean update(User user) {
        return userMapper.updateById(user) > 0;
    }

    public boolean delete(Long id) {
        return userMapper.deleteById(id) > 0;
    }
}
