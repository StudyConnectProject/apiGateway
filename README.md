# API Gateway — StudyConnect

Punto de entrada único para todos los microservicios de StudyConnect.
Construido con **Spring Cloud Gateway 2023.0.1** + **Spring Boot 3.2.5** + **Java 21**.

---

## Endpoints mapeados por servicio

### Auth Service → `https://authservice-lzwi.onrender.com`
Gateway prefix: `/api/auth/**` (sin JWT requerido)

| Método | Ruta Gateway              | Ruta Downstream          | Auth | Descripción                      |
|--------|---------------------------|--------------------------|------|----------------------------------|
| POST   | `/api/auth/register`      | `/auth/register`         | No   | Registro de usuario              |
| POST   | `/api/auth/login`         | `/auth/login`            | No   | Login, retorna access+refresh    |
| POST   | `/api/auth/refresh`       | `/auth/refresh`          | No   | Renovar access token             |
| POST   | `/api/auth/logout/{id}`   | `/auth/logout/{id}`      | No   | Invalidar sesión                 |
| POST   | `/api/auth/validate-token`| `/auth/validate-token`   | No   | Validar JWT                      |
| GET    | `/api/auth/health`        | `/auth/health`           | No   | Health check del servicio        |

---

### User Service → `https://userservice-829g.onrender.com`
Gateway prefix: `/api/users/**`

| Método | Ruta Gateway                     | Ruta Downstream               | Auth | Descripción                       |
|--------|----------------------------------|-------------------------------|------|-----------------------------------|
| GET    | `/api/users/search`              | `/users/search`               | Sí   | Buscar usuarios                   |
| GET    | `/api/users/me`                  | `/users/me`                   | Sí   | Perfil del usuario autenticado    |
| PUT    | `/api/users/me`                  | `/users/me`                   | Sí   | Actualizar propio perfil          |
| GET    | `/api/users/`                    | `/users/`                     | Sí (Admin) | Listar todos los usuarios  |
| GET    | `/api/users/:id`                 | `/users/:id`                  | Sí   | Obtener usuario por ID            |
| POST   | `/api/users/`                    | `/users/`                     | Sí   | Crear usuario                     |
| PUT    | `/api/users/:id`                 | `/users/:id`                  | Sí   | Actualizar usuario                |
| DELETE | `/api/users/:id`                 | `/users/:id`                  | Sí (Admin) | Eliminar usuario           |
| PATCH  | `/api/users/:id/role`            | `/users/:id/role`             | Sí (Admin) | Cambiar rol               |
| PATCH  | `/api/users/:id/status`          | `/users/:id/status`           | Sí (Admin) | Activar/desactivar usuario |
| GET    | `/api/users/:id/courses`         | `/users/:id/courses`          | Sí   | Cursos del usuario                |
| GET    | `/api/users/:id/matches`         | `/users/:id/matches`          | Sí   | Matchings del usuario             |

---

### Course Service → `https://courseservice-0n2q.onrender.com`
Gateway prefix: `/api/courses/**` → reescrito a `/api/v1/courses/**`

| Método | Ruta Gateway                                  | Ruta Downstream                              | Auth | Descripción                   |
|--------|-----------------------------------------------|----------------------------------------------|------|-------------------------------|
| POST   | `/api/courses`                                | `/api/v1/courses`                            | Sí   | Crear curso (tutor/admin)     |
| GET    | `/api/courses/search`                         | `/api/v1/courses/search`                     | Sí   | Buscar cursos                 |
| GET    | `/api/courses/my-enrollments`                 | `/api/v1/courses/my-enrollments`             | Sí   | Mis inscripciones (student)   |
| GET    | `/api/courses`                                | `/api/v1/courses`                            | Sí   | Listar cursos                 |
| GET    | `/api/courses/{id}`                           | `/api/v1/courses/{id}`                       | Sí   | Detalle de curso              |
| PUT    | `/api/courses/{id}`                           | `/api/v1/courses/{id}`                       | Sí   | Actualizar curso              |
| DELETE | `/api/courses/{id}`                           | `/api/v1/courses/{id}`                       | Sí   | Eliminar curso                |
| PATCH  | `/api/courses/{id}/status`                    | `/api/v1/courses/{id}/status`                | Sí   | Cambiar estado del curso      |
| POST   | `/api/courses/{id}/enroll`                    | `/api/v1/courses/{id}/enroll`                | Sí   | Inscribirse (student)         |
| DELETE | `/api/courses/{id}/enroll`                    | `/api/v1/courses/{id}/enroll`                | Sí   | Cancelar inscripción          |
| GET    | `/api/courses/{id}/students`                  | `/api/v1/courses/{id}/students`              | Sí   | Listar estudiantes (tutor)    |
| GET    | `/api/courses/{id}/tutor`                     | `/api/v1/courses/{id}/tutor`                 | Sí   | Info del tutor del curso      |
| POST   | `/api/courses/{id}/students`                  | `/api/v1/courses/{id}/students`              | Sí   | Añadir estudiante (tutor)     |
| DELETE | `/api/courses/{id}/students/{studentId}`      | `/api/v1/courses/{id}/students/{studentId}`  | Sí   | Quitar estudiante (tutor)     |
| POST   | `/api/courses/{id}/resources`                 | `/api/v1/courses/{id}/resources`             | Sí   | Añadir recurso (tutor)        |
| GET    | `/api/courses/{id}/resources`                 | `/api/v1/courses/{id}/resources`             | Sí   | Listar recursos               |
| DELETE | `/api/courses/{id}/resources/{resourceId}`    | `/api/v1/courses/{id}/resources/{resourceId}`| Sí   | Eliminar recurso              |

