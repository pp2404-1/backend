@echo off
REM Survey Backend Startup Script for Windows
REM This script builds and runs the Survey Backend service

echo ==========================================
echo    Survey Backend - Startup Script
echo ==========================================
echo.

REM Check if Java is installed
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [X] Java is not installed. Please install Java 17 or higher.
    pause
    exit /b 1
)

echo [+] Java is installed
java -version
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo [X] Maven is not installed. Please install Maven 3.6 or higher.
    pause
    exit /b 1
)

echo [+] Maven is installed
mvn -version | findstr "Apache Maven"
echo.

REM Clean and build the project
echo Building project...
call mvn clean install -DskipTests

if %errorlevel% neq 0 (
    echo [X] Build failed. Please check the errors above.
    pause
    exit /b 1
)

echo.
echo [+] Build successful!
echo.

REM Run the application
echo Starting Survey Backend...
echo.
echo ==========================================
echo    API: http://localhost:8080/api/v1
echo    Swagger: http://localhost:8080/swagger-ui.html
echo    H2 Console: http://localhost:8080/h2-console
echo ==========================================
echo.

call mvn spring-boot:run

