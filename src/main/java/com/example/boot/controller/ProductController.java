package com.example.boot.controller;

import com.example.boot.entity.Product;
import com.example.boot.exception.ResourceNotFoundException;
import com.example.boot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Get all products
    @GetMapping("/")
    public Page<Product> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "id") String sortBy,  // Default sorting by "id"
                                        @RequestParam(defaultValue = "asc") String sortOrder) { // Default sorting order "asc"
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productService.getAllProducts(pageable);
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/user/{userId}")
    public Page<Product> getProductByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return productService.getProductByUserId(userId, page, size);
    }


    // Create a new product
    @PostMapping("/")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    // Update an existing product
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    // Delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

//    @GetMapping("/search")
//    public Page<Product> searchProducts(
//            @RequestParam(required = false) String name,  // Optional search by name
//            @RequestParam(defaultValue = "0") int page,  // Default to page 0 if not provided
//            @RequestParam(defaultValue = "10") int size  // Default to size 10 if not provided
//    ) {
//        // Call the service layer method to perform search with pagination
//        Pageable pageable = PageRequest.of(page, size);
//        return productService.searchProducts(name, pageable);
//    }


    @GetMapping("/search")
    public Page<Product> searchProducts(@RequestParam String name,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "name") String sortBy,  // Default sorting by "name"
                                        @RequestParam(defaultValue = "asc") String sortOrder) { // Default sorting order "asc"
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productService.searchProducts(name, pageable);
    }
}