---

### Exam Service → `https://examservice-6p78.onrender.com`
Gateway prefix: `/api/exams/**`

| Método | Ruta Gateway                                 | Ruta Downstream                            | Auth | Descripción                         |
|--------|----------------------------------------------|--------------------------------------------|------|-------------------------------------|
| GET    | `/api/exams/attempts/student/:studentId`     | `/api/exams/attempts/student/:studentId`   | Sí   | Intentos de un estudiante           |
| GET    | `/api/exams/attempts/:attemptId`             | `/api/exams/attempts/:attemptId`           | Sí   | Detalle de un intento               |
| POST   | `/api/exams/`                                | `/api/exams/`                              | Sí   | Crear examen                        |
| GET    | `/api/exams/`                                | `/api/exams/`                              | Sí   | Listar exámenes                     |
| GET    | `/api/exams/:id`                             | `/api/exams/:id`                           | Sí   | Obtener examen                      |
| PUT    | `/api/exams/:id`                             | `/api/exams/:id`                           | Sí   | Actualizar examen                   |
| DELETE | `/api/exams/:id`                             | `/api/exams/:id`                           | Sí   | Eliminar examen                     |
| PATCH  | `/api/exams/:id/status`                      | `/api/exams/:id/status`                    | Sí   | Cambiar estado del examen           |
| POST   | `/api/exams/:id/questions`                   | `/api/exams/:id/questions`                 | Sí   | Añadir pregunta                     |
| PUT    | `/api/exams/:id/questions/:qId`              | `/api/exams/:id/questions/:qId`            | Sí   | Actualizar pregunta                 |
| DELETE | `/api/exams/:id/questions/:qId`              | `/api/exams/:id/questions/:qId`            | Sí   | Eliminar pregunta                   |
| GET    | `/api/exams/:id/take`                        | `/api/exams/:id/take`                      | Sí   | Obtener examen para rendir          |
| POST   | `/api/exams/:id/attempts`                    | `/api/exams/:id/attempts`                  | Sí   | Enviar intento                      |
| GET    | `/api/exams/:id/attempts`                    | `/api/exams/:id/attempts`                  | Sí   | Listar intentos del examen          |

---

### Matching Service → `https://matchingservice.onrender.com`
Gateway prefix: `/api/matching/**`

| Método | Ruta Gateway                               | Ruta Downstream                         | Auth | Descripción                    |
|--------|--------------------------------------------|-----------------------------------------|------|--------------------------------|
| POST   | `/api/matching/request`                    | `/api/matching/request`                 | Sí   | Crear solicitud de matching    |
| GET    | `/api/matching/`                           | `/api/matching/`                        | Sí   | Listar solicitudes             |
| GET    | `/api/matching/{id}`                       | `/api/matching/{id}`                    | Sí   | Obtener solicitud              |
| PUT    | `/api/matching/{id}`                       | `/api/matching/{id}`                    | Sí   | Actualizar solicitud           |
| DELETE | `/api/matching/{id}`                       | `/api/matching/{id}`                    | Sí   | Cancelar solicitud             |
| PATCH  | `/api/matching/{id}/status`                | `/api/matching/{id}/status`             | Sí   | Cambiar estado                 |
| POST   | `/api/matching/{id}/accept`                | `/api/matching/{id}/accept`             | Sí   | Aceptar match                  |
| POST   | `/api/matching/{id}/reject`                | `/api/matching/{id}/reject`             | Sí   | Rechazar match                 |
| POST   | `/api/matching/{id}/offer`                 | `/api/matching/{id}/offer`              | Sí   | Tutor se ofrece para un match  |
| GET    | `/api/matching/{userId}/matches`           | `/api/matching/{userId}/matches`        | Sí   | Matches de un usuario          |
| GET    | `/api/matching/recommendations/{userId}`   | `/api/matching/recommendations/{userId}`| Sí   | Recomendaciones de tutores     |
| POST   | `/api/matching/process`                    | `/api/matching/process`                 | Sí   | Procesar matches pendientes    |

