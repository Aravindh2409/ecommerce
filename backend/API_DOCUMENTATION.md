# eCommerce REST API Documentation

## Overview

This is a comprehensive REST API for a small-scale eCommerce application built with Spring Boot, Spring Security, JWT authentication, and MySQL.

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.3**
- **Spring Web**
- **Spring Data JPA**
- **Spring Security with JWT**
- **MySQL 8**
- **Maven**
- **Lombok**

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/backend/
│   │   │   ├── controller/          # REST Controllers
│   │   │   ├── service/             # Business Logic
│   │   │   ├── repository/          # Data Access Layer
│   │   │   ├── model/               # JPA Entities
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── config/              # Configuration Classes
│   │   │   ├── security/            # Security & JWT
│   │   │   └── exception/           # Exception Handling
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── database/
│   └── schema.sql                   # Database Schema
├── pom.xml
└── mvnw
```

## Setup Instructions

### 1. Prerequisites

- Java 17 or higher
- MySQL 8
- Maven 3.6+

### 2. Database Setup

```bash
# Connect to MySQL and run the schema
mysql -u root -p < backend/database/schema.sql
```

Or if you have a password:

```bash
mysql -u root -p<your-password> < backend/database/schema.sql
```

### 3. Configure Database Connection

Edit `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=your-password
```

### 4. Build and Run

```bash
# Navigate to backend directory
cd backend

# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication Endpoints

#### 1. Register User
- **URL**: `POST /auth/register`
- **Authentication**: Not required
- **Request Body**:
```json
{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "address": "123 Main St",
  "age": 28
}
```

- **Response** (201 Created):
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "userId": 1,
    "fullName": "John Doe",
    "email": "john@example.com",
    "address": "123 Main St",
    "age": 28,
    "role": "USER",
    "createdAt": "2024-01-15T10:30:00"
  }
}
```

#### 2. Login User
- **URL**: `POST /auth/login`
- **Authentication**: Not required
- **Request Body**:
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

- **Response** (200 OK):
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "user": {
      "userId": 1,
      "fullName": "John Doe",
      "email": "john@example.com",
      "address": "123 Main St",
      "age": 28,
      "role": "USER",
      "createdAt": "2024-01-15T10:30:00"
    }
  }
}
```

### Product Endpoints

#### 1. Get All Products
- **URL**: `GET /products`
- **Authentication**: Not required
- **Response** (200 OK):
```json
{
  "success": true,
  "message": "Products retrieved successfully",
  "data": [
    {
      "productId": 1,
      "adminId": 1,
      "productName": "Wireless Headphones",
      "productType": "Electronics",
      "description": "High-quality wireless headphones",
      "price": 79.99,
      "stock": 50,
      "imageUrl": "https://...",
      "createdAt": "2024-01-15T10:30:00"
    }
  ]
}
```

#### 2. Get Product by ID
- **URL**: `GET /products/{id}`
- **Authentication**: Not required
- **Response** (200 OK): Single product object

#### 3. Create Product (Admin Only)
- **URL**: `POST /products`
- **Authentication**: Required (ADMIN role)
- **Headers**: `Authorization: Bearer <token>`
- **Request Body**:
```json
{
  "productName": "Laptop Stand",
  "productType": "Accessories",
  "description": "Adjustable aluminum laptop stand",
  "price": 49.99,
  "stock": 30,
  "imageUrl": "https://..."
}
```

- **Response** (201 Created): Created product object

#### 4. Update Product (Admin Only)
- **URL**: `PUT /products/{id}`
- **Authentication**: Required (ADMIN role)
- **Headers**: `Authorization: Bearer <token>`
- **Request Body**: Same as create
- **Response** (200 OK): Updated product object

#### 5. Delete Product (Admin Only)
- **URL**: `DELETE /products/{id}`
- **Authentication**: Required (ADMIN role)
- **Headers**: `Authorization: Bearer <token>`
- **Response** (200 OK):
```json
{
  "success": true,
  "message": "Product deleted successfully",
  "data": null
}
```

### Cart Endpoints

#### 1. Get Cart
- **URL**: `GET /cart`
- **Authentication**: Required
- **Headers**: `Authorization: Bearer <token>`
- **Response** (200 OK):
```json
{
  "success": true,
  "message": "Cart retrieved successfully",
  "data": {
    "cartId": 1,
    "userId": 1,
    "cartItems": [
      {
        "cartItemId": 1,
        "productId": 1,
        "productName": "Wireless Headphones",
        "price": 79.99,
        "quantity": 2,
        "subtotal": 159.98
      }
    ],
    "totalPrice": 159.98,
    "createdAt": "2024-01-15T10:30:00"
  }
}
```

#### 2. Add Item to Cart
- **URL**: `POST /cart/add`
- **Authentication**: Required
- **Headers**: `Authorization: Bearer <token>`
- **Request Body**:
```json
{
  "productId": 1,
  "quantity": 2
}
```

- **Response** (201 Created): Updated cart object

#### 3. Remove Item from Cart
- **URL**: `DELETE /cart/remove/{productId}`
- **Authentication**: Required
- **Headers**: `Authorization: Bearer <token>`
- **Response** (200 OK): Updated cart object

### Order Endpoints

#### 1. Checkout (Place Order)
- **URL**: `POST /orders/checkout`
- **Authentication**: Required
- **Headers**: `Authorization: Bearer <token>`
- **Request Body**: Empty
- **Response** (201 Created):
```json
{
  "success": true,
  "message": "Order placed successfully",
  "data": {
    "orderId": 1,
    "userId": 1,
    "totalPrice": 159.98,
    "orderStatus": "PENDING",
    "orderItems": [
      {
        "orderItemId": 1,
        "productId": 1,
        "productName": "Wireless Headphones",
        "adminId": 1,
        "quantity": 2,
        "price": 79.99,
        "status": "PENDING"
      }
    ],
    "createdAt": "2024-01-15T10:30:00"
  }
}
```

