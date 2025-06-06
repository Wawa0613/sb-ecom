package com.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private Integer roleId;

    @ToString.Exclude//@ToString.Exclude 是 Lombok 提供的注解，
    // 用于从自动生成的 toString() 方法中排除某个字段。它的主要作用是避免在打印对象时出现无限递归、敏感信息泄露或冗长的输出。
    @Enumerated(EnumType.STRING)
    @Column(length = 20, name="role_name")
    private  AppRole roleName;
    public Role(AppRole roleName) {
        this.roleName = roleName;
    }
}
