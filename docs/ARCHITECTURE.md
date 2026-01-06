# Architecture

This project follows **Vertical Slice Architecture** combined with **Domain-Driven Design (DDD)** principles.

## Core Principles

### Vertical Slice Architecture
- Features are organized by business capability, not by technical layer
- Each feature contains all layers needed (domain, infrastructure, use case)
- Reduces coupling between features
- Makes it easier to understand and modify specific features

### Domain-Driven Design
- Domain entities are isolated from infrastructure concerns
- Clear separation between domain logic and technical implementation
- Rich domain models with business rules encapsulated

## Project Structure

```
src/main/java/me/ghisiluizgustavo/
├── notification/
│   ├── domain/                           # Domain Layer
│   │   ├── Notification.java            # Aggregate root
│   │   ├── NotificationCategory.java    # Value object (enum)
│   │   ├── NotificationStatus.java      # Value object (enum)
│   │   └── NotificationType.java        # Value object (enum)
│   │
│   ├── infrastructure/                   # Infrastructure Layer
│   │   ├── database/
│   │   │   ├── NotificationEntityJpa.java    # JPA entity
│   │   │   └── NotificationRepository.java   # Spring Data repository
│   │   │
│   │   └── rest/
│   │       ├── NotificationController.java   # REST endpoint
│   │       ├── ErrorResponse.java            # Error DTO
│   │       └── GlobalExceptionHandler.java   # Exception handling
│   │
│   └── feature/
│       └── notifyusers/                  # Feature: Notify Users
│           ├── NotifyUsersHandler.java       # Use case handler
│           ├── NotificationStrategy.java     # Strategy interface
│           ├── EmailNotificationStrategy.java
│           ├── SmsNotificationStrategy.java
│           └── PushNotificationStrategy.java
│
└── user/
    ├── domain/
    │   └── User.java                     # User record (domain entity)
    │
    └── infrastructure/
        └── database/
            └── UserRepository.java       # Mock repository
```

## Layer Responsibilities

### Domain Layer
- Contains pure business logic
- No dependencies on frameworks or infrastructure
- Entities, value objects, and business rules
- Validation at the domain level

**Example**: `Notification.java`
```java
public class Notification {
    // Business rules:
    // - Category cannot be null
    // - Type cannot be null
    // - Content cannot be null
    // - User ID must be positive
}
```

### Infrastructure Layer
- Implements technical concerns (database, REST, etc.)
- Adapts domain entities to/from external systems
- Spring Boot configurations
- JPA mappings

**Database**: Maps domain `Notification` to `NotificationEntityJpa`
**REST**: Exposes HTTP endpoints and error handling

### Feature Layer (Use Cases)
- Contains application logic
- Orchestrates domain objects and infrastructure
- Implements business workflows

**Example**: `NotifyUsersHandler`
1. Receives notification request
2. Finds subscribed users
3. Sends to all user channels
4. Persists notifications

## Design Patterns

### Strategy Pattern
Used for notification channels (EMAIL, SMS, PUSH):
```java
interface NotificationStrategy {
    boolean supports(NotificationType type);
    void send(User user, Notification notification);
}
```

Each channel has its own implementation, making it easy to:
- Add new channels without modifying existing code
- Test channels independently
- Change channel logic without affecting others

### Repository Pattern
Abstracts data access:
- `NotificationRepository` - Spring Data JPA for notifications
- `UserRepository` - In-memory mock for users

### Factory Pattern
Used in domain entities:
```java
Notification.create(category, type, content)
```

## Key Design Decisions

### 1. No Type in Request
The API request only receives `category` and `content`. The `type` is determined by the user's channels.

**Reasoning**: 
- Users don't choose how they receive notifications
- The system sends to ALL channels the user has registered
- Simplifies the API contract

### 2. One Record per Channel
Each user+channel combination creates a separate database record.

**Example**: Carol has EMAIL, SMS, PUSH → 3 records in the database

**Reasoning**:
- Enables tracking per channel
- Allows retry logic per channel in the future
- Provides detailed audit trail

### 3. Mock User Repository
Users are hardcoded in `UserRepository` instead of using a database.

**Reasoning**:
- Requirement specified no user administration
- Simplifies setup and testing
- Easy to modify for demo purposes

### 4. Strategy Selection at Runtime
Strategies are injected as a `List<NotificationStrategy>` and filtered at runtime.

**Reasoning**:
- Leverages Spring's dependency injection
- No manual strategy registration needed
- Easy to add/remove strategies

## Data Flow

```
1. POST /api/v1/notification
   ↓
2. NotificationController (validates JSON)
   ↓
3. NotifyUsersHandler.handle()
   ↓
4. Find subscribed users (UserRepository)
   ↓
5. For each user:
   ↓
6. For each channel:
   ↓
7. Find matching strategy (EMAIL/SMS/PUSH)
   ↓
8. Send notification (strategy.send())
   ↓
9. Create Notification domain object
   ↓
10. Save to database (NotificationRepository)
```

## Extension Points

### Adding a New Channel
1. Create enum value in `NotificationType`
2. Create new strategy class implementing `NotificationStrategy`
3. Add `@Component` annotation
4. Done - Spring will auto-register it

### Adding a New Category
1. Add enum value to `NotificationCategory`
2. Add users subscribed to it in `UserRepository`
3. Done - no other changes needed

### Adding Real Message Sending
1. Inject external service in strategy class
2. Implement actual sending logic in `send()` method
3. Add error handling and retries as needed

## Testing Strategy

Follow the test pyramid (Martim Fowler):
- **Unit tests**: Domain logic, strategies
- **Integration tests**: Repository, database
- **E2E tests**: Full API workflow

See `docs/TESTING.md` for details.

## Rules and Constraints

1. Domain layer must NOT depend on infrastructure
2. Each feature should be self-contained
3. Use interfaces for extension points
4. Keep strategies stateless
5. Validate at domain boundaries
