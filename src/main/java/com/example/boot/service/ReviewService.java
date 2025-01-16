package com.example.boot.service;

import com.example.boot.entity.Product;
import com.example.boot.entity.Review;
import com.example.boot.entity.User;
import com.example.boot.payload.ReviewDTO;
import com.example.boot.repository.ProductRepository;
import com.example.boot.repository.ReviewRepository;
import com.example.boot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    // Method to add a review
    public Review addReview(ReviewDTO reviewDTO) {
        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(reviewDTO.getRating());
        review.setReviewText(reviewDTO.getReviewText());

        return reviewRepository.save(review);
    }

    // Method to get reviews for a product
    public List<Review> getReviewsForProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

//    public Review getReviewsByUserId(Long userId) {
//        return reviewRepository.findByUserId(User);
//    }
}
