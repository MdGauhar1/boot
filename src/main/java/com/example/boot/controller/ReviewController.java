package com.example.boot.controller;

import com.example.boot.entity.Review;
import com.example.boot.payload.ReviewDTO;
import com.example.boot.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Endpoint to create a review
    @PostMapping
    public ResponseEntity<Review> addReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        Review review = reviewService.addReview(reviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    // Endpoint to get reviews for a product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsForProduct(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewsForProduct(productId);
        return ResponseEntity.ok(reviews);
    }
}
