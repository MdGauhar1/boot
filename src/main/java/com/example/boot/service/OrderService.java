package com.example.boot.service;


import com.example.boot.entity.Order;
import com.example.boot.entity.OrderItem;
import com.example.boot.entity.Product;
import com.example.boot.entity.User;
import com.example.boot.payload.OrderItemRequest;
import com.example.boot.payload.OrderRequest;
import com.example.boot.repository.OrderItemRepository;
import com.example.boot.repository.OrderRepository;
import com.example.boot.repository.ProductRepository;
import com.example.boot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;


    // Create a new order
    public ResponseEntity<?> createOrder(OrderRequest orderRequest) {
        // Find the user who is placing the order
        User user = userRepository.findById(orderRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        // Calculate total order price
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }

        // Create Order
        Order newOrder = new Order();
        newOrder.setOrderDate(new java.util.Date());
        newOrder.setTotalAmount(totalAmount);
        newOrder.setStatus("PENDING");
        newOrder.setUser(user);

        // Save Order
        orderRepository.save(newOrder);

        // Create OrderItems
        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            Product product = productRepository.findById(itemRequest.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setProduct(product);
            orderItem.setOrder(newOrder);
            // Save OrderItem
            orderItemRepository.save(orderItem);
        }

        return ResponseEntity.ok("Order placed successfully!");
    }

    // Get all orders for a user
    public List<Order> getOrdersByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user);
    }

    // Get order by ID
    public ResponseEntity<?> getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        return ResponseEntity.ok(order);
    }
}