---

### Chat Service → `https://chatservice-l7qc.onrender.com`
Gateway prefix REST: `/api/chat/**` → reescrito a `/**`
Gateway prefix WS: `/socket.io/**` (Socket.IO)

| Método    | Ruta Gateway                                              | Ruta Downstream                              | Auth | Descripción                      |
|-----------|-----------------------------------------------------------|----------------------------------------------|------|----------------------------------|
| GET       | `/api/chat/conversations/unread-count`                   | `/conversations/unread-count`                | Sí   | Mensajes no leídos               |
| POST      | `/api/chat/conversations/`                               | `/conversations/`                            | Sí   | Crear conversación               |
| GET       | `/api/chat/conversations/`                               | `/conversations/`                            | Sí   | Listar conversaciones            |
| GET       | `/api/chat/conversations/:id`                            | `/conversations/:id`                         | Sí   | Obtener conversación             |
| PATCH     | `/api/chat/conversations/:id`                            | `/conversations/:id`                         | Sí   | Archivar conversación            |
| POST      | `/api/chat/conversations/:id/read-receipts`              | `/conversations/:id/read-receipts`           | Sí   | Marcar como leído                |
| GET       | `/api/chat/conversations/:id/participants`               | `/conversations/:id/participants`            | Sí   | Listar participantes             |
| POST      | `/api/chat/conversations/:id/participants`               | `/conversations/:id/participants`            | Sí   | Añadir participante              |
| DELETE    | `/api/chat/conversations/:id/participants/:userId`       | `/conversations/:id/participants/:userId`    | Sí   | Quitar participante              |
| GET       | `/api/chat/conversations/:id/messages`                   | `/conversations/:id/messages`                | Sí   | Mensajes de conversación         |
| POST      | `/api/chat/conversations/:id/messages`                   | `/conversations/:id/messages`                | Sí   | Enviar mensaje                   |
| GET       | `/api/chat/conversations/messages/:messageId`            | `/conversations/messages/:messageId`         | Sí   | Obtener mensaje                  |
| DELETE    | `/api/chat/conversations/messages/:messageId`            | `/conversations/messages/:messageId`         | Sí   | Eliminar mensaje                 |
| PUT       | `/api/chat/conversations/messages/:messageId/reactions/:emoji` | `/conversations/messages/:messageId/reactions/:emoji` | Sí | Añadir reacción |
| DELETE    | `/api/chat/conversations/messages/:messageId/reactions/:emoji` | `/conversations/messages/:messageId/reactions/:emoji` | Sí | Quitar reacción |
| WebSocket | `/socket.io/**`                                          | `/socket.io/**`                              | Sí   | Socket.IO (tiempo real)          |

---

### Analytics Service → `https://backend-analytics-4xq4.onrender.com`
Gateway prefix: `/api/analytics/**` → reescrito a `/**`

| Método | Ruta Gateway                             | Ruta Downstream             | Auth | Descripción                  |
|--------|------------------------------------------|-----------------------------|------|------------------------------|
| POST   | `/api/analytics/events/`                 | `/events/`                  | Sí   | Registrar evento             |
| GET    | `/api/analytics/events/`                 | `/events/`                  | Sí   | Consultar eventos            |
| GET    | `/api/analytics/events/user-activity/:userId` | `/events/user-activity/:userId` | Sí | Actividad de usuario   |
| GET    | `/api/analytics/events/:id`              | `/events/:id`               | Sí   | Evento por ID                |
| DELETE | `/api/analytics/events/:id`              | `/events/:id`               | Sí   | Eliminar evento              |
| GET    | `/api/analytics/reports/active-users`    | `/reports/active-users`     | Sí   | Usuarios activos             |
| GET    | `/api/analytics/reports/popular-courses` | `/reports/popular-courses`  | Sí   | Cursos populares             |
| GET    | `/api/analytics/reports/top-tutors`      | `/reports/top-tutors`       | Sí   | Mejores tutores              |
| GET    | `/api/analytics/reports/system-metrics`  | `/reports/system-metrics`   | Sí   | Métricas del sistema         |
| GET    | `/api/analytics/reports/peak-hours`      | `/reports/peak-hours`       | Sí   | Horas pico de uso            |
| GET    | `/api/analytics/reports/date-range`      | `/reports/date-range`       | Sí   | Reporte por rango de fechas  |

