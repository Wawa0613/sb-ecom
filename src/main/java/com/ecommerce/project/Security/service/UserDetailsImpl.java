package com.ecommerce.project.Security.service;

import com.ecommerce.project.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
//这是你自定义的类，用来告诉 Spring Security：
//
//当前登录的用户信息都长什么样子，权限是什么，是否被锁定、过期等。
//
//它实现了 Spring Security 提供的接口：UserDetails。
@NoArgsConstructor
@Data
public class UserDetailsImpl implements UserDetails {
    //序列化是将对象的状态转换为字节流的过程，以便可以将对象保存到文件中或通过网络传输。
    //反序列化是将字节流恢复为对象的过程。
    //serialVersionUID 是一个唯一标识符，用于验证序列化和反序列化过程中，类的版本是否兼容。
    //如果类的 serialVersionUID 与序列化时生成的字节流中的 serialVersionUID 不匹配，
    // 反序列化会失败并抛出 InvalidClassException。
    private  static final long serialVersionUID=1L;//serialVersionUID 是序列化机制中用于版本控制的标识符，
    // 显式定义可以避免因类结构变化导致的反序列化问题。

    private Long id;
    private String username;
    private String email;

    public UserDetailsImpl(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }
    public static UserDetailsImpl build(User user){
        // // 把用户的角色（比如 ADMIN、USER）转换成 Spring Security 需要的权限对象
        List<GrantedAuthority>authorities=user.getRoles().stream()
                .map(role->new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());
        // 创建并返回 UserDetailsImpl 实例
        return new UserDetailsImpl(
                user.getUserId(),
                user.getUserName(),  // ⚠️ 注意：这里顺序要和构造函数对应
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    @JsonIgnore//// 防止序列化（比如转 JSON 时）把密码暴露出去
    private String password;
    //  // 当前用户所拥有的权限（角色）
    private Collection<? extends GrantedAuthority>authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    //自定义 equals 方法（判断两个用户对象是否相等，只比较 ID）
    public boolean equals(Object o){
        if (this == o) return true; // 同一个引用
        if (o == null || getClass() != o.getClass()) return false; // 类型不同
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id); // 判断用户ID是否相等
    }


}
