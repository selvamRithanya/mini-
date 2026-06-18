DigitalOcean — Quick deploy (Droplet + Docker)

1) Create Droplet
   - Ubuntu 22.04, add your SSH key. Note the public IP.

2) Copy repo to Droplet
   - Push repo to GitHub and `git clone` on droplet, or SCP the project.

3) On droplet, set the MySQL root password (example uses `root123`)

4) Start services with Docker Compose:

```bash
# on droplet
cd ~/your-repo
export MYSQL_ROOT_PASSWORD=strongpassword
docker compose -f docker-compose.prod.yml up -d --build
```

5) Open your app at http://YOUR_DROPLET_IP/

Notes
- `docker/db.properties` is mounted into the image at `/app/target/classes/db.properties` so the JVM picks up DB settings.
- Replace `root123` with a strong password and set `MYSQL_ROOT_PASSWORD` on the Droplet before `docker compose up`.
- For production, add an Nginx reverse proxy and configure TLS (use Certbot or a managed certificate).
