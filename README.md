# FieldSync Inventory Suite
### *A Full-Stack Data Synchronization & Resource Management Framework*

---

## 📋 Project Overview
**FieldSync** is a technical demonstration of an "offline-first" architecture designed to manage resource flows between remote field locations and a centralized administrative hub. 

It is engineered to support high-reliability data synchronization, secure API communication, and real-time inventory monitoring—specifically tailored for environments where network stability may be inconsistent.

---

## ✨ Key Functionalities
* **Offline-First Architecture**: Ensures field data is captured locally and synchronized automatically when a connection is restored.
* **Secure API Gateway**: Provides a robust interoperability layer for seamless communication between mobile clients and the central database.
* **Real-Time Monitoring**: Enables administrative oversight of inventory levels and resource distribution across multiple geographic nodes.

---

## 🛠 Tech Stack
* **Backend**: Spring Boot (Java) + RESTful API
* **Mobile**: Kotlin (Jetpack Compose)
* **Database**: PostgreSQL
* **Infrastructure**: Docker

## Installation

### Prerequisites

- Java JDK 21 or later
- Maven
- Docker Compose

### Steps

1. **Clone the repository**

   ```bash
   git clone [https://github.com/nanthachak-dev/fieldsync-inventory-suite.git]
   cd fieldsync-inventory-suite

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
