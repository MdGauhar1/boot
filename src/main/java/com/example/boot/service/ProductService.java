package com.example.boot.service;


import com.example.boot.entity.Product;
import com.example.boot.exception.ResourceNotFoundException;
import com.example.boot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get a product by ID
    public ResponseEntity<?> getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("product with id " + id +" not found"));
        return ResponseEntity.ok(product);
    }

    // Create a new product
    public ResponseEntity<?> createProduct(Product product) {
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
}

