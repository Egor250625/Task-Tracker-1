# 📝 Task Tracker

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-blue?style=for-the-badge&logo=springboot)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-✓-black?style=for-the-badge&logo=apachekafka)
![Docker](https://img.shields.io/badge/Docker-✓-blue?style=for-the-badge&logo=docker)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-✓-blue?style=for-the-badge&logo=postgresql)

Модульная система управления задачами, построенная на микросервисной архитектуре с использованием современных технологий Java, Spring Boot, Kafka и Docker.

## ✨ Особенности

### 🚀 Основной функционал
- 🔐 **JWT-аутентификация** — безопасная авторизация и работа без состояния
- 📝 **CRUD для задач** — полный цикл управления задачами
- 📡 **Асинхронные уведомления** — обработка событий через Kafka
- ⏰ **Планировщик задач** — автоматическая отправка напоминаний
- 📧 **Email-сервис** — отдельный сервис для отправки писем
- 🔗 **REST API** — аккуратные DTO, валидация, обработка ошибок
- 🐳 **Контейнеризация** — полная работа через Docker Compose

## 🛠️ Технологии

### 🔧 Backend
![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-6DB33F?logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-✓-6DB33F?logo=springsecurity)
![JWT](https://img.shields.io/badge/JWT-✓-000000?logo=jsonwebtokens)
![Kafka](https://img.shields.io/badge/Kafka-✓-231F20?logo=apachekafka)

### 🗄️ Database
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-✓-4169E1?logo=postgresql)
![Hibernate](https://img.shields.io/badge/Hibernate-✓-59666C?logo=hibernate)

### 📦 DevOps
![Docker](https://img.shields.io/badge/Docker-✓-2496ED?logo=docker)
![Gradle](https://img.shields.io/badge/Gradle-✓-02303A?logo=gradle)

## 🏗️ Архитектура

| Микросервис | Назначение | Технологии |
|-------------|------------|------------|
| 🚀 **api-service** | Основной сервис (аутентификация, CRUD) | Spring Boot, Security, JPA, Kafka |
| ⏰ **scheduler-service** | Планировщик уведомлений | Spring Scheduler, Kafka |
| 📧 **email-service** | Отправка email-уведомлений | Spring Mail, Kafka |

``` mermaid
graph TD
    A[Клиент] --> B[task-tracker-api]
    B --> C[PostgreSQL]
    B --> D[Kafka]
    D --> E[scheduler-service]
    D --> F[email-service]
    E --> D
    E --> C
    F --> G[SMTP Server]
```

## 🚀 Быстрый старт

### 📋 Предварительные требования
- ![Java](https://img.shields.io/badge/Java-17+-orange?logo=openjdk)
- ![Docker](https://img.shields.io/badge/Docker-✓-blue?logo=docker)
- ![Docker Compose](https://img.shields.io/badge/Docker%20Compose-✓-blue?logo=docker)

### ⚙️ Настройка окружения

1. **Клонируйте репозиторий**
   ```bash
   git clone https://github.com/your-username/task-tracker-1.git
   cd task-tracker
   ```
2.**Создайте файлы конфигурации**

В каждом модуле создайте application-secret.yml:
   ```
secrets:
  postgres:
    username: your_postgres_user
    password: your_postgres_password
  jwt:
    secret: your_jwt_secret_key
  kafka:
    bootstrap-servers: kafka:9092
    topics:
      email-sending: EMAIL_SENDING_TASKS
      user-info: USER_INFO_TOPIC
      task-notification: TASK_NOTIFICATION_TOPIC
   ```
Создайте Dockerfile в каждом модуле

   ```
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
   ```
4. **Запустите проект**

   ```
   docker-compose up --build
   ```

## 🚀 CI/CD Pipeline

### 🔄 GitHub Actions + Docker Hub

![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)
![Docker Hub](https://img.shields.io/badge/Docker_Hub-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![CI/CD](https://img.shields.io/badge/CI/CD-✓-success?style=for-the-badge)

Настроена автоматическая система непрерывной интеграции и доставки:

#### 📋 Workflow этапы:

``` mermaid
graph LR
    A[🚀 Push кода] --> B[📦 Сборка образов]
    B --> C[🧪 Тестирование]
    C --> D[🐳 Push в Docker Hub]
    D --> E[🚀 Деплой на сервер]
```

Настроена автоматическая система непрерывной интеграции и доставки:

# Telegram 

**@grdvt**
