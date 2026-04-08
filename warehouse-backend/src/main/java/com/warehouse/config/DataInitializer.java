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
 * 应用启动时执行，确保现有数据的密码被正确加密
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {
        // 确保管理员账户存在且密码已加密
        initializeAdminUser();
    }

    /**
     * 初始化管理员账户
     * 将明文密码转换为BCrypt加密格式
     */
    private void initializeAdminUser() {
        // 查询admin用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", "admin");
        User admin = userMapper.selectOne(wrapper);

        if (admin != null) {
            // 检查密码是否已加密（BCrypt格式）
            if (!admin.getPassword().startsWith("$2")) {
                // 密码未加密，执行加密
                admin.setPassword(passwordEncoder.encode("123456"));
                userMapper.updateById(admin);
                System.out.println("[DataInitializer] 管理员密码已加密更新");
            }
        } else {
            // 创建默认管理员
            admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setNickname("管理员");
            admin.setRole("admin");
            userMapper.insert(admin);
            System.out.println("[DataInitializer] 默认管理员账户已创建: admin/123456");
        }
    }
}
