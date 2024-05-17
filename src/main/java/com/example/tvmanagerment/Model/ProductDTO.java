package com.example.tvmanagerment.Model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private String productName;
    private Long price;
    private String imageUrl;
    private int categoryId;
}
