package com.warehouse.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.warehouse.entity.User;
import com.warehouse.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 数据初始化器
 * 应用启动时执行，确保所有用户密码被正确BCrypt加密
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeUsers();
    }

    /**
     * 初始化所有用户账户
     * 将无效的密码哈希（非标准BCrypt格式）重新加密
     */
    private void initializeUsers() {
        // 查询所有用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 仅查询密码字段不以$2a$或$2b$或$2y$开头的用户（非BCrypt格式）
        wrapper.notLike("password", "$2a$")
               .or()
               .notLike("password", "$2b$")
               .or()
               .notLike("password", "$2y$");

        // 更简单的方式：遍历所有用户检查密码
        for (User user : userMapper.selectList(null)) {
            String pwd = user.getPassword();
            // BCrypt哈希必须以$2开头且长度为60
            if (pwd == null || !pwd.startsWith("$2") || pwd.length() != 60) {
                user.setPassword(passwordEncoder.encode("123456"));
                userMapper.updateById(user);
                System.out.println("[DataInitializer] 用户 " + user.getUsername() + " 密码已加密更新");
            }
        }

        // 确保admin账户存在
        QueryWrapper<User> adminWrapper = new QueryWrapper<>();
        adminWrapper.eq("username", "admin");
        if (userMapper.selectOne(adminWrapper) == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setNickname("管理员");
            admin.setRole("admin");
            userMapper.insert(admin);
            System.out.println("[DataInitializer] 默认管理员账户已创建: admin/123456");
        }
    }
}
