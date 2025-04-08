# 📝 Task Manager App

A full-stack task management application built with **Angular 19** (frontend) and **Spring Boot** (backend).  
Users can sign up, log in, and manage their personal tasks securely with JWT-based authentication. The entire stack is **Dockerized** for easy setup and deployment.

## 🚀 Tech Stack

### Frontend
- Angular 19
- RxJS, Reactive Forms
- Angular Router & HttpClient

### Backend
- Spring Boot 3
- Java 17
- Spring Security with JWT
- Maven
- MySQL


## 📦 Features

- User Registration & Login
- JWT Authentication (Access Token in localStorage, Refresh Token in HTTP-only cookie)
- Protected routes with Angular AuthGuard
- Create, Update, Delete tasks
- Each user can manage their own tasks
- Fully Dockerized (Angular + Spring Boot + MySQL)


## How to Run the App (Using Docker)

1. **Build the Spring Boot App**:

cd backend  
./mvnw clean package -DskipTests 

2. **Start both backend and frontend containers**:

docker-compose up --build

- Frontend will running on: http://localhost:4200
- Backend will be running on: http://localhost:8080


## Running Without Docker

1. **Run the Backend**:

cd backend  
./mvnw spring-boot:run

2. **Run the Frontend**:

cd frontend  
npm install  
ng serve


## Datebase Setup

- MySQL is used as the relational database.
- Two main entities: 
    - User: Handles authentication and owns tasks.
    - Task: Belongs to a single user.
- Relationship: 
    - One User -> Many Tasks


## Authentication & Security

- Implemented using Spring Security + JWT.
- On login: 
    - Access token is stored in localStorage.
    - Refresh token is stored in an HTTP-only cookie.
- Token refresh flow is handled by Angular auth interceptor


## ScreenShots

![Image](https://github.com/user-attachments/assets/558e0ac5-0541-4e6d-9de1-32b33613015e)
![Image](https://github.com/user-attachments/assets/929bbdb3-f39e-4a08-8234-16643d60483e)
![Image](https://github.com/user-attachments/assets/1d0773cc-32e8-4c39-a542-096d94152c07)
![Image](https://github.com/user-attachments/assets/cefac1bc-6754-4091-8a47-2c8f7d0226aa)
![Image](https://github.com/user-attachments/assets/810a98ed-c369-4337-8d7d-493a67be03f4)
