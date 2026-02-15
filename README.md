# 🚗 Car Service Management System

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2023.x-blue.svg)](https://spring.io/projects/spring-cloud)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Test Coverage](https://img.shields.io/badge/Coverage-85%25-success.svg)](https://github.com/yourusername/car-service-management)

> Enterprise-grade microservices backend built with Spring Boot, Spring Cloud, and Keycloak authentication. This production-ready system demonstrates scalable architecture with secure authentication, API gateway routing, service discovery, and comprehensive audit logging.

---

## 📋 Table of Contents

- [Documentation](#-documentation)
- [Overview](#-overview)
- [System Architecture](#-system-architecture)
- [Key Features](#-key-features)
- [Microservices](#-microservices)
- [Security](#-security)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Testing](#-testing)
- [Database Schema](#-database-schema)
- [Contributing](#-contributing)
- [Author](#-author)

---

## 📚 Documentation

**Complete system documentation available on Confluence:**

👉 [View Full Documentation](https://raikokage.atlassian.net/wiki/external/NjNlYzdlMGZiODZkNDZkMDk0ZDVlZDQzMmZlN2UwNGM)

Includes detailed architecture diagrams, API specifications, design decisions, and implementation guides.

---

## 🎯 Overview

This project implements a comprehensive car service management system using microservices architecture. It showcases enterprise-level patterns including:

- **Secure Authentication** - OAuth2 + JWT via Keycloak
- **API Gateway Pattern** - Centralized routing and security
- **Service Discovery** - Dynamic service registration with Eureka
- **Database-per-Service** - Independent data management
- **Inter-Service Communication** - Secure service-to-service calls
- **Audit Logging** - Complete activity tracking
- **Comprehensive Testing** - 85%+ code coverage

**Business Use Case:** Manage car service records with user validation, vehicle number verification, role-based access control, and complete audit trails.

---

## 🏗️ System Architecture

### Architecture Diagram

```
┌─────────┐
│ Client  │
└────┬────┘
     │
     │ JWT Token
     ▼
┌─────────────────┐
│  API Gateway    │ ◄──── Keycloak (OAuth2/JWT)
│  (Port: 8081)   │
└────┬────────────┘
     │
     │ Service Discovery
     ▼
┌─────────────────┐
│ Eureka Server   │
│  (Port: 8761)   │
└─────────────────┘
     │
     ├──────┬─────────┬──────────┬────────────┐
     │      │         │          │            │
     ▼      ▼         ▼          ▼            ▼
┌────────┐ ┌──────┐ ┌────────┐ ┌──────────┐
│  Car   │ │ User │ │  Car   │ │  Audit   │
│Service │ │Service│ │Validate│ │ Service  │
└───┬────┘ └───┬──┘ └───┬────┘ └────┬─────┘
    │          │        │            │
    ▼          ▼        ▼            ▼
┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐
│car_db  │ │user_db │ │valid_db│ │audit_db│
└────────┘ └────────┘ └────────┘ └────────┘
```

### Request Flow

1. **Client** sends request with JWT token
2. **API Gateway** validates token with Keycloak
3. **Eureka** resolves service location
4. **Car Service** processes request:
   - Validates user via User Service
   - Validates car number via Car Validation Service
   - Saves record to database
   - Logs activity to Audit Service
5. **Response** returned through gateway

---

## ✨ Key Features

### 🔐 Authentication & Authorization
- OAuth2 + JWT token-based authentication via Keycloak
- Role-based access control (ADMIN, USER)
- Secure service-to-service communication using client credentials flow
- Token validation at API Gateway level

### 🌐 API Gateway
- Single entry point for all services
- Route management (`/carservice/**`, `/users/**`, `/audit/**`)
- JWT validation and role enforcement
- Load balancing and fault tolerance

### 🔍 Service Discovery
- Dynamic service registration with Netflix Eureka
- Client-side load balancing
- Health monitoring and failover support

### ✅ Business Logic
- **User Validation** - Ensures customers exist before service creation
- **Vehicle Validation** - Validates Indian vehicle registration numbers
- **Duplicate Prevention** - Prevents redundant service records
- **Date Validation** - Ensures service dates are not in the future

### 📊 Audit Logging
- Complete activity tracking (CREATE, UPDATE, DELETE)
- User attribution and timestamp logging
- Centralized audit database

### 🧪 Testing
- 85%+ code coverage with JUnit 5 and Mockito
- Unit tests for services, controllers, and security
- Integration tests for complete workflows
- JaCoCo for coverage reporting

---

## 🔧 Microservices

### 1️⃣ Car Service
**Port:** 8082 | **Database:** `car_service_db`

Core business service managing car service operations.

**Endpoints:**
- `POST /api/carservice` - Create service record
- `PUT /api/carservice/{id}` - Update service
- `DELETE /api/carservice/{id}` - Delete service
- `GET /api/carservice` - Fetch all records
- `GET /api/carservice/{id}` - Fetch by ID

**Validations:**
- Customer existence check
- Car number format validation
- Service date validation
- Duplicate prevention

---

### 2️⃣ User Profile Service
**Port:** 8083 | **Database:** `user_service_db`

Manages user profiles and roles.

**Endpoints:**
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/users` - List users
- `GET /api/users/validate/{userId}` - Validate user existence

**Features:**
- Role management (ADMIN/USER)
- User preferences
- Profile management

---

### 3️⃣ Car Validation Service
**Port:** 8084 | **Database:** `car_validation_db`

Validates Indian vehicle registration numbers.

**Endpoints:**
- `POST /api/validate` - Validate car number

**Format:** `^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$`

**Examples:**
- ✅ `TN10AB1234`
- ✅ `KA05MN4567`
- ✅ `DL01XY9999`
- ❌ `ABC123` (invalid)

---

### 4️⃣ Audit Service
**Port:** 8085 | **Database:** `audit_db`

Centralized logging for all system activities.

**Endpoints:**
- `POST /api/audit` - Log activity
- `GET /api/audit` - Fetch audit logs

**Log Fields:**
- Action type (CREATE, UPDATE, DELETE)
- User/Admin identifier
- Timestamp
- Entity details

---

### 5️⃣ API Gateway
**Port:** 8081

Single entry point with routing and security.

**Routes:**
- `/carservice/**` → Car Service
- `/users/**` → User Service
- `/audit/**` → Audit Service

**Security:** JWT validation, role-based authorization

---

### 6️⃣ Eureka Service Discovery
**Port:** 8761

Service registry and discovery server.

**Dashboard:** `http://localhost:8761`

---

## 🔐 Security

### Authentication Flow

```
1. User Login
   ↓
2. Keycloak generates JWT
   ↓
3. Client includes JWT in requests
   ↓
4. API Gateway validates JWT
   ↓
5. Gateway routes to service
   ↓
6. Service enforces role-based access
```

### Roles

| Role | Permissions |
|------|-------------|
| **ADMIN** | Full access - create, read, update, delete |
| **USER** | Read-only access to own records |

### Service-to-Service Security

Car Service communicates with User Service using **Keycloak Client Credentials Flow** for secure inter-service authentication.

---

## 💻 Tech Stack

| Technology | Purpose |
|------------|---------|
| **Java 21** | Backend runtime |
| **Spring Boot 3.x** | Microservices framework |
| **Spring Cloud Gateway** | API Gateway |
| **Spring Cloud Netflix Eureka** | Service discovery |
| **Keycloak** | OAuth2 authentication server |
| **MySQL** | Relational database |
| **Maven** | Build automation |
| **JUnit 5** | Unit testing |
| **Mockito** | Mocking framework |
| **JaCoCo** | Code coverage |
| **Swagger/OpenAPI** | API documentation |

---

## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.8+
- MySQL 8.0+
- Keycloak 23.0+

### Installation Steps

#### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/car-service-management.git
cd car-service-management
```

#### 2. Configure MySQL Databases

Create the following databases:

```sql
CREATE DATABASE car_service_db;
CREATE DATABASE user_service_db;
CREATE DATABASE car_validation_db;
CREATE DATABASE audit_db;
```

#### 3. Start Keycloak

```bash
# Download and start Keycloak
cd keycloak-23.0.0
bin/kc.sh start-dev
```

Access Keycloak: `http://localhost:8080`

Configure realm, clients, and users according to the [Confluence documentation](https://raikokage.atlassian.net/wiki/external/NjNlYzdlMGZiODZkNDZkMDk0ZDVlZDQzMmZlN2UwNGM).

#### 4. Start Services (in order)

```bash
# 1. Eureka Server
cd eureka-server
mvn spring-boot:run

# 2. User Service
cd user-service
mvn spring-boot:run

# 3. Car Validation Service
cd car-validation-service
mvn spring-boot:run

# 4. Audit Service
cd audit-service
mvn spring-boot:run

# 5. Car Service
cd car-service
mvn spring-boot:run

# 6. API Gateway
cd api-gateway
mvn spring-boot:run
```

#### 5. Verify Services

Check Eureka Dashboard: `http://localhost:8761`

All services should appear as registered.

---

## 📖 API Documentation

### Accessing Swagger UI

**URL:** `http://localhost:8081/swagger-ui.html`

Swagger is secured with OAuth2. Click **Authorize** and log in with Keycloak credentials:

**Test Users:**
- **Admin:** `admin_user` / `password`
- **User:** `normal_user` / `password`

### Sample API Request

```bash
# Get JWT token from Keycloak
curl -X POST "http://localhost:8080/realms/car-service/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=car-service-client" \
  -d "client_secret=your-secret" \
  -d "username=admin_user" \
  -d "password=password" \
  -d "grant_type=password"

# Create car service record
curl -X POST "http://localhost:8081/carservice/api/carservice" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "carNumber": "TN10AB1234",
    "serviceType": "Oil Change",
    "serviceDate": "2024-02-15",
    "cost": 2500.00
  }'
```

---

## 🧪 Testing

### Run All Tests

```bash
mvn clean test
```

### Generate Coverage Report

```bash
mvn clean test jacoco:report
```

View report: `target/site/jacoco/index.html`

### Test Coverage Summary

| Module | Coverage |
|--------|----------|
| Car Service | 87% |
| User Service | 85% |
| Car Validation Service | 90% |
| Audit Service | 83% |
| API Gateway | 82% |

**Overall:** 85%+

---

## 🗄️ Database Schema

### Database-per-Service Architecture

| Service | Database | Tables |
|---------|----------|--------|
| Car Service | `car_service_db` | `car_services` |
| User Service | `user_service_db` | `users`, `roles` |
| Car Validation | `car_validation_db` | `validation_history` |
| Audit Service | `audit_db` | `audit_logs` |

**Design Principle:** Each microservice owns its database, ensuring loose coupling and independent scalability.

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

Please ensure:
- Code follows project conventions
- Tests are included for new features
- Documentation is updated

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Kunal Pal**  
Full Stack Developer | Java | Spring Boot | Microservices

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue)](https://www.linkedin.com/in/kunal70616c/))
[![GitHub](https://img.shields.io/badge/GitHub-Follow-black)](https://github.com/Kunal70616c)
[![Email](https://img.shields.io/badge/Email-Contact-red)](mailto:kunal.cs.dev@outlook.com)

---

## 🌟 Project Highlights

This project demonstrates:

✅ **Enterprise Architecture** - Production-ready microservices design  
✅ **Security Best Practices** - OAuth2 + JWT implementation  
✅ **Scalability** - Service discovery and load balancing  
✅ **Data Integrity** - Database-per-service pattern  
✅ **Observability** - Comprehensive audit logging  
✅ **Quality Assurance** - 85%+ test coverage  
✅ **Professional Documentation** - Swagger + Confluence  
✅ **Real-World Patterns** - Similar to banking and enterprise systems

---

## 💡 Interview Talking Points

When presenting this project:

> "This system implements a complete enterprise microservices ecosystem with OAuth2 authentication, API gateway routing, service discovery, secure inter-service communication, and audit logging—demonstrating patterns used in production banking and financial systems."

**Key Discussion Areas:**
- Why microservices over monolith?
- How does service discovery work?
- Explain the JWT flow through the gateway
- How do you handle distributed transactions?
- What happens if the User Service is down?
- How would you scale this system?

---

## ⭐ Show Your Support

If you found this project helpful, please give it a ⭐ on GitHub!

---


<div align="center">

**Built with ❤️ using Spring Boot Microservices**

[⬆ Back to Top](#-car-service-management-system)

</div>
