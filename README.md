# Survey Backend Service

Backend service for AI-powered survey creation and management system.

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Python LLM service (optional, for AI features)

### Installation & Run

```bash
cd survey-backend
mvn clean install
mvn spring-boot:run
```

The service will start on **http://localhost:8080**

## 📚 Documentation

For comprehensive documentation, see [doc_dev_backend.md](../doc_dev_backend.md)

## 🔗 API Endpoints

- **API Base:** `http://localhost:8080/api/v1`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **H2 Console:** `http://localhost:8080/h2-console`

## 🎯 Key Features

- ✅ JWT-based authentication
- ✅ Survey CRUD operations
- ✅ AI-powered survey generation
- ✅ Response collection and statistics
- ✅ RESTful API design
- ✅ OpenAPI/Swagger documentation
- ✅ H2 in-memory database

## 🔐 Authentication

All protected endpoints require JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## 💻 Technology Stack

- Spring Boot 3.1.5
- Spring Security with JWT
- Spring Data JPA
- H2 Database
- OpenAPI/Swagger
- Lombok

