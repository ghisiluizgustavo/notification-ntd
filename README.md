# Notification System

A notification service that sends messages to users based on their subscribed categories and preferred channels.

## Tech Stack

- Java 21
- Spring Boot 4.0.1
- PostgreSQL 14
- Flyway (migrations)
- Maven
- Lombok

## Quick Start

1. **Start PostgreSQL**
   ```bash
   docker-compose up -d
   ```

2. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Send a notification**
   ```bash
   curl -X POST http://localhost:8080/api/v1/notification \
     -H "Content-Type: application/json" \
     -d '{
       "category": "SPORTS",
       "content": "Big game tonight!"
     }'
   ```

## How it works

1. You send a notification with a **category** and **content**
2. The system finds all users subscribed to that category
3. For each user, notifications are sent through ALL their registered channels
4. Each notification is saved in the database with:
   - category
   - type
   - user_id
   - status
   - created_at
   - updated_at

## Architecture

The project follows **DDD + Vertical Slice Architecture**:

```
AI Generated
src/main/java/me/ghisiluizgustavo/
├── notification/
│   ├── domain/                    # Domain entities and enums
│   ├── infrastructure/
│   │   ├── database/             # JPA entities and repositories
│   │   └── rest/                 # Controllers and error handling
│   └── feature/
│       └── notifyusers/          # Use case: notify users
│           ├── NotifyUsersHandler.java
│           └── *Strategy.java    # Channel strategies (EMAIL, SMS, PUSH)
└── user/
    ├── domain/                    # User entity
    └── infrastructure/
        └── database/             # User repository (mock)
```
