# eCommerce REST API - Technical Specifications

## Project Overview

A production-ready REST API for an eCommerce application with user authentication, product management, shopping cart, and order processing capabilities. Built with Spring Boot and follows industry best practices for security, scalability, and maintainability.

---

## 🛠️ Tech Stack

### Core Framework
- **Java**: 17 LTS
- **Spring Boot**: 3.2.3
- **Build Tool**: Maven 3.6+
- **Application Server**: Embedded Tomcat

### Spring Ecosystem
- **Spring Web**: RESTful API development
- **Spring Data JPA**: Data access and ORM
- **Spring Security**: Authentication & Authorization
- **Spring Boot DevTools**: Hot reload during development

### Database
- **DBMS**: MySQL 8.0+
- **Driver**: MySQL Connector/J (Latest)
- **Connection Pool**: HikariCP (Spring default)
- **Schema**: 6 normalized tables with foreign keys

### Authentication & Security
- **JWT Library**: JJWT 0.12.3
  - jjwt-api: API module
  - jjwt-impl: Implementation
  - jjwt-jackson: JSON support
- **Algorithm**: HMAC SHA-512
- **Password Encryption**: BCrypt

### Validation & Utilities
- **Validation**: Jakarta Bean Validation
- **Lombok**: Annotation processor for boilerplate reduction
- **Jackson**: JSON processing (built into Spring)

### Testing & Development
- **Testing Framework**: JUnit 5 (via Spring Boot Test)
- **Mocking**: Mockito
- **DevTools**: Spring Boot DevTools for rapid development

---

## 📋 System Requirements

### Minimum Requirements
| Component | Version | Notes |
|-----------|---------|-------|
| Java JDK | 17+ | LTS version recommended |
| MySQL | 8.0+ | InnoDB storage engine |
| Maven | 3.6+ | For build and dependency management |
| RAM | 2GB+ | For running the application |
| Disk Space | 500MB+ | For dependencies and database |

### Development Environment
- IDE: IntelliJ IDEA / VS Code / Eclipse
- OS: Windows / macOS / Linux
- Git: For version control (optional)

---

## 🏗️ Architecture Components

### 1. Presentation Layer (Controllers)
```
AuthController          → /auth
ProductController       → /products
CartController          → /cart
OrderController         → /orders
```

**Responsibilities:**
- Handle HTTP requests/responses
- Request validation via annotations
- Route requests to appropriate services
- Return standardized API responses

### 2. Security Layer
```
JwtTokenProvider        → Token generation & validation
JwtAuthenticationFilter → JWT extraction from requests
JwtAuthenticationEntryPoint → Unauthorized access handling
CustomUserDetailsService → Load user details
SecurityConfig          → Spring Security configuration
```

**Responsibilities:**
- Generate and validate JWT tokens
- Extract and validate auth headers
- Load user details from database
- Configure security rules per endpoint

### 3. Business Logic Layer (Services)
```
AuthService             → Registration, login, JWT
ProductService          → Product CRUD operations
CartService             → Cart item management
OrderService            → Order placement, status updates
```

**Responsibilities:**
- Implement business rules
- Data transformation (Entity ↔ DTO)
- Transaction management
- Exception handling and validation

### 4. Data Access Layer (Repositories)
```
UserRepository
ProductRepository
CartRepository
CartItemRepository
OrderRepository
OrderItemRepository
```

**Responsibilities:**
- Database CRUD operations
- Custom query methods
- JPA relationships

### 5. Data Layer (Entities)
```
User            ↔ Cart       (1:1)
User            ↔ Product    (1:N as admin)
User            ↔ Order      (1:N)
Cart            ↔ CartItem   (1:N)
Product         ↔ CartItem   (1:N)
Product         ↔ OrderItem  (1:N)
Order           ↔ OrderItem  (1:N)
```

### 6. Exception Handling
```
GlobalExceptionHandler
├── ResourceNotFoundException (404)
├── BadRequestException (400)
├── ResourceConflictException (409)
├── MethodArgumentNotValidException (400)
└── Generic Exception (500)
```

---

## 📊 Database Schema

### Tables (6 total)

#### 1. users
```sql
PRIMARY KEY: user_id
UNIQUE: email
COLUMNS: full_name, password (hashed), address, age, role, created_at
INDEXES: email, user_id
```

#### 2. products
```sql
PRIMARY KEY: product_id
FOREIGN KEY: admin_id → users
COLUMNS: product_name, product_type, description, price, stock, image_url, created_at
INDEXES: product_name, admin_id, product_id
```

#### 3. cart
```sql
PRIMARY KEY: cart_id
FOREIGN KEY: user_id → users (UNIQUE, 1:1)
COLUMNS: created_at
INDEXES: user_id
```

#### 4. cart_items
```sql
PRIMARY KEY: cart_item_id
FOREIGN KEYS: cart_id → cart, product_id → products
COLUMNS: quantity
UNIQUE: (cart_id, product_id)
INDEXES: cart_id, product_id
```

