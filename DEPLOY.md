# Deploy & Run from GitHub

## 1) Push project to GitHub

Open PowerShell in the project folder:

```powershell
cd "C:\Users\dines\Desktop\Login Monitoring"
git init
git add .
git commit -m "Initial commit: Login Monitoring System"
```

Create a new repo on GitHub (website) named `login-monitoring-system`, then:

```powershell
git branch -M main
git remote add origin https://github.com/shettyashik15/login-monitoring-system.git
git push -u origin main
```

Replace `YOUR_USERNAME` with your GitHub username.

---

## 2) Clone and run on any machine

```bash
git clone https://github.com/shettyashik15/login-monitoring-system.git
cd login-monitoring-system
```

### Option A — Docker (recommended)

Requires [Docker Desktop](https://www.docker.com/products/docker-desktop/).

```bash
docker compose up --build
```

- Frontend: http://localhost:3000
- Backend: http://localhost:8080
- MySQL: localhost:3306 (user `root`, password `root`)

Admin login: `admin` / `Admin@123`

### Option B — Local (MySQL + Maven + Node)

1. Start MySQL and create DB (or let Spring Boot create it).
2. Copy `backend/src/main/resources/application.properties.example` → `application.properties` and set your MySQL password.
3. Backend:

```bash
cd backend
mvn spring-boot:run
```

4. Frontend (new terminal):

```bash
cd frontend
cp .env.example .env
npm install
npm start
```

---

## 3) GitHub Actions (automatic)

On every push to `main`, CI runs:

- Maven build for backend
- `npm run build` for frontend

See `.github/workflows/ci.yml`.

---

## 4) Environment variables

| Variable | Where | Purpose |
|----------|--------|---------|
| `DB_URL` | Backend | MySQL JDBC URL |
| `DB_USERNAME` | Backend | DB user |
| `DB_PASSWORD` | Backend | DB password |
| `CORS_ORIGINS` | Backend | Allowed frontend URLs (comma-separated) |
| `REACT_APP_API_URL` | Frontend | Backend API base URL |

---

## 5) Production notes

- Change `security.jwt.secret` to a new Base64 key.
- Use strong MySQL passwords.
- Set `CORS_ORIGINS` to your real frontend domain.
- Set `REACT_APP_API_URL` to your deployed API URL when building the frontend.
