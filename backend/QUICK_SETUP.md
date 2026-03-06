# Quick Setup Guide - eCommerce REST API

## Prerequisites
- Java 17+
- MySQL 8+
- Maven 3.6+
- Git (optional)

## Step 1: Set Up Database

```bash
# Windows Command Prompt or PowerShell
mysql -u root -p < backend\database\schema.sql

# macOS/Linux
mysql -u root -p < backend/database/schema.sql
```

If you have a password:
```bash
mysql -u root -p<your-password> < backend/database/schema.sql
```

## Step 2: Configure Database Connection

Edit `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=your-password  # Change this
```

## Step 3: Build the Project

```bash
cd backend
./mvnw clean install
```

On Windows:
```cmd
cd backend
mvnw.cmd clean install
```

## Step 4: Run the Application

```bash
./mvnw spring-boot:run
```

On Windows:
```cmd
mvnw.cmd spring-boot:run
```

Server will start at: `http://localhost:8080`

## Step 5: Test the API

### Option A: Using cURL

1. Register a user:
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Test User",
    "email": "test@example.com",
    "password": "password123",
    "address": "123 Main St",
    "age": 30
  }'
```

2. Login:
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

Copy the `token` from the response.

3. Get Products:
```bash
curl -X GET http://localhost:8080/products
```

4. Add to Cart (replace TOKEN with your JWT):
```bash
curl -X POST http://localhost:8080/cart/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "productId": 1,
    "quantity": 1
  }'
```

### Option B: Using Postman

1. Import the collection (if available) or create requests manually
2. Set up Authorization header: `Bearer <your-jwt-token>`
3. Test each endpoint

### Option C: Using VS Code REST Client

Create a file `.http` or `.rest` and use the REST Client extension:

```http
### Register
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "fullName": "Test User",
  "email": "test@example.com",
  "password": "password123",
  "address": "123 Main St",
  "age": 30
}

### Login
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "password123"
}

### Get Products
GET http://localhost:8080/products
```

## Sample User Data (from schema.sql)

### Admin Users
- Email: `admin@ecommerce.com`
- Email: `vendor@ecommerce.com`

### Regular Users
- Email: `johndoe@example.com`
- Email: `janesmith@example.com`
- Email: `bobwilson@example.com`

**Note**: Passwords in the schema are hashed. You should register new users through the `/auth/register` endpoint for testing.

## Project Structure

```
backend/
├── src/main/java/com/ecommerce/backend/
│   ├── controller/                 # REST Controllers (5 endpoints handler)
│   ├── service/                    # Business Logic (4 services)
│   ├── repository/                 # Data Access (6 repositories)
│   ├── model/                      # JPA Entities (6 entities + 2 enums)
│   ├── dto/                        # DTOs (13 classes)
│   ├── config/                     # Configurations (2 classes)
│   ├── security/                   # Security & JWT (4 classes)
│   ├── exception/                  # Exception Handling (4 classes)
│   └── BackendApplication.java     # Main Application
├── src/main/resources/
│   └── application.properties       # Configuration
├── database/
│   └── schema.sql                  # Database Schema
├── API_DOCUMENTATION.md             # Full API Documentation
├── QUICK_SETUP.md                  # This file
├── pom.xml                         # Maven Dependencies
└── mvnw / mvnw.cmd                # Maven Wrapper
```

## Common Issues & Solutions

### Issue: Database Connection Failed
**Solution**: 
- Ensure MySQL is running: `mysql -u root -p`
- Check credentials in application.properties
- Verify database was created: `SHOW DATABASES;`

### Issue: Port 8080 Already in Use
**Solution**:
```properties
# In application.properties
server.port=8081
```

### Issue: JWT Token Invalid
**Solution**:
- Token expires after 24 hours. Login again to get a new token
- Ensure Authorization header format: `Bearer <token>`

### Issue: 403 Forbidden on Admin Endpoints
**Solution**:
- Your user account must have ADMIN role
- Register a new admin user or use existing admin credentials
- Products can only be updated/deleted by the admin who created them

### Issue: Product Update/Delete Fails
**Solution**:
- Verify you're the owner of the product
- Only admins can create/update/delete products
- Check authorization header is included

## Default Configuration

- **Server Port**: 8080
- **Database**: ecommerce_db
- **JWT Expiration**: 24 hours
- **JWT Algorithm**: HS512
- **Password Encryption**: BCrypt
- **Session Creation**: STATELESS (JWT)

## Environment Variables (Optional)

You can set these as environment variables instead of editing properties:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/ecommerce_db
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=your-password
export APP_JWT_SECRET=your-secret-key
export APP_JWT_EXPIRATION=86400000
```

## Development Tips

1. **Enable SQL Logging**: Already enabled in properties for debugging
2. **Hot Reload**: DevTools is included for hot reload during development
3. **API Testing**: Use the provided curl commands or Postman
4. **Database Reset**: Re-run schema.sql to reset database

## Production Checklist

- [ ] Change JWT secret to a strong value
- [ ] Update database credentials
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate` (not update)
- [ ] Disable SQL logging
- [ ] Set `spring.jpa.show-sql=false`
- [ ] Use HTTPS instead of HTTP
- [ ] Implement rate limiting
- [ ] Add API documentation endpoint (Swagger/SpringDoc)
- [ ] Set up proper logging
- [ ] Configure CORS properly

## Next Steps

1. Read the full [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)
2. Test all endpoints
3. Integrate with your frontend application
4. Implement additional features as needed
5. Set up CI/CD pipeline
6. Deploy to production

## Support

For detailed API documentation, see [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)

Happy coding! 🚀
