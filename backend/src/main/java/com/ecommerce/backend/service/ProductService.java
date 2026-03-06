package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.ProductRequest;
import com.ecommerce.backend.dto.ProductResponse;
import com.ecommerce.backend.exception.BadRequestException;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }
    
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return mapToProductResponse(product);
    }
    
    @Transactional
    public ProductResponse createProduct(ProductRequest request, Long adminId) {
        Product product = Product.builder()
                .adminId(adminId)
                .productName(request.getProductName())
                .productType(request.getProductType())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .imageUrl(request.getImageUrl())
                .build();
        
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }
    
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductRequest request, Long adminId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        if (!product.getAdminId().equals(adminId)) {
            throw new BadRequestException("You can only update your own products");
        }
        
        product.setProductName(request.getProductName());
        product.setProductType(request.getProductType());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        
        Product updatedProduct = productRepository.save(product);
        return mapToProductResponse(updatedProduct);
    }
    
    @Transactional
    public void deleteProduct(Long productId, Long adminId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        if (!product.getAdminId().equals(adminId)) {
            throw new BadRequestException("You can only delete your own products");
        }
        
        productRepository.deleteById(productId);
    }
    
    public List<ProductResponse> getProductsByAdminId(Long adminId) {
        return productRepository.findByAdminId(adminId)
                .stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }
    
    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .adminId(product.getAdminId())
                .productName(product.getProductName())
                .productType(product.getProductType())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
