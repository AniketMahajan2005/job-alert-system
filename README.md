# Job Alert System 🔔

A real-time job alert backend system built with Java Spring Boot.

## Features
- JWT Authentication with role-based access (User/Admin)
- Redis caching for fast job listings
- Real-time WebSocket notifications for matched users
- RESTful APIs for job posting and searching
- MySQL database with JPA/Hibernate

## Live Demo
Base URL: `https://job-alert-system-3.onrender.com`

## Tech Stack
- Java 17
- Spring Boot 4.x
- Spring Security + JWT
- MySQL + Spring Data JPA
- Redis (Upstash)
- WebSocket (STOMP)
- Docker ready

## API Endpoints

| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/auth/register | Public |
| POST | /api/auth/login | Public |
| POST | /api/jobs/post | Admin |
| GET | /api/jobs/all | Authenticated |
| GET | /api/jobs/search?skill= | Authenticated |
| GET | /api/jobs/{id} | Authenticated |
| GET | /api/notifications/my | Authenticated |
| WS | /ws | Public |

## Setup

1. Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/job-alert-system.git
```

2. Configure `application.properties` with your:
    - MySQL credentials
    - Redis (Upstash) credentials
    - JWT secret key

3. Run the application
```bash
./mvnw spring-boot:run
```

## Architecture
- **Controller** → handles HTTP requests
- **Service** → business logic
- **Repository** → database operations
- **Security** → JWT filter chain
- **WebSocket** → real-time notifications
- **Redis** → caching layer