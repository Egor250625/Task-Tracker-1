FROM eclipse-temurin:17-jre
WORKDIR /app
# Берём готовый JAR (убираем стадию сборки)
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]


