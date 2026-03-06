package com.ecommerce.backend.dto;

import com.ecommerce.backend.model.OrderStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusRequest {
    
    private OrderStatus orderStatus;
}
