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
- Swagger / OpenAPI

---

## Installation

### Prerequisites

- Java JDK 21 or later
- PostgreSQL 15 or later
- Maven

### Steps

1. **Clone the repository**

   ```bash
   git clone https://github.com/unlibit-dev/rcrc-seed-management-api.git
   cd rcrc-seed-api

2. **Configure the database**

   ```bash
   # Database connection
   spring.datasource.url=jdbc:postgresql://localhost:5432/rcrc_seed_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   
   # Prevent Spring Boot from initializing schema.sql/data.sql
   spring.sql.init.mode=never
   spring.jpa.hibernate.ddl-auto=none

3. **Build and run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run

> 🐳 The database schema is initialized through Docker Compose.
> You do **not** need to configure `schema.sql` in `application.properties`.

---

## Usage

*** Todo: You can test endpoints using Postman or Swagger UI (if enabled).
Frontend apps such as RCRC Seed Manager (mobile) and RCRC Seed Admin (web) will connect to this API.

---

## API Overview

*** Todo: A detailed API reference (endpoints, request/response models) can be added here or linked from Swagger/OpenAPI docs.

---

## Project Structure

(To be updated)
<img width="291" height="338" alt="image" src="https://github.com/user-attachments/assets/70a0984d-b3e8-4df2-b606-74a7e3ecb7ef" />

---

## License

(Example/Draft)
RCRC Seed Management System – Backend API  
Copyright © 2025 Ministry of Forest and Environment, Lao PDR

This software is developed and maintained by the Ministry of Forest and Environment, Lao PDR.

Permission is granted to authorized personnel and agencies of the Government of Lao PDR to use, copy, modify, and distribute this software for official and non-commercial purposes, provided that this copyright notice and permission statement appear in all copies.

Unauthorized use, distribution, or modification of this software is strictly prohibited.

This software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose, or non-infringement. In no event shall the authors or the Ministry be liable for any claim, damages, or other liability arising from the use of this software.

For inquiries or permission requests, please contact:  
[Insert official contact or email address here]