#### 5. orders
```sql
PRIMARY KEY: order_id
FOREIGN KEY: user_id → users
COLUMNS: total_price, order_status, created_at
INDEXES: user_id, order_id
```

#### 6. order_items
```sql
PRIMARY KEY: order_item_id
FOREIGN KEYS: order_id → orders, product_id → products, admin_id → users
COLUMNS: quantity, price, status
INDEXES: order_id, product_id, admin_id
```

### Relationships
```
users (1) ──── (1) cart (via cart.user_id)
users (1) ──── (N) products (via products.admin_id)
users (1) ──── (N) orders (via orders.user_id)

cart (1) ──── (N) cart_items (via cart_items.cart_id)
products (1) ──── (N) cart_items (via cart_items.product_id)
products (1) ──── (N) order_items (via order_items.product_id)

orders (1) ──── (N) order_items (via order_items.order_id)
```

### Data Types
- IDs: INT AUTO_INCREMENT
- Text: VARCHAR(255) or TEXT
- Money: DECIMAL(10,2)
- Timestamps: TIMESTAMP DEFAULT CURRENT_TIMESTAMP
- Enums: VARCHAR(50) for status fields
- Passwords: VARCHAR(255) for bcrypt hashes

---

## 🔒 Security Architecture

### Authentication Flow
```
┌─────────────────────────────────────────────────┐
│ User sends login credentials to /auth/login     │
└──────────────────────┬──────────────────────────┘
                       ↓
┌─────────────────────────────────────────────────┐
│ AuthService validates credentials               │
│ - Load user from UserRepository                │
│ - Verify password with BCryptPasswordEncoder   │
└──────────────────────┬──────────────────────────┘
                       ↓
┌─────────────────────────────────────────────────┐
│ JwtTokenProvider generates JWT token            │
│ - Subject: user email                          │
│ - Algorithm: HS512                             │
│ - Expiration: 24 hours (configurable)          │
└──────────────────────┬──────────────────────────┘
                       ↓
┌─────────────────────────────────────────────────┐
│ Token returned to client in AuthResponse       │
│ Format: { token, type: "Bearer", user: {...} } │
└─────────────────────────────────────────────────┘
```

### Authorization Flow
```
┌──────────────────────────────────────────────┐
│ Client sends request with Authorization header│
│ Header: "Authorization: Bearer <jwt-token>"  │
└────────────────────┬─────────────────────────┘
                     ↓
┌──────────────────────────────────────────────┐
│ JwtAuthenticationFilter intercepts request   │
│ - Extract token from Authorization header   │
│ - Validate token signature                  │
│ - Extract user email from token claims      │
└────────────────────┬─────────────────────────┘
                     ↓
┌──────────────────────────────────────────────┐
│ CustomUserDetailsService loads user details  │
│ - Query UserRepository for user by email     │
│ - Create UserDetails with authorities       │
│ - Authority: ROLE_USER or ROLE_ADMIN        │
└────────────────────┬─────────────────────────┘
                     ↓
┌──────────────────────────────────────────────┐
│ SecurityConfig checks endpoint authorization │
│ - Public endpoints: permitAll()              │
│ - Protected endpoints: requiresAuthentication│
│ - Admin endpoints: hasRole("ADMIN")          │
└────────────────────┬─────────────────────────┘
                     ↓
┌──────────────────────────────────────────────┐
│ Request proceeds or returns 403 Forbidden    │
└──────────────────────────────────────────────┘
```

### Password Security
- Encoded with BCryptPasswordEncoder
- Cost factor: 10 (default)
- Unique per user
- Never stored in plain text
- Never returned in responses

---

## 🔄 API Design Patterns

### Response Format
```json
{
  "success": boolean,
  "message": "descriptive message",
  "data": {} // null if not applicable
}
```

### Error Response Format
```json
{
  "success": false,
  "message": "error description",
  "data": null
}
```

### Validation Error Response
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "fieldName": "error message",
    "anotherField": "error message"
  }
}
```

### HTTP Status Codes Used
| Code | Meaning | Examples |
|------|---------|----------|
| 200 | OK | GET, PUT, DELETE success |
| 201 | Created | POST success |
| 400 | Bad Request | Validation errors, invalid input |
| 401 | Unauthorized | Missing/invalid JWT token |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Email already exists |
| 500 | Server Error | Unexpected errors |

---

## 🔌 API Versioning Strategy

Currently at **v1** (implicit)

For future versions:
- URL versioning: `/api/v2/products`
- Header versioning: `X-API-Version: 2`
- Query parameter: `?version=2`

---

## 📦 Dependency Management

All dependencies managed via Maven:
- Spring Boot manages most versions
- JJWT explicitly pinned to 0.12.3
- MySQL Connector auto-versioned with Spring IO

### Dependency Tree (High-Level)
```
pom.xml
├── spring-boot-starter-parent (3.2.3)
│   ├── spring-boot-starter-web
│   ├── spring-boot-starter-data-jpa
│   ├── spring-boot-starter-security
│   ├── spring-boot-starter-validation
│   ├── spring-boot-devtools
│   ├── spring-boot-starter-test
│   └── ...other Spring dependencies
├── jjwt (0.12.3)
│   ├── jjwt-api
│   ├── jjwt-impl
│   └── jjwt-jackson
└── mysql-connector-j
```

---

## ⚙️ Configuration Management

### Environment-Specific Configuration
```properties
# Database
spring.datasource.url=jdbc:mysql://host:port/db_name
spring.datasource.username=user
spring.datasource.password=password

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update|validate|create
spring.jpa.show-sql=true|false

