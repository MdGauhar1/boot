package com.example.boot.service;

import com.example.boot.entity.Product;
import com.example.boot.entity.Review;
import com.example.boot.entity.User;
import com.example.boot.payload.ReviewDTO;
import com.example.boot.repository.ProductRepository;
import com.example.boot.repository.ReviewRepository;
import com.example.boot.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ModelMapper modelMapper;

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
    public List<ReviewDTO> getReviewsForProduct(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        if (reviews.isEmpty()) {
            throw new RuntimeException("No reviews found for product with id: " + productId);
        }

        // Convert the Review entities to ReviewResponseDTOs
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .collect(Collectors.toList());
    }



    public List<ReviewDTO> getReviewsByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);

        // Convert Review entities to ReviewResponseDTOs
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .collect(Collectors.toList());
    }
}
