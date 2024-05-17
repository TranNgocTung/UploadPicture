package com.example.tvmanagerment.Service;

import com.example.tvmanagerment.Model.Image;
import com.example.tvmanagerment.Model.Product;
import com.example.tvmanagerment.Model.ProductCategory;
import com.example.tvmanagerment.Repository.ProductCategoryRepository;
import com.example.tvmanagerment.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategory;

    private static final String UPLOAD_DIR = "images";

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public Product saveOrUpdateProduct(Product product, MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
        String newFileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        Path filePath = uploadPath.resolve(newFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        product.setImageUrl(newFileName);
        return productRepository.save(product);
    }
    public List<Product> getAllProduct() {
        List<Product> products = productRepository.findAll();
        if (products != null) {
            List<String> imageUrls = productRepository.getAllImageUrls();
            products.forEach(product -> {
                if (product.getImageUrl() != null && imageUrls.contains(product.getImageUrl())) {
                    product.setImageUrl(product.getImageUrl().replace("/api/images/", ""));
                }
            });
        }
        return products;
    }

    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    public Resource loadImageAsResource(Integer id) throws IOException {
        Optional<Product> optionalImage = productRepository.findById(id);
        if (!optionalImage.isPresent()) {
            throw new ImageNotFoundException("Image not found with id: " + id);
        }
        Product image = optionalImage.get();
        Path filePath = Paths.get(UPLOAD_DIR).resolve(image.getImageUrl()).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new ImageNotFoundException("Image not found with id: " + id);
        }
    }

    public Product saveOrUpdateProducts(Product product) {
        return productRepository.save(product);
    }


    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }


    public ProductCategory findCategoryById(int categoryId) {
        return productCategory.findById(categoryId).orElse(null);
    }


    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);

    }

    public List<Product> searchProductName(String productName) {
        return productRepository.findByProductNameContaining(productName);
    }

    public List<Product> findAllProductsSortedByPriceAsc() {
        List<Product> sortedProductsAsc = productRepository.findAllByOrderByPriceAsc();
        productRepository.saveAll(sortedProductsAsc);
        return sortedProductsAsc;
    }

    public List<Product> findAllProductsSortedByPriceDesc() {
        List<Product> sortedProductsDesc = productRepository.findAllByOrderByPriceDesc();
        productRepository.saveAll(sortedProductsDesc);
        return sortedProductsDesc;
    }
}



