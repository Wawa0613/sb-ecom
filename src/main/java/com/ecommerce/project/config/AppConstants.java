package com.ecommerce.project.config;

public class AppConstants {
    //AppConstants 是一个用于存储应用程序中常用默认参数的类，
    // 例如分页的默认页码、页大小、排序字段和排序方向等，方便在控制器或服务层复用，避免硬编码。
    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "50";
    public static final String SORT_CATEGORIES_BY = "categoryId";
    public static final String SORT_PRODUCTS_BY = "productId";
    public static final String SORT_DIR = "asc";
}
