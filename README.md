# Login Monitoring System (React + Spring Boot + MySQL)

Full-stack login monitoring project with user authentication, login audit logs, account lockout policy, suspicious login detection, and an admin dashboard.

[![CI](https://github.com/Ashikshett6/Login_monitoring/actions/workflows/ci.yml/badge.svg)](https://github.com/Ashikshett6/Login_monitoring/actions/workflows/ci.yml)

> **Deploy to GitHub & run after clone:** see [DEPLOY.md](DEPLOY.md)

## Project Structure

```text
Login Monitoring/
├── backend/
│   └── src/main/java/com/loginmonitoring/backend/
│       ├── config/
│       ├── controller/
│       ├── model/
│       ├── repository/
│       ├── security/
│       └── service/
└── frontend/
    └── src/
        ├── components/
        ├── pages/
        ├── services/
        └── styles/
```

## Features Implemented

- User registration and login
- JWT-based authentication
- BCrypt password encryption
- Login logs: username, IP address, time, browser details, login status
- Account lockout after 5 failed attempts
- Blocked user tracking table
- Suspicious login detection for potential brute-force patterns
- Admin dashboard with summary cards, logs table, blocked users list, unblock action
- Responsive Bootstrap UI

## Quick run with Docker (from GitHub clone)

```bash
docker compose up --build
```

Open http://localhost:3000 — admin: `admin` / `Admin@123`

## Backend Setup

1. Go to `backend`.
2. Copy `application.properties.example` to `application.properties` and set MySQL password (or use env vars `DB_USERNAME`, `DB_PASSWORD`).
3. Ensure MySQL is running.
4. Run:

```bash
mvn spring-boot:run
```

Default backend URL: `http://localhost:8080`

### Default Admin Account

- Username: `admin`
- Password: `Admin@123`

## Frontend Setup

1. Go to `frontend`.
2. Copy `.env.example` to `.env` (optional; default API is `http://localhost:8080/api`).
3. Install dependencies:

```bash
npm install
```

3. Start app:

```bash
npm start
```

Default frontend URL: `http://localhost:3000`

## API Endpoints

### Auth APIs

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/unlock/{username}` (manual unlock route)

### Admin APIs (JWT + ADMIN role required)

- `GET /api/admin/summary`
- `GET /api/admin/logs`
- `GET /api/admin/blocked-users`
- `PUT /api/admin/unblock/{username}`

## Database Schema

SQL schema included in:

- `backend/src/main/resources/schema.sql`

Tables:

- `users`
- `login_logs`
- `blocked_users`
