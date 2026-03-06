package com.ecommerce.backend.dto;

import com.ecommerce.backend.model.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    
    private Long userId;
    private String fullName;
    private String email;
    private String address;
    private Integer age;
    private UserRole role;
    private LocalDateTime createdAt;
}
