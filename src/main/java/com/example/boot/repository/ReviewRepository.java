package com.example.boot.repository;

import com.example.boot.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
     List<Review> findByProductId(Long productId);

     Review findByUserId(Long userId);
}
