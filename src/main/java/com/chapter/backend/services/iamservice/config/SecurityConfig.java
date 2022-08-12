package com.chapter.backend.services.iamservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // disabling csrf here, should enable it before using in production
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll();
    }
}