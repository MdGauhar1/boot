package com.example.boot.service;


import com.example.boot.entity.Product;
import com.example.boot.entity.User;
import com.example.boot.exception.ResourceNotFoundException;
import com.example.boot.repository.ProductRepository;
import com.example.boot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all products
//    public Page<Product> getAllProducts(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return productRepository.findAll(pageable);
//    }
    // Get all products with pagination and sorting
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // Get a product by ID
    public ResponseEntity<?> getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("product with id " + id +" not found"));
        return ResponseEntity.ok(product);
    }

    public Page<Product> getProductByUserId(Long userId,int page,int size){
        Pageable pageable = PageRequest.of(page,size);
        return productRepository.findByUserId(userId,pageable);
    }

    // Create a new product
    public ResponseEntity<?> createProduct(Product product) {
        User user = userRepository.findById(product.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        product.setUser(user);
        productRepository.save(product);
        return ResponseEntity.ok("Product created successfully");
    }

    // Update a product
    public ResponseEntity<?> updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStockQuantity(product.getStockQuantity());
        productRepository.save(existingProduct);
        return ResponseEntity.ok("Product updated successfully");
    }

    // Delete a product
    public ResponseEntity<?> deleteProduct(Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    public Page<Product> searchProducts(String name, Pageable pageable) {
        Page<Product> products = productRepository.searchByName(name, pageable);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found with name: " + name);
        }
        return products;
    }
}

