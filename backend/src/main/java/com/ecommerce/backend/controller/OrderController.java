package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.ApiResponse;
import com.ecommerce.backend.dto.OrderResponse;
import com.ecommerce.backend.dto.OrderStatusRequest;
import com.ecommerce.backend.service.OrderService;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<?>> checkout() {
        Long userId = getCurrentUserId();
        OrderResponse order = orderService.checkout(userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order placed successfully", order));
    }
    
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<?>> getUserOrders() {
        Long userId = getCurrentUserId();
        List<OrderResponse> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", orders));
    }
    
    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<?>> getAdminOrders() {
        Long adminId = getCurrentUserId();
        List<OrderResponse> orders = orderService.getAdminOrders(adminId);
        return ResponseEntity.ok(ApiResponse.success("Admin orders retrieved successfully", orders));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", order));
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<?>> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusRequest request) {
        Long adminId = getCurrentUserId();
        OrderResponse order = orderService.updateOrderStatus(id, request, adminId);
        return ResponseEntity.ok(ApiResponse.success("Order status updated successfully", order));
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new RuntimeException("User not found"));
        return user.getUserId();
    }
}
