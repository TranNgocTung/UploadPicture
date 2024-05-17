package com.example.tvmanagerment.Service;

import com.example.tvmanagerment.Model.ProductCategory;
import com.example.tvmanagerment.Repository.ProductCategoryRepository;
import com.example.tvmanagerment.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<ProductCategory> getAllCategories() {
        return productCategoryRepository.findAllByOrderByCreatedAtDesc();
    }
    public Optional<ProductCategory> getCategoryById(int id) {
        return productCategoryRepository.findById(id);
    }

    public ProductCategory saveCategory(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }
    public void deleteCategory(int id) {
        productCategoryRepository.deleteById(id);
    }

}
