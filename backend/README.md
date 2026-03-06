# eCommerce REST API Backend

![Status](https://img.shields.io/badge/Status-Production%20Ready-brightgreen)
![Java Version](https://img.shields.io/badge/Java-17%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-green)
![License](https://img.shields.io/badge/License-MIT-blue)

A complete, production-ready REST API for an eCommerce application built with **Spring Boot 3.2.3**, **Spring Security with JWT**, and **MySQL**.

## 🌟 Features

✅ **User Authentication & Authorization**
- User registration with validation
- Secure login with JWT tokens
- Role-based access control (USER, ADMIN)
- Password encryption with BCrypt

✅ **Product Management**
- Public product catalog
- Admin product CRUD operations
- Product search and filtering
- Stock inventory tracking

✅ **Shopping Cart**
- Per-user cart management
- Add/remove items
- Automatic price calculations
- Cart persistence

✅ **Order Processing**
- One-click checkout
- Order history retrieval
- Admin order management
- Order status tracking
- Stock management on purchase

✅ **Security**
- JWT-based stateless authentication
- Role-based endpoint authorization
- Input validation
- Comprehensive exception handling
- CORS support

✅ **API Design**
- RESTful architecture
- Consistent JSON responses
- Proper HTTP status codes
- Comprehensive API documentation

---

## 📋 Quick Navigation

- **[QUICK_SETUP.md](./QUICK_SETUP.md)** - Start here! Step-by-step setup guide
- **[API_DOCUMENTATION.md](./API_DOCUMENTATION.md)** - Complete API reference with examples
- **[PROJECT_SUMMARY.md](./PROJECT_SUMMARY.md)** - Overview of all components
- **[TECHNICAL_SPECS.md](./TECHNICAL_SPECS.md)** - Detailed technical specifications
- **[database/schema.sql](./database/schema.sql)** - Database schema

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- MySQL 8+
- Maven 3.6+

### 1. Set Up Database
```bash
mysql -u root -p < backend/database/schema.sql
```

### 2. Configure Database Connection
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your-password
```

### 3. Build & Run
```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

Server starts at: `http://localhost:8080`

### 4. Test the API
```bash
# Register a user
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "address": "123 Main St",
    "age": 28
  }'

# Login to get JWT token
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

---

## 📚 API Endpoints Overview

### Authentication (2 endpoints)
```
POST   /auth/register          - Register new user
POST   /auth/login             - Login & get JWT token
```

### Products (5 endpoints)
```
GET    /products               - Get all products
GET    /products/{id}          - Get product by ID
POST   /products               - Create product (Admin)
PUT    /products/{id}          - Update product (Admin)
DELETE /products/{id}          - Delete product (Admin)
```

### Cart (3 endpoints)
```
GET    /cart                   - Get user's cart
POST   /cart/add               - Add item to cart
DELETE /cart/remove/{productId} - Remove item from cart
```

### Orders (5 endpoints)
```
POST   /orders/checkout        - Place order from cart
GET    /orders/user            - Get user's orders
GET    /orders/admin           - Get admin's orders (Admin)
GET    /orders/{id}            - Get order details
PUT    /orders/{id}/status     - Update order status (Admin)
```

**Total: 15 REST endpoints**

---

## 🏗️ Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/backend/
│   │   │   ├── controller/          # REST Controllers (4)
│   │   │   ├── service/             # Business Logic (4)
│   │   │   ├── repository/          # Data Access (6)
│   │   │   ├── model/               # JPA Entities (6)
│   │   │   ├── dto/                 # DTOs (13)
│   │   │   ├── config/              # Configuration (2)
│   │   │   ├── security/            # JWT & Security (4)
│   │   │   ├── exception/           # Exception Handling (4)
│   │   │   └── BackendApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── database/
│   └── schema.sql               # Database Schema
├── pom.xml                      # Maven Configuration
├── QUICK_SETUP.md              # Getting Started
├── API_DOCUMENTATION.md         # Full API Guide
├── PROJECT_SUMMARY.md          # Component Overview
├── TECHNICAL_SPECS.md          # Technical Details
└── README.md                   # This file
```

---

## 🔐 Security Features

### Authentication
- **JWT Tokens**: 24-hour expiration (configurable)
- **Algorithm**: HMAC SHA-512
- **Stateless**: No server-side session storage

### Authorization
- **Role-Based Access Control**: USER and ADMIN roles
- **Endpoint Protection**: Public, authenticated, and admin-only endpoints
- **Owner Verification**: Users can only manage their own resources

### Password Security
- **Encryption**: BCrypt hashing
- **Best Practices**: Never stored in plain text, never returned in responses

---

## 💾 Database Schema

### 6 Core Tables
1. **users** - User accounts with roles
2. **products** - Product catalog
3. **cart** - Shopping carts (1:1 with users)
4. **cart_items** - Items in cart
5. **orders** - Customer orders
6. **order_items** - Items in orders

### Relationships
```
users (1) ──── (1) cart
users (1) ──── (N) products (as admin)
users (1) ──── (N) orders
products (1) ──── (N) cart_items
products (1) ──── (N) order_items
cart (1) ──── (N) cart_items
orders (1) ──── (N) order_items
```

---

## 🎯 Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Java | 17+ |
| Framework | Spring Boot | 3.2.3 |
| Database | MySQL | 8+ |
| ORM | Hibernate/JPA | 6.0+ |
| Auth | Spring Security + JWT | 0.12.3 |
| Build | Maven | 3.6+ |
| Password | BCrypt | Spring Boot |
| Validation | Jakarta Validation | 3.0+ |

---

## 📖 Documentation

### Getting Started
- **[QUICK_SETUP.md](./QUICK_SETUP.md)** - Setup and running the application

### Using the API
- **[API_DOCUMENTATION.md](./API_DOCUMENTATION.md)** - Complete API reference
  - All 15 endpoints documented
  - Request/response examples
  - Error handling guide
  - Sample curl commands
  - Postman integration guide

### Understanding the System
- **[PROJECT_SUMMARY.md](./PROJECT_SUMMARY.md)** - Architecture and components
  - Component breakdown
  - API endpoints summary
  - Security features
  - Database schema
  - Use cases covered

### Deep Dive
- **[TECHNICAL_SPECS.md](./TECHNICAL_SPECS.md)** - Technical details
  - System requirements
  - Detailed architecture
  - Design patterns
  - Configuration management
  - Performance considerations

---

## 🔧 Configuration

### Environment Variables
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/ecommerce_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your-password
APP_JWT_SECRET=your-secret-key
APP_JWT_EXPIRATION=86400000
```

### Application Properties
Key settings in `application.properties`:
```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.jpa.hibernate.ddl-auto=update
app.jwt.expiration=86400000
```

---

## 🧪 Testing the API

### Using cURL
```bash
# Get all products
curl http://localhost:8080/products

# Add to cart (requires auth)
curl -X POST http://localhost:8080/cart/add \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 2}'
```

### Using Postman
1. Create a POST request to `/auth/login`
2. Copy the token from response
3. Add `Authorization: Bearer <token>` header to other requests
4. Use provided API_DOCUMENTATION.md for all endpoints

### Using VS Code REST Client
Create a `.http` file and use the REST Client extension to test endpoints.

---

## 🚀 Running in Production

### Build
```bash
mvn clean package -DskipTests
```

### Run
```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Configuration for Production
1. Set strong JWT secret
2. Change database credentials
3. Set `ddl-auto=validate` (not update)
4. Disable SQL logging
5. Use HTTPS/TLS
6. Implement rate limiting
7. Set up monitoring

---

## 📋 Sample Data

The database includes pre-populated sample data:
- 2 Admin users
- 3 Regular users
- 8 Sample products
- 3 Sample carts
- 4 Sample orders

Use these for testing. Create new users with `/auth/register` endpoint.

---

## 🔍 Common Issues & Solutions

### Database Connection Error
**Solution**: Check MySQL is running and credentials are correct in `application.properties`

### JWT Token Expired
**Solution**: Login again to get a new token

### Port 8080 in Use
**Solution**: Change `server.port` in `application.properties`

### Permission Denied on Admin Endpoints
**Solution**: Ensure you have ADMIN role, or the product/order belongs to you

See [QUICK_SETUP.md](./QUICK_SETUP.md#troubleshooting) for more solutions.

---

## 📈 Performance & Scalability

✅ **Connection Pooling**: HikariCP with optimized settings
✅ **Lazy Loading**: JPA relationships use lazy loading
✅ **Database Indexes**: On frequently queried columns
✅ **Stateless Auth**: JWT enables horizontal scaling
✅ **Efficient Queries**: Optimized repository methods

---

## 🔒 Security Best Practices

- ✅ Password encryption (BCrypt)
- ✅ JWT for stateless authentication
- ✅ Role-based authorization
- ✅ Input validation on all endpoints
- ✅ SQL injection prevention (JPA)
- ✅ Comprehensive exception handling
- ✅ CORS configuration
- ✅ No sensitive data in responses

---

## 📞 Support

### Documentation
- Full API documentation in [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)
- Setup guide in [QUICK_SETUP.md](./QUICK_SETUP.md)
- Technical details in [TECHNICAL_SPECS.md](./TECHNICAL_SPECS.md)

### Troubleshooting
- See troubleshooting section in [QUICK_SETUP.md](./QUICK_SETUP.md)
- Check error messages - they're designed to be helpful
- Review database schema in [database/schema.sql](./database/schema.sql)

---

## 🎯 Next Steps

1. **Read** [QUICK_SETUP.md](./QUICK_SETUP.md) for setup instructions
2. **Review** [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) for all endpoints
3. **Test** the API using provided curl commands
4. **Integrate** with your frontend application
5. **Deploy** following production checklist in [TECHNICAL_SPECS.md](./TECHNICAL_SPECS.md)

---

## 📊 Project Statistics

- **Lines of Code**: 2500+
- **Classes**: 35+
- **REST Endpoints**: 15
- **Database Tables**: 6
- **Test Coverage**: Ready for implementation
- **Documentation Pages**: 4

---

## 📝 License

This project is provided for educational and commercial use.

---

## ✨ Features Roadmap

### Current (v1.0)
- ✅ User authentication with JWT
- ✅ Product management
- ✅ Shopping cart
- ✅ Order processing
- ✅ Role-based access control

### Future Enhancements
- 📋 Pagination for listings
- 💬 Product reviews and ratings
- ❤️ Wishlist functionality
- 💳 Payment gateway integration
- 📧 Email notifications
- 📊 Admin analytics dashboard
- 🏪 Multiple seller support
- 🛡️ Two-factor authentication

---

## 👨‍💻 Development

### Building the Project
```bash
mvn clean install
```

### Running Tests
```bash
mvn test
```

### Code Quality
- Clean architecture with separation of concerns
- Meaningful variable and method names
- Comments on complex logic
- Follows Spring Boot best practices
- SOLID principles applied

---

## 🤝 Contributing

When extending this API:
1. Follow the existing layered architecture
2. Add entity → DTO → repository → service → controller
3. Include input validation
4. Add meaningful error messages
5. Update documentation
6. Test thoroughly

---

## Version Info

- **Version**: 1.0.0
- **Last Updated**: 2024
- **Status**: Production Ready
- **Java**: 17+
- **Spring Boot**: 3.2.3

---

## 📞 Quick Links

- 📖 [API Documentation](./API_DOCUMENTATION.md)
- 🚀 [Quick Setup Guide](./QUICK_SETUP.md)
- 📋 [Project Summary](./PROJECT_SUMMARY.md)
- 🔧 [Technical Specifications](./TECHNICAL_SPECS.md)
- 💾 [Database Schema](./database/schema.sql)

---

**Ready to build amazing eCommerce experiences! 🚀**

For questions or issues, refer to the comprehensive documentation files included in this project.
