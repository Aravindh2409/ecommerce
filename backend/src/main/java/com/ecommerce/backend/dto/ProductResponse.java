package com.ecommerce.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    
    private Long productId;
    private Long adminId;
    private String productName;
    private String productType;
    private String description;
    private Double price;
    private Integer stock;
    private String imageUrl;
    private LocalDateTime createdAt;
}