#### 2. Get User Orders
- **URL**: `GET /orders/user`
- **Authentication**: Required
- **Headers**: `Authorization: Bearer <token>`
- **Response** (200 OK): Array of order objects

#### 3. Get Admin Orders
- **URL**: `GET /orders/admin`
- **Authentication**: Required (ADMIN role)
- **Headers**: `Authorization: Bearer <token>`
- **Response** (200 OK): Array of orders for products sold by the admin

#### 4. Get Order by ID
- **URL**: `GET /orders/{id}`
- **Authentication**: Required
- **Headers**: `Authorization: Bearer <token>`
- **Response** (200 OK): Single order object

#### 5. Update Order Status (Admin Only)
- **URL**: `PUT /orders/{id}/status`
- **Authentication**: Required (ADMIN role)
- **Headers**: `Authorization: Bearer <token>`
- **Request Body**:
```json
{
  "orderStatus": "SHIPPED"
}
```

- **Response** (200 OK): Updated order object

**Valid Order Status Values**: `PENDING`, `CONFIRMED`, `SHIPPED`, `DELIVERED`, `CANCELLED`

## Authentication

All protected endpoints require a JWT token in the `Authorization` header:

```
Authorization: Bearer <your-jwt-token>
```

The JWT token is obtained by logging in and expires in 24 hours (configurable via `app.jwt.expiration` in application.properties).

## User Roles

### USER Role
- Can register and login
- Can view all products
- Can manage their cart
- Can place orders (checkout)
- Can view their own orders
- Cannot create/update/delete products

### ADMIN Role
- Can register and login
- Can view all products
- Can create/update/delete their own products
- Can manage their cart
- Can place orders
- Can view their own orders and orders for their products
- Can update order status for their orders

## Error Handling

All errors are returned with appropriate HTTP status codes and messages:

```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

**Common Error Codes**:
- `400 Bad Request`: Invalid input or validation error
- `401 Unauthorized`: Authentication required or invalid token
- `403 Forbidden`: User doesn't have required permission
- `404 Not Found`: Resource not found
- `409 Conflict`: Resource already exists
- `500 Internal Server Error`: Server error

## Model Entities

### User
- `userId` (Long, PK)
- `fullName` (String)
- `email` (String, Unique)
- `password` (String, Encrypted)
- `address` (String)
- `age` (Integer)
- `role` (Enum: USER, ADMIN)
- `createdAt` (LocalDateTime)

### Product
- `productId` (Long, PK)
- `adminId` (Long, FK)
- `productName` (String)
- `productType` (String)
- `description` (String)
- `price` (Double)
- `stock` (Integer)
- `imageUrl` (String)
- `createdAt` (LocalDateTime)

### Cart
- `cartId` (Long, PK)
- `userId` (Long, FK, Unique)
- `createdAt` (LocalDateTime)

### CartItem
- `cartItemId` (Long, PK)
- `cartId` (Long, FK)
- `productId` (Long, FK)
- `quantity` (Integer)

### Order
- `orderId` (Long, PK)
- `userId` (Long, FK)
- `totalPrice` (Double)
- `orderStatus` (Enum: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
- `createdAt` (LocalDateTime)

### OrderItem
- `orderItemId` (Long, PK)
- `orderId` (Long, FK)
- `productId` (Long, FK)
- `adminId` (Long, FK)
- `quantity` (Integer)
- `price` (Double)
- `status` (Enum: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)

## Sample Usage

### 1. Register a User
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "address": "123 Main St",
    "age": 28
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

### 3. Get All Products
```bash
curl -X GET http://localhost:8080/products
```

### 4. Add Item to Cart (with token)
```bash
curl -X POST http://localhost:8080/cart/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-token>" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'
```

### 5. Checkout
```bash
curl -X POST http://localhost:8080/orders/checkout \
  -H "Authorization: Bearer <your-token>"
```

## Security Features

- **JWT Authentication**: Secure token-based authentication
- **Password Encryption**: Passwords are encrypted using BCrypt
- **Role-Based Access Control**: Fine-grained authorization based on user roles
- **CORS Support**: Cross-origin requests are allowed
- **Input Validation**: All inputs are validated using Jakarta validation annotations
- **Exception Handling**: Comprehensive exception handling with meaningful error messages

## Performance Considerations

- **Connection Pooling**: HikariCP connection pool with optimal settings
- **Lazy Loading**: JPA relationships use lazy loading to optimize queries
- **Indexing**: Database indexes on frequently queried columns
- **Pagination**: Ready for pagination implementation if needed

## Future Enhancements

- Add pagination to product and order listings
- Implement product reviews and ratings
- Add wishlist functionality
- Implement payment gateway integration
- Add email notifications
- Implement order tracking
- Add admin dashboard with analytics
- Implement inventory management
- Add user profile management

## Troubleshooting

### 1. Database Connection Error
- Ensure MySQL is running
- Check database credentials in `application.properties`
- Verify database `ecommerce_db` exists

### 2. JWT Token Expired
- Login again to get a new token
- Increase `app.jwt.expiration` if needed (value in milliseconds)

### 3. Product Update/Delete Fails
- Ensure you're using an ADMIN token
- Verify you're the owner of the product (same admin_id)

### 4. Order Checkout Fails
- Ensure cart has items
- Check product stock availability
- Verify user account exists

## License

This project is designed for educational and commercial use.

## Support

For issues or questions, please contact the development team.
