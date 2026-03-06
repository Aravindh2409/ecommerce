package com.ecommerce.backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    
    private Long cartId;
    private Long userId;
    private List<CartItemResponse> cartItems;
    private Double totalPrice;
    private LocalDateTime createdAt;
}
