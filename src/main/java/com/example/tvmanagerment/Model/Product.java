package com.example.tvmanagerment.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private String productName;
    private Long price;
    private String imageUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at_product")
    private LocalDateTime createdAtProduct;
    @PrePersist
    public void prePersist() {
        try {
            createdAtProduct = LocalDateTime.now();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @ManyToOne
    private ProductCategory category;

}
