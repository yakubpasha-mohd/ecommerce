# ShopSphere E-Commerce Platform — Phase 3

Phase 3 adds a complete **User Service** to the Phase 2 React e-commerce UI.

## Included

- React + TypeScript frontend retained from Phase 2
- Spring Boot 3.4 + Java 21 User Service
- PostgreSQL `ecommerce_user` database
- Flyway migrations
- User registration and login
- BCrypt password hashing
- JWT authentication
- Customer/Admin roles
- User profile read/update
- Address CRUD
- Default-address management
- Validation and global exception handling
- Actuator health/metrics
- Swagger/OpenAPI
- Docker multi-stage build
- Docker Compose integration
- Nginx `/api` reverse proxy
- React registration/login/profile/address integration
- Unit test foundation

## Project structure

```text
ecommerce-phase3/
├── frontend/
├── services/
│   └── user-service/
├── infrastructure/
│   └── postgres/init/
├── docker-compose.yml
└── README.md
```

## Start

```bash
docker compose up -d --build
```

Frontend:

```text
http://<SERVER-IP>:3000
```

User Service:

```text
http://<SERVER-IP>:8081/actuator/health
http://<SERVER-IP>:8081/swagger-ui/index.html
```

The frontend uses Nginx to proxy `/api/*` to `user-service:8081`, so browser requests remain same-origin.

## First-time database note

The PostgreSQL init script creates `ecommerce_user` on a fresh PostgreSQL volume. If an existing Phase 2 PostgreSQL volume is already present, the init script will not rerun. For a clean Phase 3 database only, stop the stack and remove the PostgreSQL volume before starting again:

```bash
docker compose down
# WARNING: this removes the existing PostgreSQL data volume
# docker volume rm ecommerce_postgres_data

docker compose up -d --build
```

If you need to preserve existing PostgreSQL data, create the database manually instead:

```sql
CREATE DATABASE ecommerce_user;
```

## API endpoints

```text
POST   /api/v1/users/register
POST   /api/v1/users/login
GET    /api/v1/users/me
PUT    /api/v1/users/me
GET    /api/v1/users/{userId}
PUT    /api/v1/users/{userId}
POST   /api/v1/users/{userId}/addresses
GET    /api/v1/users/{userId}/addresses
PUT    /api/v1/users/{userId}/addresses/{addressId}
DELETE /api/v1/users/{userId}/addresses/{addressId}
PUT    /api/v1/users/{userId}/addresses/{addressId}/default
```

## Security

Set a strong `JWT_SECRET` in production. The Compose value is for development only.

## Tests

```bash
cd services/user-service
mvn test
```

## Phase 3 acceptance

Registration, login, profile, address CRUD, default address, validation, JWT-protected endpoints, Docker health, Swagger, and React integration should all be verified before moving to Phase 4 Catalog + Search.
