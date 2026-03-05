-- =====================================================
-- MySQL eCommerce Database Schema
-- =====================================================

-- Create Database
CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

-- =====================================================
-- USERS TABLE
-- =====================================================
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL    .\mvnw clean install,
    email VARCHAR(120) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    address TEXT,
    age INT,
    role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- PRODUCTS TABLE
-- =====================================================
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    admin_id INT NOT NULL,
    product_name VARCHAR(150) NOT NULL,
    product_type VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_product_name (product_name),
    INDEX idx_admin_id (admin_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- CART TABLE
-- =====================================================
CREATE TABLE cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- CART_ITEMS TABLE
-- =====================================================
CREATE TABLE cart_items (
    cart_item_id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    FOREIGN KEY (cart_id) REFERENCES cart(cart_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_cart_id (cart_id),
    INDEX idx_product_id (product_id),
    UNIQUE KEY unique_cart_product (cart_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- ORDERS TABLE
-- =====================================================
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    order_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- ORDER_ITEMS TABLE
-- =====================================================
CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    admin_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id),
    INDEX idx_admin_id (admin_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- SEED DATA
-- =====================================================

-- Insert sample users (2 admins, 3 regular users)
INSERT INTO users (full_name, email, password, address, age, role) VALUES
('Admin User', 'admin@ecommerce.com', '$2b$10$Z3b0YJ4JZ4K2L5M6N7O8P', '123 Admin St, Tech City, TC 12345', 35, 'ADMIN'),
('Admin Vendor', 'vendor@ecommerce.com', '$2b$10$Q1W2E3R4T5Y6U7I8O9P0', '456 Vendor Ave, Business Town, BT 67890', 42, 'ADMIN'),
('John Doe', 'johndoe@example.com', '$2b$10$A1B2C3D4E5F6G7H8I9J0', '789 Main St, Customer City, CC 11111', 28, 'USER'),
('Jane Smith', 'janesmith@example.com', '$2b$10$K1L2M3N4O5P6Q7R8S9T0', '321 Oak Ave, Shopper Town, ST 22222', 31, 'USER'),
('Bob Wilson', 'bobwilson@example.com', '$2b$10$U1V2W3X4Y5Z6A7B8C9D0', '654 Pine Rd, Purchase City, PC 33333', 45, 'USER');

-- Insert sample products (created by admins)
INSERT INTO products (admin_id, product_name, product_type, description, price, stock, image_url) VALUES
(1, 'Wireless Headphones', 'Electronics', 'High-quality wireless headphones with noise cancellation', 79.99, 50, 'https://via.placeholder.com/500'),
(1, 'USB-C Cable', 'Accessories', 'Durable USB-C charging cable, 2 meters', 12.99, 200, 'https://via.placeholder.com/500'),
(2, 'Laptop Stand', 'Accessories', 'Adjustable aluminum laptop stand for ergonomic setup', 49.99, 30, 'https://via.placeholder.com/500'),
(2, 'Mechanical Keyboard', 'Electronics', 'RGB mechanical keyboard with custom switches', 129.99, 25, 'https://via.placeholder.com/500'),
(1, 'Phone Case', 'Accessories', 'Protective phone case for iPhone 15', 19.99, 150, 'https://via.placeholder.com/500'),
(2, 'Monitor Light Bar', 'Electronics', 'Smart monitor light bar for eye comfort', 89.99, 40, 'https://via.placeholder.com/500'),
(1, 'Webcam HD', 'Electronics', '1080p HD webcam with built-in microphone', 59.99, 35, 'https://via.placeholder.com/500'),
(2, 'Desk Pad', 'Accessories', 'Large extended desk mouse pad', 29.99, 80, 'https://via.placeholder.com/500');

-- Insert sample carts for users
INSERT INTO cart (user_id) VALUES
(3),
(4),
(5);

-- Insert sample cart items
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES
(1, 1, 1),
(1, 2, 2),
(2, 3, 1),
(2, 4, 1),
(3, 5, 3),
(3, 6, 1);

-- Insert sample orders
INSERT INTO orders (user_id, total_price, order_status) VALUES
(3, 104.97, 'COMPLETED'),
(4, 179.98, 'PROCESSING'),
(3, 29.99, 'COMPLETED'),
(5, 59.99, 'PENDING');

-- Insert sample order items
INSERT INTO order_items (order_id, product_id, admin_id, quantity, price, status) VALUES
(1, 1, 1, 1, 79.99, 'DELIVERED'),
(1, 2, 1, 1, 12.99, 'DELIVERED'),
(1, 5, 1, 1, 12.99, 'DELIVERED'),
(2, 3, 2, 1, 49.99, 'SHIPPED'),
(2, 4, 2, 1, 129.99, 'SHIPPED'),
(3, 5, 1, 3, 29.99, 'DELIVERED'),
(4, 6, 2, 1, 59.99, 'PENDING');

-- =====================================================
-- VERIFY DATA
-- =====================================================
-- Uncomment these queries to verify the data loading

-- SELECT 'Users' AS table_name, COUNT(*) AS count FROM users
-- UNION
-- SELECT 'Products', COUNT(*) FROM products
-- UNION
-- SELECT 'Cart', COUNT(*) FROM cart
-- UNION
-- SELECT 'CartItems', COUNT(*) FROM cart_items
-- UNION
-- SELECT 'Orders', COUNT(*) FROM orders
-- UNION
-- SELECT 'OrderItems', COUNT(*) FROM order_items;
