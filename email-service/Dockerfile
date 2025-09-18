# --- Stage 1: build JAR ---
FROM gradle:8.9-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# --- Stage 2: run with JRE ---
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]


