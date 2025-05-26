package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {//This class represents a Data Transfer Object (DTO) for Category.

    private Long categoryId;
    private String categoryName;
}
