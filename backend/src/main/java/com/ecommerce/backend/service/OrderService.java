package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.OrderItemResponse;
import com.ecommerce.backend.dto.OrderResponse;
import com.ecommerce.backend.dto.OrderStatusRequest;
import com.ecommerce.backend.exception.BadRequestException;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.model.*;
import com.ecommerce.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Transactional
    public OrderResponse checkout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));
        
        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }
        
        Double totalPrice = 0.0;
        Order order = Order.builder()
                .user(user)
                .orderStatus(OrderStatus.PENDING)
                .build();
        
        Order savedOrder = orderRepository.save(order);
        
        for (CartItem cartItem : cart.getCartItems()) {
            Double itemPrice = cartItem.getProduct().getPrice();
            totalPrice += itemPrice * cartItem.getQuantity();
            
            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .product(cartItem.getProduct())
                    .adminId(cartItem.getProduct().getAdminId())
                    .quantity(cartItem.getQuantity())
                    .price(itemPrice)
                    .status(OrderItemStatus.PENDING)
                    .build();
            
            orderItemRepository.save(orderItem);
            
            // Reduce product stock
            Product product = cartItem.getProduct();
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }
        
        savedOrder.setTotalPrice(totalPrice);
        Order finalOrder = orderRepository.save(savedOrder);
        
        // Clear cart
        cartItemRepository.deleteAll(cart.getCartItems());
        
        return mapToOrderResponse(finalOrder);
    }
    
    public List<OrderResponse> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }
    
    public List<OrderResponse> getAdminOrders(Long adminId) {
        List<OrderItem> adminOrderItems = orderItemRepository.findByAdminId(adminId);
        
        return adminOrderItems.stream()
                .map(OrderItem::getOrder)
                .distinct()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusRequest request, Long adminId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        // Check if admin is authorized to update this order
        List<OrderItem> orderItems = order.getOrderItems();
        boolean isAuthorized = orderItems.stream()
                .anyMatch(item -> item.getAdminId().equals(adminId));
        
        if (!isAuthorized) {
            throw new BadRequestException("You are not authorized to update this order");
        }
        
        order.setOrderStatus(request.getOrderStatus());
        Order updatedOrder = orderRepository.save(order);
        
        return mapToOrderResponse(updatedOrder);
    }
    
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return mapToOrderResponse(order);
    }
    
    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> orderItems = order.getOrderItems() != null ?
                order.getOrderItems().stream()
                        .map(this::mapToOrderItemResponse)
                        .collect(Collectors.toList()) :
                List.of();
        
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .orderItems(orderItems)
                .createdAt(order.getCreatedAt())
                .build();
    }
    
    private OrderItemResponse mapToOrderItemResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .orderItemId(orderItem.getOrderItemId())
                .productId(orderItem.getProduct().getProductId())
                .productName(orderItem.getProduct().getProductName())
                .adminId(orderItem.getAdminId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .status(orderItem.getStatus())
                .build();
    }
}
