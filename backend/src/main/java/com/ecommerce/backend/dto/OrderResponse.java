package com.ecommerce.backend.dto;

import com.ecommerce.backend.model.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    
    private Long orderId;
    private Long userId;
    private Double totalPrice;
    private OrderStatus orderStatus;
    private List<OrderItemResponse> orderItems;
    private LocalDateTime createdAt;
}
