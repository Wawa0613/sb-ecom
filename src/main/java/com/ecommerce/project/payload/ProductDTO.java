package com.ecommerce.project.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data//@Data 是 Lombok 提供的注解，它是一个复合注解，可以自动帮你生成 Java 类中常见的样板代码
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productID;

    private String productName;
    private String image;
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;
}
