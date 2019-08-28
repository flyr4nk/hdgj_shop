package com.hdgj.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // 设置默认的加密方式
        return new BCryptPasswordEncoder();
    }

    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
    		/**
    		 * 配置授权
    		 */
    		.authorizeRequests()	
	    		.antMatchers("/static/**").permitAll()
	    		.antMatchers("/").permitAll()
	    		.antMatchers("/user").hasRole("USER")
    		.and()
    		/**
    		 * 配置表单
    		 */
    		.formLogin()
    			.loginPage("/login")//.failureUrl("/login-error")		//登录页面
    			.loginProcessingUrl("/authentication/form")				//处理登录的请求，要用usernamepassword过滤器来处理这个请求
    			.successForwardUrl("/loginSuccess")
    			.and()
    		.exceptionHandling().accessDeniedPage("/401");		//异常处理重定向到401
    	
    	http.logout().logoutSuccessUrl("/");
    		
    	
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                // 在内存中创建用户并为密码加密
                .withUser("user").password(passwordEncoder().encode("123456")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("123456")).roles("ADMIN");

    }
} 