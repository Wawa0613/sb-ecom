package com.ecommerce.project.Security.service;

import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//用来告诉 Spring Security “如何通过用户名加载用户信息”。
// Spring Security 在用户登录时，通过用户名查询数据库，并把查到的用户封装成 UserDetails，供 Spring Security 认证和授权使用。
@Service
public class UserDetailsServiceImpl implements UserDetailsService {//表示你要实现 loadUserByUsername() 方法，这是 Spring Security 必须的一个方法，用于登录认证。
    @Autowired
    UserRepository userRepository;
    @Override
    @Transactional//表示这个方法是一个事务方法，防止懒加载失败
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {//Spring Security 登录时会调用这个方法。
        //findByUserName() 是你写在 UserRepository 里的查询方法。
        //如果找不到用户，就抛出 Spring Security 的标准异常 UsernameNotFoundException，这会被 Spring Security 捕捉并返回认证失败。
        User user=userRepository.findByUserName(username).
                orElseThrow(()->new UsernameNotFoundException("User Not Found with username"+username));
        return UserDetailsImpl.build(user);//将你自己数据库里的 User 对象转成 UserDetailsImpl 对象（这个类实现了 UserDetails 接口）。
        //这样 Spring Security 就能用它来判断账号、密码、权限等。


    }
}
