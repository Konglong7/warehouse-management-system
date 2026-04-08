package com.warehouse.config;

import com.warehouse.common.JwtUtils;
import com.warehouse.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 * 实现JWT认证和权限控制
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 配置安全过滤器链
     * @param http HttpSecurity对象
     * @return SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF（使用JWT不需要）
            .csrf().disable()
            // 禁用Session（使用JWT无状态认证）
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // 配置请求授权
            .authorizeRequests()
            // 登录、注册接口允许匿名访问
            .antMatchers("/api/user/login", "/api/user/register").permitAll()
            // 静态资源允许访问
            .antMatchers("/favicon.ico", "/error").permitAll()
            // 管理员接口需要admin角色
            .antMatchers("/api/user/delete/**").hasRole("admin")
            .antMatchers("/api/user/list").hasRole("admin")
            // 其他接口需要认证
            .anyRequest().authenticated()
            .and()
            // 添加JWT过滤器
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
            // 禁用默认登录页面
            .formLogin().disable()
            // 禁用默认HTTP Basic认证
            .httpBasic().disable();

        return http.build();
    }
}