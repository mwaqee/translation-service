# Translation Management Service (Starter)

A fast, clean Spring Boot service for storing and exporting i18n translations.

## Tech
- Java 21, Spring Boot 3
- Spring Web, Data JPA, Security (JWT), Validation
- PostgreSQL
- OpenAPI via springdoc

> Note on PSR-12: that's a PHP style guide. This project follows idiomatic Java/Spring conventions.

## Run locally (with Docker)
```bash
# Build jar
mvn -q -DskipTests clean package

# Start DB + App (seeds ~120k rows on first boot)
docker compose up --build
```

App runs on `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Auth (Demo)
```bash
TOKEN=$(curl -s -XPOST http://localhost:8080/api/v1/auth/token   -H 'Content-Type: application/json'   -d '{"email":"admin@example.com","password":"secret"}' | jq -r .token)
echo $TOKEN
```

## Create translation
```bash
curl -XPOST http://localhost:8080/api/v1/translations  -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json'  -d '{
  "namespace":"auth",
  "tkey":"login.button",
  "values":[
    {"locale":"en","platform":"web","text":"Sign in"},
    {"locale":"fr","platform":"web","text":"Se connecter"}
  ],
  "tags":["auth","common"]
}'
```

## Search
```bash
curl -H "Authorization: Bearer $TOKEN"   "http://localhost:8080/api/v1/translations?q=login&locale=en&platform=web&size=20"
```

## Export (ETag-enabled)
```bash
curl -i -H "Authorization: Bearer $TOKEN"   "http://localhost:8080/api/v1/export?locale=en&platform=web"
```

- Export returns a flat `{ "key": "text" }` map for the locale+platform (optionally `&namespace=...`).
- Uses ETag; repeat calls with `If-None-Match` may return `304 Not Modified` fast.

## Seed
Seeding is controlled by `app.seed`:
- Docker Compose sets `APP_SEED=true` for first run (120k values).
- Or run locally: `mvn spring-boot:run -Dspring-boot.run.arguments="--app.seed=true"`

## Config
Set DB via env vars:
- `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`

JWT:
- `APP_JWT_SECRET` (min ~32 chars), `app.jwt.issuer`, `app.jwt.expiryMinutes`
