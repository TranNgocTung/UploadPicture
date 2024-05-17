package com.example.tvmanagerment.Repository;

import com.example.tvmanagerment.Model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    List<ProductCategory> findAllByOrderByCreatedAtDesc();

}
