# Runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

ARG JAR_NAME=app
COPY build/libs/${JAR_NAME}-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

