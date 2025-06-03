# quarkus-communication-demo

A demo app showing different methods of communication between backend built on Quarkus and Angular frontend.

## 🎯 Project Goal

Show different approaches to system designing with Quarkus and Angular.

### Communication:
1. [x] REST API
2. [x] GraphQL
3. [ ] gRPC
4. [ ] Webhook
5. [ ] SSE (Server-Sent Events)
6. [ ] WebSocket
7. [x] Long Polling / Short Polling
8. [ ] Kafka

### Databases:
1. [x] MongoDB
2. [ ] PostgreSQL
3. [ ] Redis

## Other:
1. [ ] Authentication
2. [ ] Caching
3. [x] Docker
5. [x] Testing (Unit, Integration, End-to-End)
6. [x] Documentation (OpenAPI, Swagger, Asciidoc)
7. [x] Internationalization (i18n)
8. [ ] Scheduling

## 🚀 Startup

### Backend (Quarkus)
```bash
./mvnw quarkus:dev
```

### Frontend (Angular)
```bash
cd frontend
npm install
ng serve
```

### Other services
```bash
cp .env.template .env
docker compose up -d
```
