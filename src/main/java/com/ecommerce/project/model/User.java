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

    //这段代码的意思是：一个用户（User）拥有多个商品（Product），即 User 和 Product 之间是 一对多（OneToMany）关系。
    @OneToMany(mappedBy = "user",
               cascade = {CascadeType.PERSIST, CascadeType.MERGE},//表示如果保存或更新 User，会连带保存或更新关联的 Product；
                orphanRemoval=true)//表示如果某个 Product 不再属于任何 User（从 products 集合中移除），那它也会被从数据库中删除；
    private Set<Product>products;
}
