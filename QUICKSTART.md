# 🚀 Survey Backend - Quick Start Guide

## Prerequisites

- **Java 17+** - [Download Here](https://adoptium.net/)
- **Maven 3.6+** - [Download Here](https://maven.apache.org/download.cgi)

## Installation

### Option 1: Using Startup Script (Recommended)

**Linux/Mac:**
```bash
cd survey-backend
chmod +x run.sh
./run.sh
```

**Windows:**
```cmd
cd survey-backend
run.bat
```

### Option 2: Manual Start

```bash
cd survey-backend
mvn clean install
mvn spring-boot:run
```

## Access Points

Once started, the backend will be available at:

- **API Base:** http://localhost:8080/api/v1
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **H2 Console:** http://localhost:8080/h2-console

## Quick Test

### 1. Health Check
```bash
curl http://localhost:8080/api/v1/health
```

Expected response:
```json
{
  "status": "HEALTHY",
  "service": "Survey Backend",
  "version": "1.0.0"
}
```

### 2. Register User
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"demo","password":"demo1234"}'
```

### 3. Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"demo","password":"demo1234"}'
```

Save the token from the response!

### 4. Create Survey
```bash
TOKEN="your-token-here"

curl -X POST http://localhost:8080/api/v1/surveys/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "title": "Test Survey",
    "questions": [
      {
        "question": "How satisfied are you?",
        "answers": ["Very satisfied", "Satisfied", "Neutral", "Dissatisfied"]
      }
    ]
  }'
```

## Next Steps

1. **Read Full Documentation:** See [doc_dev_backend.md](../doc_dev_backend.md)
2. **Explore API:** Visit http://localhost:8080/swagger-ui.html
3. **Connect Frontend:** Update frontend's `DEMO_MODE` to `false`
4. **Setup AI Features:** Install and run Python LLM service

## Troubleshooting

### Port Already in Use
```bash
# Change port
export SERVER_PORT=9090
mvn spring-boot:run
```

### Build Fails
```bash
# Clean and rebuild
mvn clean install -U
```

### Can't Connect
- Check if service is running
- Verify port 8080 is not blocked
- Check firewall settings

## Support

For detailed information and troubleshooting, see the complete documentation in `doc_dev_backend.md`.