# JWT
app.jwt.secret=your-secret-key
app.jwt.expiration=86400000 (milliseconds)

# Server
server.port=8080
server.servlet.context-path=/

# Logging
logging.level.root=INFO
logging.level.com.ecommerce.backend=DEBUG
```

### Configuration Profiles
- **development**: show-sql=true, ddl-auto=update
- **production**: show-sql=false, ddl-auto=validate
- **testing**: h2 in-memory database

---

## 🎯 REST API Principles Followed

1. **Resource-Oriented URLs**
   - `/products` for collections
   - `/products/{id}` for individual resources
   - `/cart` for user's cart
   - `/orders` for orders

2. **Standard HTTP Methods**
   - GET: Retrieve resources
   - POST: Create resources
   - PUT: Update resources
   - DELETE: Delete resources

3. **Proper HTTP Status Codes**
   - 2xx: Success
   - 4xx: Client errors
   - 5xx: Server errors

4. **Stateless Authentication**
   - JWT tokens (not sessions)
   - No server-side session state
   - Token in Authorization header

5. **Consistent Error Handling**
   - Standardized error responses
   - Meaningful error messages
   - Validation error details

6. **CORS Support**
   - Allow cross-origin requests
   - Configurable origins and methods

---

## 📈 Performance Considerations

### Connection Pooling
- HikariCP with optimized settings
- Max pool size: 10
- Min idle: 5
- Connection timeout: 20 seconds

### Query Optimization
- Lazy loading on relationships
- Indexed columns on frequent queries
- N+1 query problem avoided via proper joins

### Caching Opportunities (Future)
- Cache product catalog
- Cache user details after login
- Redis integration ready

### Scalability Features
- Stateless JWT auth (horizontal scaling)
- Database connection pooling
- Lazy loading for memory efficiency

---

## 🔍 Testing Strategy

### Unit Tests
- Service layer tests
- Repository tests (with H2 in-memory DB)
- Utility/Helper tests

### Integration Tests
- Controller tests with MockMvc
- Full application context tests
- Database integration tests

### Testing Tools
- JUnit 5
- Mockito for mocking
- H2 for test database
- AssertJ for fluent assertions

---

## 📚 Documentation Standards

### Code Documentation
- Javadoc for public methods
- Inline comments for complex logic
- Clear variable/method names

### API Documentation
- Endpoint descriptions
- Request/Response examples
- Error scenarios
- Sample usage via curl

### Deployment Documentation
- Setup instructions
- Configuration guide
- Troubleshooting section
- Security checklist

---

## 🚀 Deployment Considerations

### Packaging
- As executable JAR (recommended)
- Standard Spring Boot packaging

### Environment Setup
- Database migration (schema.sql)
- Configuration externalization
- Secret management (JWT secret)
- Logging configuration

### Performance Tuning
- JVM heap size
- Database connection pool size
- Thread pool configuration
- Caching strategy

### Monitoring
- Application logging
- Error tracking (Sentry optional)
- Database query monitoring
- API usage metrics

---

## 🔐 Security Checklist

- [x] Password encryption (BCrypt)
- [x] JWT for stateless auth
- [x] HTTPS ready (configure TLS)
- [x] CORS configured
- [x] Input validation
- [x] SQL injection prevention (JPA)
- [x] Exception handling (no stack traces)
- [x] Role-based access control
- [x] Token expiration
- [x] Secure headers (implement in production)

---

## 📋 Code Quality Metrics

- **Maintainability**: High (Layered architecture)
- **Testability**: High (Service layer design)
- **Scalability**: High (Stateless design)
- **Security**: High (JWT + RBAC)
- **Documentation**: Comprehensive

---

## 🔧 Build & Deployment Commands

```bash
# Development build
mvn clean install

# Run locally
mvn spring-boot:run

# Production build
mvn clean package -DskipTests

# Create executable JAR
mvn clean package

# Run JAR
java -jar backend-0.0.1-SNAPSHOT.jar
```

---

## 📞 Support & Maintenance

- Code is well-commented
- Clear error messages
- Comprehensive documentation
- Sample data provided
- Configuration is externalized

---

## ✅ Compliance & Standards

- **REST API**: Follows RESTful design principles
- **Java**: Follows Oracle Java Code Conventions
- **Security**: OWASP Top 10 aligned
- **Performance**: Spring Boot best practices
- **Scalability**: Horizontal scaling ready

---

*Technical Specifications v1.0*
*Last Updated: 2024*
*Ready for Production: Yes*