---

## Estructura del proyecto

```
apiGateway/
├── src/
│   └── main/
│       ├── java/com/studyconnect/gateway/
│       │   ├── ApiGatewayApplication.java
│       │   ├── config/
│       │   │   └── SecurityConfig.java         ← CORS + WebFlux Security
│       │   ├── controllers/
│       │   │   └── FallbackController.java     ← Circuit Breaker fallback (503)
│       │   └── filters/
│       │       ├── AuthenticationFilter.java   ← Valida JWT en rutas protegidas
│       │       └── LoggingFilter.java          ← Log global de requests
│       └── resources/
│           ├── application.yml                 ← Configuración principal (producción)
│           └── application-dev.yml             ← Perfil local (localhost)
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── .env
├── .env.example
└── README.md
```

---

## Ejecución

### Requisitos

- Java 21
- Maven 3.8+
- Redis (para Rate Limiting) — omitir deshabilitando los filtros `RequestRateLimiter`

### Con Maven (producción)

```bash
cd apiGateway
mvn spring-boot:run
```

### Con Maven (perfil local — apunta a localhost)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Con Docker Compose

```bash
# 1. Copiar variables de entorno
cp .env.example .env
# 2. Editar JWT_SECRET y URLs si es necesario
# 3. Arrancar
docker compose up --build
```

---

## Variables de entorno

| Variable              | Default (development)                                  | Descripción                             |
|-----------------------|--------------------------------------------------------|-----------------------------------------|
| `SERVER_PORT`         | `8080`                                                 | Puerto del gateway                      |
| `JWT_SECRET`          | _mismo secret que authservice_                         | Clave HMAC-SHA256 para validar JWTs     |
| `AUTH_SERVICE_URL`    | `https://authservice-lzwi.onrender.com`                | URL del Auth Service                    |
| `USER_SERVICE_URL`    | `https://userservice-829g.onrender.com`                | URL del User Service                    |
| `COURSE_SERVICE_URL`  | `https://courseservice-0n2q.onrender.com`              | URL del Course Service                  |
| `EXAM_SERVICE_URL`    | `https://examservice-6p78.onrender.com`                | URL del Exam Service                    |
| `MATCHING_SERVICE_URL`| `https://matchingservice.onrender.com`                 | URL del Matching Service                |
| `CHAT_SERVICE_URL`    | `https://chatservice-l7qc.onrender.com`                | URL HTTP del Chat Service               |
| `CHAT_SERVICE_WS_URL` | `wss://chatservice-l7qc.onrender.com`                  | URL WebSocket del Chat Service          |
| `ANALYTICS_SERVICE_URL`| `https://backend-analytics-4xq4.onrender.com`        | URL del Analytics Service               |
| `REDIS_HOST`          | `localhost`                                            | Host de Redis                           |
| `REDIS_PORT`          | `6379`                                                 | Puerto de Redis                         |

---

## Flujo de autenticación

```
Cliente → GET /api/users/me
           │
           ▼
    API Gateway (puerto 8080)
           │
           ├─ AuthenticationFilter
           │    ├─ ¿Es ruta pública?  → sí → reenvía directo
           │    ├─ Extrae Bearer token
           │    ├─ Valida JWT (HMAC-SHA256)
           │    └─ Añade headers:
           │         X-User-Id:    <userId>
           │         X-User-Role:  <role>
           │         X-User-Email: <email>
           │
           ▼
    User Service (puerto 8082 / Render)
```

## Headers inyectados por el gateway

Los microservicios downstream reciben estos headers adicionales cuando el token es válido:

| Header          | Valor                      |
|-----------------|----------------------------|
| `X-User-Id`     | UUID del usuario           |
| `X-User-Role`   | Rol(es): student/tutor/admin |
| `X-User-Email`  | Email del usuario          |

---

## Health check

```bash
curl http://localhost:8080/actuator/health
```
