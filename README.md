# Projet Indiv — Full Stack (Docker + CI + CD Minikube)

Monorepo:
- `backend/` Spring Boot (Flyway, JPA, Basic Auth, Swagger)
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

Auth Basic (pour créer commande / historique):
- user: aziz
- pass: aziz123

Arrêt:
```bash
docker compose down
```

## 2) CI (GitHub Actions)
Déclenché sur push/PR. Exécute:
- `mvn test` (backend)
- `npm ci && npm run build` (frontend)

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
