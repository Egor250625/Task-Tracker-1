# 📋 Task Tracker API

> Многопользовательский планировщик задач в стиле Trello с микросервисной архитектурой

## 🚀 Обзор проекта

Task Tracker API — современное решение для управления задачами, вдохновленное функциональностью Trello. Проект реализован с использованием микросервисной архитектуры и предоставляет REST API для управления задачами, пользователями и уведомлениями.

## 🛠️ Технологический стек

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-green?style=flat-square&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue?style=flat-square&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-blue?style=flat-square&logo=docker)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-black?style=flat-square&logo=apache-kafka)
![Gmail](https://img.shields.io/badge/Gmail-red?style=flat-square&logo=gmail)

### Основные технологии:
- **Java 17**
- **Gradle**
- **Spring Framework:**
  - Spring Boot
  - Spring Web
  - Spring Security (JWT)
  - Spring Data JPA
  - Spring for Apache Kafka
  - Spring Scheduler
  - Spring Mail (Gmail)
- **PostgreSQL**
- **Liquibase**
- **Apache Kafka**
- **Docker**
- **CI/CD (GitHub Actions)**

---

## 🏗️ Архитектура приложения

Проект построен на основе **микросервисной архитектуры** и включает четыре основных микросервиса:

### 🔧 API Service
Основной сервис для работы с бизнес-логикой:
- REST API для управления пользователями и задачами
- Аутентификация и авторизация через JWT
- Интеграция с Kafka для отправки уведомлений

### 📧 Email Service
Сервис уведомлений для отправки email:
- Подписка на Kafka топик `EMAIL_SENDING_TASKS`
- Отправка email через Gmail SMTP

### ⏰ Scheduler Service
Сервис планировщика:
- Автоматическая генерация ежедневных отчетов о задачах
- Анализ изменений за сутки для каждого пользователя
- Отправка сводок через Kafka в Email Service

### 💻 Frontend Service
Клиентский фронтенд:
- Панель администратора
- Веб-интерфейс для пользователей
- Интеграция с API Service

```mermaid
graph TD
    subgraph Frontend
        A[Frontend Service]
    end

    subgraph Backend
        B[API Service]
        C[Scheduler Service]
        D[Email Service]
    end

    subgraph DB
        E[PostgreSQL]
    end

    subgraph Messaging
        F[Kafka Broker]
    end

    %% REST взаимодействие
    A -->|REST API| B

    %% API сервис как продюсер Kafka
    B -->|Kafka Producer| F

    %% Kafka консумеры
    C -->|Kafka Consumer| F
    D -->|Kafka Consumer| F

    %% Scheduler как продюсер
    C -->|Kafka Producer| F

    %% JDBC взаимодействие с БД
    B -->|JDBC| E
    C -->|JDBC| E

    %% Email сервис через Gmail SMTP
    D -->|SMTP| G[Gmail]
```

---

## 🔄 CI/CD

Проект использует **GitHub Actions** для автоматизации процессов сборки и деплоя:  
- Автоматическая сборка Docker образов микросервисов  
- Публикация образов в Docker Hub  
- Автоматический деплой на удалённый сервер при пуше в основную ветку  

---

## 📦 Структура проекта

task-tracker/

├── api-service/ - Основной REST API сервис

├── frontend-service/ # Веб-клиент

├── scheduler-service/ # Сервис планировщика

├── email-service/ # Сервис рассылки Gmail

├── docker-compose.yaml # Конфигурация Docker контейнеров

└── README.md


## 🚀 Запуск проекта

Для локального запуска проекта достаточно:  

1. Создать копию файла `.env.example` и при необходимости заполнить переменные окружения:  
```bash
cp .env.example .env
```
   Файл .env.example содержит все необходимые настройки для локального запуска.
   
2. Собрать Docker-образы микросервисов и запустить проект:
```bash
docker-compose -f compose.yaml up --build
```

После этого все сервисы будут работать локально, включая API, фронтенд, Scheduler и Email.

## 👨‍💻 Автор

- Telegram: [@grdvt](https://t.me/@grdvt) 
- GitHub: [@Egor250625](https://github.com/Egor250625)
