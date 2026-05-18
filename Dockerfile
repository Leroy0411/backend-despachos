# =========================================================
# STAGE 1 — BUILD
# =========================================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Cachear dependencias antes de copiar el código fuente
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# =========================================================
# STAGE 2 — RUNTIME
# Imagen final: solo JRE (sin Maven ni JDK)
# =========================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

LABEL maintainer="Innovatech Chile"
LABEL service="backend-despachos"
LABEL version="1.0"

WORKDIR /app

# Usuario no-root (mínimo privilegio)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /app/target/*.jar app.jar
RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8081

ENV SPRING_PROFILES_ACTIVE=prod \
    DB_HOST=mysql-despachos \
    DB_PORT=3306 \
    DB_NAME=despachos_db \
    DB_USER=despachos_user \
    DB_PASS=despachos_pass \
    SERVER_PORT=8081

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8081/actuator/health || exit 1

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
