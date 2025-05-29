package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productID;
    @NotBlank
    @Size(min=3, message="Product name must contain at least 3 characters")
    private String productName;
    private String image;
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;
    @ManyToOne //Many products can belong to one category
    @JoinColumn(name="category_id")//Foreign key in Product table
    private Category category;

}
