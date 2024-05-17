package com.example.tvmanagerment.Repository;

import com.example.tvmanagerment.Model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT i.filename FROM Image i")
    List<String> getAllImageUrls();
 }