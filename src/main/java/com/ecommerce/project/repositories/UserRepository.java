package com.ecommerce.project.repositories;

import com.ecommerce.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserName(String username);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);
    /*
    这是一种 查询派生方法（Derived Query Method）。

意思是：根据参数 username，在 User 表里查找 userName 字段匹配的记录。

Optional<User> 是返回类型，意味着结果可以为空。
     */
}
