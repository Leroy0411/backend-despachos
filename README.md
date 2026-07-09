# Backend Despachos — Innovatech Chile

Sistema de gestión logística de despachos. API REST construida con Spring Boot 3 + Java 21, desplegada en AWS ECS Fargate.

## 🌐 URL Pública

```
http://despachos-alb-1656417120.us-east-1.elb.amazonaws.com/api/despachos
http://despachos-alb-1656417120.us-east-1.elb.amazonaws.com/swagger-ui.html
http://despachos-alb-1656417120.us-east-1.elb.amazonaws.com/actuator/health
```

## 🏗️ Arquitectura

```
Internet → ALB (puerto 80) → ECS Fargate: backend-despachos (puerto 8081) → RDS MySQL 8.0
```

| Componente | Tecnología | Servicio AWS |
|---|---|---|
| API REST | Spring Boot 3 + Java 21 | ECS Fargate |
| Base de datos | MySQL 8.0 | Amazon RDS |
| Imágenes | Docker multi-stage | Amazon ECR |
| CI/CD | GitHub Actions | ECR + ECS update-service |
| Logs | awslogs driver | CloudWatch: /ecs/backend-despachos |

## 📦 Estructura del Proyecto

```
backend-despachos/
├── src/
│   └── main/java/com/citt/
│       ├── SpringbootApiRestDespachoApplication.java
│       ├── controller/DespachoController.java
│       ├── persistence/
│       │   ├── entity/Despacho.java
│       │   ├── repository/DespachoRepository.java
│       │   └── services/DespachoServiceImpl.java
│       ├── exceptions/
│       │   ├── DespachoNotFoundException.java
│       │   └── RestResponseEntityExceptionHandler.java
│       └── config/
│           ├── CorsConfig.java
│           └── OpenApiConfig.java
├── Dockerfile
├── docker-compose.yml
├── .github/
│   └── workflows/
│       └── deploy.yml
└── README.md
```

## 🚀 Endpoints API REST

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /api/despachos | Listar todos los despachos |
| GET | /api/despachos/{id} | Obtener despacho por ID |
| POST | /api/despachos | Crear nuevo despacho |
| PUT | /api/despachos/{id} | Actualizar despacho |
| DELETE | /api/despachos/{id} | Eliminar despacho |
| GET | /actuator/health | Health check del servicio |
| GET | /swagger-ui.html | Documentación interactiva |

### Campos del modelo Despacho

```json
{
  "id": 1,
  "numeroDespacho": "DSP-001",
  "estado": "Pendiente",
  "direccionDestino": "Av. Libertador 123, Santiago",
  "observaciones": "Frágil",
  "fechaDespacho": "2025-01-15",
  "fechaCierre": null
}
```

Estados válidos: `Pendiente`, `En tránsito`, `Entregado`, `Cancelado`

## 🐳 Docker

### Build local

```bash
docker build -t backend-despachos .
```

### Variables de entorno requeridas

| Variable | Descripción | Ejemplo |
|---|---|---|
| DB_HOST | Host de la base de datos | mysql-despachos |
| DB_PORT | Puerto MySQL | 3306 |
| DB_NAME | Nombre de la base de datos | despachos_db |
| DB_USER | Usuario de la base de datos | despachos_user |
| DB_PASS | Contraseña de la base de datos | (secret) |
| SERVER_PORT | Puerto del servidor | 8081 |

### Levantar entorno local completo

```bash
# Clonar repositorios
git clone https://github.com/lerotype04/backend-despachos
git clone https://github.com/lerotype04/frontend-despacho

# Crear archivo .env en backend-despachos/
cat > .env << 'ENVEOF'
DOCKERHUB_USERNAME=lerotype04
DB_DESPACHOS_PASS=despachos_pass_segura
MYSQL_ROOT_PASSWORD=rootpass123
ENVEOF

# Levantar todos los servicios
docker compose up -d

# Verificar
curl http://localhost:8081/actuator/health
curl http://localhost:8081/api/despachos
```

## ⚙️ Pipeline CI/CD

El pipeline se dispara automáticamente con cada `push` a la rama `deploy`:

```
push a rama deploy
    → Checkout código
    → Configurar credenciales AWS (IAM)
    → Login en Amazon ECR
    → Docker build multi-stage
    → Push a ECR (:latest + :sha-{commit})
    → aws ecs update-service (rolling update sin downtime)
```

### Secrets requeridos en GitHub

| Secret | Descripción |
|---|---|
| AWS_ACCESS_KEY_ID | Credencial IAM AWS |
| AWS_SECRET_ACCESS_KEY | Clave secreta IAM |
| AWS_SESSION_TOKEN | Token de sesión (AWS Academy) |

## ☁️ Infraestructura AWS

| Recurso | ID / Valor |
|---|---|
| Región | us-east-1 |
| Clúster ECS | despachos-cluster (Fargate) |
| Task Definition | backend-despachos:1 |
| ECR Repository | 617217798110.dkr.ecr.us-east-1.amazonaws.com/backend-despachos |
| RDS Endpoint | despachos-mysql.cjua67nnkwk5.us-east-1.rds.amazonaws.com |
| Security Group | sg-0c70718056d34c0e2 (TCP 8081 solo desde ALB) |
| Log Group | /ecs/backend-despachos (CloudWatch) |
| IAM Role | arn:aws:iam::617217798110:role/LabRole |

## 📊 Autoscaling

- Tipo: Target Tracking (ECSServiceAverageCPUUtilization)
- Umbral: 50% CPU
- Mínimo: 1 task — Máximo: 3 tasks
- Cooldown scale-out: 60s — Cooldown scale-in: 300s

## 🔒 Seguridad

- Imagen base: `eclipse-temurin:21-jre-alpine` (minimalista)
- Usuario no-root: `appuser:appgroup`
- Puerto único expuesto: 8081
- Credenciales: solo via variables de entorno (nunca en código)
- Security Group: puerto 8081 accesible únicamente desde el ALB

## 📝 Ramas

| Rama | Propósito |
|---|---|
| main | Código estable |
| deploy | Trigger del pipeline CI/CD |

## 🛠️ Stack Tecnológico

- Java 21 + Spring Boot 3
- Spring Data JPA + Hibernate
- MySQL 8.0
- Maven 3.9
- Docker (multi-stage, Alpine)
- GitHub Actions
- Amazon ECS Fargate + ECR + RDS + ALB + CloudWatch
