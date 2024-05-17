package com.example.tvmanagerment.Repository;

import com.example.tvmanagerment.Model.Product;
import com.example.tvmanagerment.Model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:keyword%")
    List<Product> findByProductNameContaining(@Param("keyword") String keyword);
    List<Product> findAllByOrderByPriceAsc();
    List<Product> findAllByOrderByPriceDesc();
    List<Product> findAllByOrderByCreatedAtProductDesc();
    @Query("SELECT i.imageUrl FROM Product i")
    List<String> getAllImageUrls();

}
