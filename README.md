# Backend Despachos — Innovatech Chile

API REST de gestión de despachos desarrollada con Spring Boot 3 + MySQL 8.

## Stack
- Java 21 + Spring Boot 3.2
- Spring Data JPA + MySQL 8
- SpringDoc OpenAPI (Swagger UI)
- Docker (multi-stage build)
- GitHub Actions (CI/CD)

## Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/despachos` | Listar todos los despachos |
| GET | `/api/despachos/{id}` | Obtener despacho por ID |
| POST | `/api/despachos` | Crear nuevo despacho |
| PUT | `/api/despachos/{id}` | Actualizar despacho |
| DELETE | `/api/despachos/{id}` | Eliminar despacho |
| GET | `/actuator/health` | Health check (Docker) |
| GET | `/swagger-ui.html` | Documentación interactiva |

## Levantar localmente con Docker

```bash
# Clonar el repositorio
git clone https://github.com/TU-USUARIO/backend-despachos.git
cd backend-despachos

# Levantar con docker-compose (desde la raíz del proyecto)
docker compose up -d mysql-despachos backend-despachos

# Verificar que está corriendo
curl http://localhost:8081/actuator/health
```

## Variables de entorno

| Variable | Descripción | Valor por defecto |
|---|---|---|
| `DB_HOST` | Host de MySQL | `mysql-despachos` |
| `DB_PORT` | Puerto MySQL | `3306` |
| `DB_NAME` | Nombre de la BD | `despachos_db` |
| `DB_USER` | Usuario MySQL | `despachos_user` |
| `DB_PASS` | Contraseña MySQL | `despachos_pass` |
| `SERVER_PORT` | Puerto del servidor | `8081` |

## CI/CD Pipeline

El pipeline se activa con `push` a la rama `deploy`:

```
push → deploy
  ├── Build imagen Docker (multi-stage)
  ├── Push a Docker Hub
  └── SSH a EC2 → docker pull + docker compose up
```

### Secrets requeridos en GitHub

| Secret | Descripción |
|---|---|
| `DOCKERHUB_USERNAME` | Usuario Docker Hub |
| `DOCKERHUB_TOKEN` | Token de acceso Docker Hub |
| `EC2_HOST_BACKEND` | IP de la instancia EC2 backend |
| `EC2_USER` | Usuario SSH (ec2-user / ubuntu) |
| `EC2_SSH_KEY` | Clave privada PEM |

## Dockerfile — Decisiones técnicas

- **Multi-stage build**: etapa `builder` con Maven genera el JAR; etapa `runtime` usa solo JRE Alpine (~200MB vs ~600MB)
- **Usuario no-root**: `appuser:appgroup` por seguridad (mínimo privilegio)
- **HEALTHCHECK**: permite a Docker y EC2 detectar si el servicio está listo
- **`-XX:+UseContainerSupport`**: la JVM detecta correctamente los límites de memoria del contenedor

---
*ISY1101 — Introducción a Herramientas DevOps | DuocUC 2025*
