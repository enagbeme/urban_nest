# Urban Nest - Project Setup & Running Guide

## Description
Urban Nest is a Spring Boot web application for discovering, listing, and managing rental properties.
The UI is server-rendered using Thymeleaf templates, and the application uses a Supabase-hosted PostgreSQL database.

## Tech Stack
- **Backend:** Spring Boot (Java)
- **UI (Frontend):** Thymeleaf templates
- **Database:** Supabase (PostgreSQL)

## Prerequisites
- **Java Development Kit (JDK)** (v17 or higher)
- **Maven** (for building the backend)
- **Supabase Account** (for Database & Auth)

## 1. Configuration Setup

### Backend (Spring Boot)
1. Navigate to `backend/src/main/resources/application.properties`.
2. Configure your Supabase Database connection. The application is set up to read sensitive values from environment variables (recommended).
   You can find the database host and connection details in Supabase Dashboard > Project Settings > Database.
   ```properties
   spring.datasource.url=jdbc:postgresql://<YOUR_HOST>:5432/postgres
   spring.datasource.username=postgres
   spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
   ```

3. Set the required environment variables (examples):
   ```
   SPRING_DATASOURCE_PASSWORD=your_database_password
   SPRING_MAIL_PASSWORD=your_email_app_password
   ```

## 2. Running the Application

### Start the Application
The application is a Spring Boot app that serves Thymeleaf pages.
```bash
cd backend
mvn spring-boot:run
```
Access the web app at: `http://localhost:8080`

## 3. Project Structure
- **backend/**: Spring Boot + Spring Data JPA + Security + Thymeleaf templates
