package com.ecommerce.backend.dto;

import com.ecommerce.backend.model.OrderItemStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    
    private Long orderItemId;
    private Long productId;
    private String productName;
    private Long adminId;
    private Integer quantity;
    private Double price;
    private OrderItemStatus status;
}
