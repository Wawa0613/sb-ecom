package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name="Users",
        uniqueConstraints = {
        @UniqueConstraint(columnNames="userName"),
        @UniqueConstraint(columnNames = "email")
        })//@Table 注解中的 uniqueConstraints
//用于在数据库表中声明“组合唯一约束”或“字段唯一性约束”。
//
//比如可以防止两个用户拥有相同的 userName 或 email。告诉数据库： userName 和 email 这两个字段必须是唯一的；
//自动生成 SQL： JPA 在建表时，会在 Users 表中添加两个 UNIQUE 约束；
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;
    @NotBlank
    @Size(max=20)
    @Column(name="userName")
    private String userName;

    @NotBlank
    @Size(max=50)
    @Email
    @Column(name="email")
    private String email;

    @NotBlank
    @Size(max=120)
    @Column(name="password")
    private String password;

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
    @Setter
    @Getter
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
               fetch = FetchType.EAGER)
    @JoinTable(name="user_role",
               joinColumns = @JoinColumn(name="user_id"),
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles=new HashSet<>();
}
