# Urban Nest - Project Setup & Running Guide

## Prerequisites
- **Node.js** (v18 or higher)
- **Java Development Kit (JDK)** (v17 or higher)
- **Maven** (for building the backend)
- **Supabase Account** (for Database & Auth)

## 1. Configuration Setup

### Backend (Spring Boot)
1. Navigate to `backend/src/main/resources/application.properties`.
2. Update the following lines with your Supabase Database credentials (found in Supabase Dashboard > Project Settings > Database):
   ```properties
   spring.datasource.url=jdbc:postgresql://<YOUR_HOST>:5432/postgres
   spring.datasource.username=postgres
   spring.datasource.password=<YOUR_PASSWORD>
   ```

### Frontend (React)
1. Navigate to the `frontend` directory.
2. Create a file named `.env` (you can copy `.env.example`).
3. Add your Supabase API credentials (found in Supabase Dashboard > Project Settings > API):
   ```env
   VITE_SUPABASE_URL=your_supabase_project_url
   VITE_SUPABASE_ANON_KEY=your_supabase_anon_key
   ```

## 2. Running the Application

### Start the Frontend
The frontend is built with Vite.
```bash
cd frontend
npm install
npm run dev
```
Access the web app at: `http://localhost:5173`

### Start the Backend
The backend is a Spring Boot application.
```bash
cd backend
mvn spring-boot:run
```
The API will be available at: `http://localhost:8080`

## 3. Project Structure
- **frontend/**: React + Tailwind CSS + Vite
- **backend/**: Spring Boot + Spring Data JPA + Security
