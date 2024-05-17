package com.example.tvmanagerment.Controller;

import com.example.tvmanagerment.Model.ProductCategory;
import com.example.tvmanagerment.Service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/categories")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping
    public List<ProductCategory> getAllCategories() {
        return productCategoryService.getAllCategories();
    }
    @GetMapping("/{id}")
    public ProductCategory getCategoryById(@PathVariable int id) {
        return productCategoryService.getCategoryById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));
    }
    @PostMapping
    public ProductCategory createCategory(@RequestBody ProductCategory category){
        return productCategoryService.saveCategory(category);
    }
    @PutMapping("/{id}")
    public ProductCategory productCategory(@PathVariable int id, @RequestBody ProductCategory category){
        category.setCategoryId(id);
        return productCategoryService.saveCategory(category);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id) {
        productCategoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
