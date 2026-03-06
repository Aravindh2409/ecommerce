package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.ApiResponse;
import com.ecommerce.backend.dto.CartItemRequest;
import com.ecommerce.backend.dto.CartResponse;
import com.ecommerce.backend.service.CartService;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getCart() {
        Long userId = getCurrentUserId();
        CartResponse cart = cartService.getCart(userId);
        return ResponseEntity.ok(ApiResponse.success("Cart retrieved successfully", cart));
    }
    
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<?>> addToCart(@Valid @RequestBody CartItemRequest request) {
        Long userId = getCurrentUserId();
        CartResponse cart = cartService.addItemToCart(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item added to cart successfully", cart));
    }
    
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse<?>> removeFromCart(@PathVariable Long productId) {
        Long userId = getCurrentUserId();
        CartResponse cart = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart successfully", cart));
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new RuntimeException("User not found"));
        return user.getUserId();
    }
}
