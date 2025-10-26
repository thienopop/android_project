package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // tạm thời tắt CSRF cho test Postman
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/**").permitAll() // cho phép tất cả /user/**
            );

        return http.build();
    }
}
