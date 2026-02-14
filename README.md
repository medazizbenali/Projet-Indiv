# Projet Indiv — Full Stack (Docker + CI + CD Minikube)

Monorepo:
- `backend/` Spring Boot (Flyway, JPA, JWT, Swagger, Actuator)
- `frontend/` React (Vite)
- `docker-compose.yml` 3 services (db, backend, frontend)
- `.github/workflows/ci.yml` CI GitHub Actions (tests + build)
- `k8s/` manifests Minikube (CD démontrée en local)

## 1) Run en Docker
Pré-requis: Docker Desktop

```bash
docker compose up --build
```

URLs:
- Front: http://localhost:3000
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui/index.html
- Health: http://localhost:8080/actuator/health
- Metrics (Prometheus): http://localhost:8080/actuator/prometheus

Auth:
- `POST /api/auth/register` puis `POST /api/auth/login` renvoient un JWT.
- Les endpoints `POST /api/orders` et `GET /api/me/orders` sont protégés (Bearer token).

Arrêt:
```bash
docker compose down
```

## 2) CI (GitHub Actions)
Déclenché sur push/PR. Exécute:
- `mvn test` (backend)
- `npm ci && npm run build` (frontend)

Sécurité (minimal):
- **Trivy** scanne le repo (dépendances/IaC/Docker/K8s) et échoue si vulnérabilités **HIGH/CRITICAL**.

## 3) CD démontrée via Minikube (local)

```bash
minikube start
eval "$(minikube docker-env)"

docker build -t projet-indiv-backend:0.0.1 ./backend
docker build -t projet-indiv-frontend:0.0.1 ./frontend

kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/postgres.yaml
kubectl apply -f k8s/backend.yaml
kubectl apply -f k8s/frontend.yaml

kubectl -n projet-indiv port-forward svc/frontend-svc 3000:80
kubectl -n projet-indiv port-forward svc/backend-svc 8080:80

```

### HTTPS (TLS) via Ingress (Minikube)
Objectif : démontrer HTTPS/TLS en local (sécurité minimale).

1) Activer l'Ingress NGINX de Minikube
```bash
minikube addons enable ingress
```

2) Créer un certificat auto-signé + secret TLS
```bash
kubectl apply -f k8s/namespace.yaml
./scripts/k8s-tls.sh projet-indiv projet-indiv.local projet-indiv-tls
```

3) Appliquer l'Ingress
```bash
kubectl apply -f k8s/ingress.yaml
```

4) Ajouter le host local
```bash
echo "$(minikube ip) projet-indiv.local" | sudo tee -a /etc/hosts
```

Ensuite :
- Front + API via HTTPS: https://projet-indiv.local
- L'API est proxifiée sur `/api/*`

> Note: le navigateur affichera un avertissement (certificat auto-signé), ce qui est normal pour une démo locale.


## HTTPS (docker compose)

```bash
./scripts/gen-local-cert.sh localhost
docker compose -f docker-compose.yml -f docker-compose.tls.yml up --build
```

- Front: https://localhost:8443
- API: https://localhost:8443/api/products

> Note: certificat auto-signé (normal si ton navigateur affiche un warning).

## Tests de charge (k6)

Backend démarré (compose), puis:

```bash
docker run --rm --network host -v "$PWD:/src" grafana/k6 run /src/tests/load/k6-smoke.js
docker run --rm --network host -v "$PWD:/src" grafana/k6 run /src/tests/load/k6-spike.js
```
