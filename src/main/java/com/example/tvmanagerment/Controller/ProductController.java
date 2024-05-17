package com.example.tvmanagerment.Controller;

import com.example.tvmanagerment.Model.Product;
import com.example.tvmanagerment.Model.ProductCategory;
import com.example.tvmanagerment.Model.ProductDTO;
import com.example.tvmanagerment.Service.ProductCategoryService;
import com.example.tvmanagerment.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProduct();
        if (!products.isEmpty()) {
            return ResponseEntity.ok(products);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));
    }

    @GetMapping("/picture/{id}")
    public ResponseEntity<Resource> downloadImageById(@PathVariable Integer id) {
        try {
            Resource resource = productService.loadImageAsResource(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping

    public ResponseEntity<?> addProduct(@RequestParam("file") MultipartFile file,
                                        @ModelAttribute ProductDTO productDTO) throws IOException {
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setPrice(productDTO.getPrice());
        ProductCategory category = productService.findCategoryById(productDTO.getCategoryId());
        product.setCategory(category);
        Product savedProduct = productService.saveOrUpdateProduct(product,file);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable int id,
                                           @RequestParam(value = "file", required = false) MultipartFile file,
                                           @ModelAttribute ProductDTO productDTO) throws IOException{

        Optional<Product> optionalExistingProduct = productService.findById(id);
        if (!optionalExistingProduct.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        Product existingProduct = optionalExistingProduct.get();
        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setPrice(productDTO.getPrice());
        if (productDTO.getCategoryId() != 0) {
            ProductCategory category = productCategoryService.getCategoryById(productDTO.getCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product category not found"));
            existingProduct.setCategory(category);
        }
        if (file != null && !file.isEmpty()) {
            Product updatedProduct = productService.saveOrUpdateProduct(existingProduct, file);
            return ResponseEntity.ok(updatedProduct);
        } else {
            Product updatedProduct = productService.saveOrUpdateProducts(existingProduct);
            return ResponseEntity.ok(updatedProduct);
        }
    }
    @GetMapping("/pagination")
    public Page<Product> getProducts(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size
                                     ){
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> getProductBySearch(@RequestParam(required = false, defaultValue = "") String search) {
        List<Product> productName = productService.searchProductName(search);
        if (productName.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productName, HttpStatus.OK);
    }

    @GetMapping("/ascending")
    public List<Product> getProductsSortedByPriceAscending() {
        return productService.findAllProductsSortedByPriceAsc();
    }

    @GetMapping("/descending")
    public List<Product> getProductsSortedByPriceDescending() {
        return productService.findAllProductsSortedByPriceDesc();
    }
}

