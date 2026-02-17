# RCRC Seed Management API

**RCRC Seed Management API** is the backend service of the **RCRC Seed Management System**, developed for **Naphok**, a division under the **Ministry of Forest and Environment, Lao PDR**.

This project is built using **Spring Boot (Java)** with a **PostgreSQL** database. It provides a set of RESTful APIs that support frontend applications such as the **RCRC Seed Manager** mobile app and the **RCRC Seed Admin** web application.

---

## Features

- User authentication and role-based access control
- Seed inventory and species management
- Data synchronization for mobile apps
- Logging and audit trails
- RESTful API design for frontend integration

---

## Technologies Used

- Java 21+
- Spring Boot
- PostgreSQL
- Spring Data JPA
- Spring Security
- Maven

---

## Installation

### Prerequisites

- Java JDK 21 or later
- Maven
- Docker Compose

### Steps

1. **Clone the repository**

   ```bash
   git clone [https://github.com/nanthachak-dev/fieldsync-inventory-suite.git]
   cd fielsync-inventory-suite

2. Create Postgres Password in Docker Compose Folder
   ```bash
   # Create .env file in fieldsync-inventory-database folder then add below variable
   DB_PASSWORD=your_db_password
   
3. Config application.properties in Spring Boot Project
   ```bash
   # Create .env file in fieldsync-inventory-backend folder then create 2 following variables:
   JWT_SECRET_KEY=your_generated_key
   DB_PASSWORD=your_postgres_password (the same value as DB_PASSWORD in .env file in fieldsync-inventory-database folder

   # Config application.properties
   # Database connection
   spring.datasource.url=jdbc:postgresql://localhost:5432/fieldsync_inventory
   spring.datasource.username=admin
   spring.datasource.password=${DB_PASSWORD}
   
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   
   # Prevent Spring Boot from initializing schema.sql/data.sql
   spring.sql.init.mode=never
   
   # Never respond with full stack trace when responding with error (for production)
   server.error.include-stacktrace=never
   #server.error.include-message=never
   
   # Security Configuration
   application.security.jwt.secret-key=${JWT_SECRET_KEY}
   # JWT expiration time (value and unit)
   # Examples: 1 HOURS, 7 DAYS, 30 MINUTES, 24 HOURS
   application.security.jwt.expiration-time=7
   application.security.jwt.expiration-unit=DAYS

4. **Build and run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   # Recommend using Intellij IDE to compile and run Spring Boot project

> 🐳 The database schema is initialized through Docker Compose.
> You do **not** need to configure `schema.sql` in `application.properties`.

---

## Usage

*** Todo: You can test endpoints using Postman or Swagger UI (if enabled).
Frontend apps such as fieldsync-inventory-app will connect to this API.
to compile and run this app, it is recommended to open fieldsync-inventory-app with Android Studio

---

## API Overview

*** Todo: A detailed API reference (endpoints, request/response models) can be added here or linked from Swagger/OpenAPI docs.

---

## Project Structure

(To be updated)
<img width="291" height="338" alt="image" src="https://github.com/user-attachments/assets/70a0984d-b3e8-4df2-b606-74a7e3ecb7ef" />
