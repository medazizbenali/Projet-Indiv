# Projet Indiv (Full stack)

Monorepo:
- `backend/` : Spring Boot (Flyway, JPA, Security Basic, Swagger)
- `frontend/` : React (Vite)

## Lancer (local)
### Backend
1) Démarrer PostgreSQL local (ou via Docker) et créer `projet_indiv` (user/pass: projet/projet)
2) Dans `backend/`:
```bash
mvn clean test
mvn spring-boot:run
```
Swagger: `http://localhost:8080/swagger-ui/index.html`

### Frontend
Dans `frontend/`:
```bash
npm install
npm run dev
```
Front: `http://localhost:5173`

## Identifiants (Basic Auth)
- user: `aziz`
- pass: `aziz123`

## Endpoints
- `GET /api/products` (public)
- `POST /api/orders` (auth)
- `GET /api/me/orders` (auth)
- `GET /api/me/orders/{id}` (auth)

## Notes
- Le `pom.xml` est nettoyé: version plugin Spring Boot, encodage UTF-8, Java 17 fixé (suppression warnings).
