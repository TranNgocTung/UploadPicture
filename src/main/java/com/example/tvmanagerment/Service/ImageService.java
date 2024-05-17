package com.example.tvmanagerment.Service;

import com.example.tvmanagerment.Model.Image;
import com.example.tvmanagerment.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    private static final String UPLOAD_DIR = "images";

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public String storeImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath);
        Image image = new Image();
        image.setFilename(file.getOriginalFilename());
        imageRepository.save(image);
        return filePath.toString();
    }

    public Resource loadImageAsResource(Long id) throws IOException {
        Optional<Image> optionalImage = imageRepository.findById(id);
        if (!optionalImage.isPresent()) {
            throw new ImageNotFoundException("Image not found with id: " + id);
        }

        Image image = optionalImage.get();
        Path filePath = Paths.get(UPLOAD_DIR).resolve(image.getFilename()).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            return resource;
        } else {
            throw new ImageNotFoundException("Image not found with id: " + id);
        }
    }

    public List<String> getAllImageUrls() {
        List<String> imageUrls = new ArrayList<>();
        List<Image> images = imageRepository.findAll();
        for (Image image : images) {
            String imageUrl = "/api/images/" + image.getId();
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }
}