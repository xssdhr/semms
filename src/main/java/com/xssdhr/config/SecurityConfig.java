package com.xssdhr.config;

import com.xssdhr.common.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Spring security 配置类
 * @author xssdhr
 * @3/3/2023 下午4:23
 * @Company 南京点子跳跃传媒有限公司
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private MyUserDetailsServiceImpl myUserDetailsService;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;


    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private static final String URL_WHITELIST[] ={
            "/login",
            "/logout",
            "/captcha",
            "/password",
            "/image/**"
    } ;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }





    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //开启跨域 以及csrf攻击 关闭
        http
                .cors()
                .and()
                .csrf()
                .disable()
                //登录登出配置
                .formLogin()
                    .successHandler(loginSuccessHandler)
                    .failureHandler(loginFailureHandler)
                .and()
                    .logout()
                    .logoutSuccessHandler(logoutSuccessHandler)
                // session禁用配置
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//无状态

                // 拦截规则配置
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll() //白名单，泛型
                .anyRequest().authenticated()
        // 异常处理器配置

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        // 自定义过滤器配置
                .and()
                .addFilter(jwtAuthenticationFilter());


    }


}
