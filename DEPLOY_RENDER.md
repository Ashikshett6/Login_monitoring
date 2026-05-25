# Deploy to Cloud (Render + MySQL)

Live repo: [Ashikshett6/Login_monitoring](https://github.com/Ashikshett6/Login_monitoring)

## Architecture

| Service | Platform | URL (after deploy) |
|---------|----------|-------------------|
| React UI | Render Static Site | `https://login-monitoring-ui.onrender.com` |
| Spring Boot API | Render Web Service (Docker) | `https://login-monitoring-api.onrender.com` |
| MySQL | Railway / PlanetScale / local | connection string in env vars |

---

## Step 1 — MySQL database (required)

Render does not offer free MySQL. Use **Railway** (easiest):

1. Go to [railway.app](https://railway.app) → **New Project** → **Provision MySQL**
2. Open MySQL service → **Connect** → copy variables:
   - `MYSQLHOST`, `MYSQLPORT`, `MYSQLDATABASE`, `MYSQLUSER`, `MYSQLPASSWORD`
3. Build JDBC URL:

```text
jdbc:mysql://HOST:PORT/DATABASE?useSSL=true&allowPublicKeyRetrieval=true&serverTimezone=UTC
```

Example:

```text
jdbc:mysql://containers-us-west-xxx.railway.app:3306/railway?useSSL=true&allowPublicKeyRetrieval=true&serverTimezone=UTC
```

---

## Step 2 — Deploy backend + frontend on Render

### Option A — One-click Blueprint

1. Open: [Deploy to Render](https://render.com/deploy?repo=https://github.com/Ashikshett6/Login_monitoring)
2. Sign in with GitHub → approve **Ashikshett6/Login_monitoring**
3. Render creates **login-monitoring-api** and **login-monitoring-ui**
4. On **login-monitoring-api** → **Environment** → add:

| Key | Value |
|-----|--------|
| `DB_URL` | Your JDBC URL from Step 1 |
| `DB_USERNAME` | MySQL user |
| `DB_PASSWORD` | MySQL password |
| `CORS_ORIGINS` | `https://login-monitoring-ui.onrender.com` |

5. Wait for deploy (first build ~5–10 min on free tier)
6. Open UI: `https://login-monitoring-ui.onrender.com`
7. Login: `admin` / `Admin@123`

### Option B — Manual from dashboard

1. [dashboard.render.com](https://dashboard.render.com) → **New** → **Blueprint** → connect repo `Login_monitoring`
2. Uses root `render.yaml` automatically
3. Set `DB_*` env vars as above

---

## Step 3 — Verify

- API health: `https://login-monitoring-api.onrender.com/health` → `{"status":"UP"}`
- UI loads and login works
- Admin dashboard shows logs after login attempts

---

## Free tier notes

- Render free web services **sleep after 15 min** — first request may take ~30s to wake
- Set **Health Check Path** to `/health` (already in `render.yaml`)
- Change `JWT_SECRET` in production (Render can auto-generate)

---

## Update deployment

Push to `main` on GitHub — Render auto-redeploys if enabled in service settings.

```bash
git push origin main
```
