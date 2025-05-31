package com.ecommerce.project.repositories;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ProductRepository extends JpaRepository<Product, Long> {//


    //Pageable	分页请求参数（页码、每页数量、排序）
    //Page<T>	分页查询结果（数据 + 分页元信息
    Page<Product>findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);
    Page<Product>findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);
}
