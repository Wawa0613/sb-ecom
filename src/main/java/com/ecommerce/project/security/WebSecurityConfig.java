package com.ecommerce.project.security;

import com.ecommerce.project.security.jwt.AuthEntryPointJwt;
import com.ecommerce.project.security.jwt.AuthTokenFilter;
import com.ecommerce.project.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//配置 Spring Security 行为，如认证、授权、过滤器链等。
@Configuration//表示这是一个配置类，会被 Spring 容器识别并加载。
@EnableWebSecurity//启用 Spring Security 的 web 安全支持。
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;//注入你自己实现的 UserDetailsServiceImpl（用户信息加载逻辑）。
    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;//注入自定义的未授权访问处理器（比如：token 无效、未登录时报 401）。
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){//定义你的自定义 JWT 认证过滤器（从请求头提取并验证 token）。
        return new AuthTokenFilter();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){//这是 Spring Security 中用来设置“数据库认证方式”的配置代码。
        /*
        它是 Spring Security 提供的一个类，用于根据你数据库中的用户信息（用户名 + 密码）进行认证。
         你提供：

         用户名：UserDetailsServiceImpl 中根据用户名查用户

         密码加密器：BCryptPasswordEncoder

         Spring 会用它们验证登录用户是否合法。
         */
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();//实例化了 Spring 提供的“数据库用户认证提供者”对象。
        authenticationProvider.setUserDetailsService(userDetailsService);//告诉 Spring 认证的时候应该通过你自己写的 UserDetailsServiceImpl 来根据用户名加载用户。
        authenticationProvider.setPasswordEncoder(passwordEncoder());//告诉 Spring 用什么加密器来比对密码。 这里用的是 BCryptPasswordEncoder（你之前写了 passwordEncoder() 方法作为 Bean）。


        return authenticationProvider;

    }

    /*这段代码的作用是：获取 Spring Security 的认证核心组件 AuthenticationManager 并把它注入进来。


    它是 Spring Security 验证用户身份的核心接口。

登录时，用户名密码会被封装成一个 Authentication 对象，传给它。

它再去调用：

DaoAuthenticationProvider →

UserDetailsService.loadUserByUsername() 去查用户

用 PasswordEncoder 验证密码
     */

    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();

    }
    @Bean//密码加密器
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJwt))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/admin/**").permitAll()
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }
}
