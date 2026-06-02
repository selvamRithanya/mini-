# How to fix "localhost connection failed"

## Quick start (recommended)

1. **Start MySQL** (Windows Services → MySQL → Start)
2. Run database script once:
   ```bash
   mysql -u root -p < sql/schema.sql
   ```
3. Edit `src/main/resources/db.properties` — set your MySQL password
4. **Double-click `start-server.bat`** (wait until you see `Server running`)
5. Open in browser:
   - **Login:** http://localhost:8082/login.html

> First run downloads libraries (`download-libs.ps1`) and compiles — may take 1–2 minutes.
> **Do not close** the black command window while using the app.

## Demo login

| Field    | Value               |
|----------|---------------------|
| Username | admin               |
| Password | admin123            |
| Mail ID  | admin@gmail.com     |

If login fails, run in MySQL:
```bash
mysql -u root -p < sql/fix-admin-login.sql
```

## If it still fails

| Problem | Fix |
|---------|-----|
| **Connection refused** | Run `start-server.bat` and wait for "Server running" |
| **Login error / database** | Start MySQL, run `schema.sql`, check `db.properties` |
| **Port 8080 in use** | Close other apps using 8080 or change `PORT` in `EmbeddedServer.java` |
| **Build errors** | Run `download-libs.ps1` then `compile.bat` |

## Manual commands

```bat
download-libs.ps1   REM once — downloads JARs
compile.bat         REM build Java classes
start-server.bat    REM compile + start server
```
