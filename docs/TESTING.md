# Testing Strategy

This project follows the **Test Pyramid** (Martin Fowler) to ensure code quality and maintainability.

## Test Pyramid Overview

```
        /\
       /  \      E2E Tests (Few)
      /----\     - Full system tests
     /      \    - API to database
    /--------\   
   /          \  Integration Tests (Some)
  /------------\ - Database interaction
 /              \- External services
/----------------\
|                | Unit Tests (Many)
|  Fast & Cheap  | - Domain logic
|                | - Business rules
\________________/
```

### Principles
1. **Many unit tests** - Fast, isolated, test business logic
2. **Some integration tests** - Test component interaction
3. **Few E2E tests** - Test critical user journeys
4. **Fast feedback** - Tests should run quickly
5. **Independent** - Tests should not depend on each other

## Test Levels

### 1. Unit Tests (Base of Pyramid)

**Purpose**: Test individual components in isolation

**What to test**:
- Domain entities and business rules
- Strategy implementations
- Validation logic
- Utility methods

**Characteristics**:
- No database
- No HTTP calls
- No external dependencies
- Fast execution (milliseconds)
- Use mocks/stubs for dependencies

**Examples**:

```java
// Domain validation
@Test
void shouldThrowException_whenCategoryIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
        Notification.create(null, NotificationType.EMAIL, "content");
    });
}

// Strategy logic
@Test
void emailStrategy_shouldSupportEmailType() {
    EmailNotificationStrategy strategy = new EmailNotificationStrategy();
    assertTrue(strategy.supports(NotificationType.EMAIL));
    assertFalse(strategy.supports(NotificationType.SMS));
}

// Business rules
@Test
void notification_shouldHavePendingStatus_whenCreated() {
    Notification notification = Notification.create(
        NotificationCategory.SPORTS, 
        NotificationType.EMAIL, 
        "test"
    );
    assertEquals(NotificationStatus.PENDING, notification.getStatus());
}
```

**Coverage target**: 80%+ for domain logic

### 2. Integration Tests (Middle of Pyramid)

**Purpose**: Test components working together

**What to test**:
- Repository operations (database)
- JPA entity mappings
- Flyway migrations
- Spring context loading
- Use case handlers with real dependencies

**Characteristics**:
- Uses test database (H2 or test container)
- Spring Boot test context
- Real database queries
- Slower than unit tests (seconds)

**Examples**:

```java
@SpringBootTest
@AutoConfigureTestDatabase
class NotificationRepositoryTest {
    
    @Autowired
    private NotificationRepository repository;
    
    @Test
    void shouldSaveAndRetrieveNotification() {
        // Given
        NotificationEntityJpa entity = new NotificationEntityJpa(...);
        
        // When
        NotificationEntityJpa saved = repository.save(entity);
        
        // Then
        assertNotNull(saved.getId());
        Optional<NotificationEntityJpa> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
    }
    
    @Test
    void shouldFindNotificationsByCategory() {
        // Test query methods
    }
}
```

**Coverage target**: All repositories and database operations

### 3. E2E Tests (Top of Pyramid)

**Purpose**: Test complete user workflows

**What to test**:
- Full API request → response flow
- Database persistence
- Error handling
- Business scenarios end-to-end

**Characteristics**:
- Full application context
- Real HTTP requests
- Real database (or test container)
- Slowest tests (seconds to minutes)
- Test critical paths only

**Examples**:

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class NotificationE2ETest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private NotificationRepository repository;
    
    @Test
    void shouldCreateNotification_andSaveToDatabase() {
        // Given
        NotificationRequest request = new NotificationRequest(
            NotificationCategory.SPORTS,
            "Test content"
        );
        
        // When
        ResponseEntity<Void> response = restTemplate.postForEntity(
            "/api/v1/notification", 
            request, 
            Void.class
        );
        
        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        
        List<NotificationEntityJpa> notifications = repository.findAll();
        assertFalse(notifications.isEmpty());
        assertEquals("Test content", notifications.get(0).getContent());
    }
    
    @Test
    void shouldReturn400_whenCategoryIsInvalid() {
        // Test error scenarios
    }
}
```

**Coverage target**: Critical user journeys (3-5 main scenarios)

## Test Organization

### Directory Structure
```
src/test/java/
├── me/ghisiluizgustavo/
│   ├── notification/
│   │   ├── domain/
│   │   │   └── NotificationTest.java              # Unit
│   │   │
│   │   ├── feature/
│   │   │   └── notifyusers/
│   │   │       ├── NotifyUsersHandlerTest.java    # Unit (with mocks)
│   │   │       └── NotifyUsersHandlerIT.java      # Integration
│   │   │
│   │   └── infrastructure/
│   │       ├── database/
│   │       │   └── NotificationRepositoryIT.java  # Integration
│   │       │
│   │       └── rest/
│   │           └── NotificationControllerE2E.java # E2E
│   │
│   └── user/
│       └── domain/
│           └── UserTest.java                      # Unit
```

### Naming Conventions
- **Unit tests**: `*Test.java`
- **Integration tests**: `*IT.java`
- **E2E tests**: `*E2E.java`

## Test Data Strategy

### Unit Tests
- Use builders or factory methods
- Hardcode test data in tests
- Keep data simple and focused

```java
Notification notification = Notification.create(
    NotificationCategory.SPORTS,
    NotificationType.EMAIL,
    "test content"
);
```

### Integration Tests
- Use `@Sql` annotations to load test data
- Create SQL scripts in `src/test/resources/data/`
- Clean up after tests

```java
@Sql("/data/insert-test-notifications.sql")
@Test
void testQuery() {
    // Test with pre-loaded data
}
```

### E2E Tests
- Use fixtures or test data factories
- Leverage Flyway migrations for schema
- Consider using test containers for isolation

## Mocking Strategy

### When to Mock
- External services
- Slow operations
- Complex dependencies in unit tests

### When NOT to Mock
- Domain entities (use real objects)
- Simple value objects
- Repository in integration tests (use real database)

### Tools
- **Mockito**: For mocking dependencies
- **@MockBean**: For Spring context mocking

```java
@ExtendWith(MockitoExtension.class)
class NotifyUsersHandlerTest {
    
    @Mock
    private NotificationRepository repository;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private NotifyUsersHandler handler;
    
    @Test
    void shouldNotifySubscribedUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(testUsers);
        
        // Act
        handler.handle(request);
        
        // Assert
        verify(repository, times(3)).save(any());
    }
}
```

## Test Execution

### Run All Tests
```bash
./mvnw test
```

### Run Specific Test Class
```bash
./mvnw test -Dtest=NotificationTest
```

### Run Integration Tests Only
```bash
./mvnw test -Dtest=*IT
```

### Run E2E Tests Only
```bash
./mvnw test -Dtest=*E2E
```

### With Coverage Report
```bash
./mvnw test jacoco:report
# Report at: target/site/jacoco/index.html
```

## Coverage Goals

| Layer                | Target Coverage |
|----------------------|-----------------|
| Domain entities      | 90%+            |
| Use case handlers    | 80%+            |
| Strategies           | 80%+            |
| Controllers          | 70%+            |
| Repositories         | 100% (via IT)   |

**Overall target**: 80% code coverage

## Testing Best Practices

### 1. AAA Pattern
Always structure tests with Arrange-Act-Assert:

```java
@Test
void testExample() {
    // Arrange (Given)
    Notification notification = createTestNotification();
    
    // Act (When)
    notification.updateStatus(NotificationStatus.SENT);
    
    // Assert (Then)
    assertEquals(NotificationStatus.SENT, notification.getStatus());
}
```

### 2. Test Naming
Use descriptive names that explain the scenario:

```java
// Good
shouldThrowException_whenContentIsNull()
shouldSaveNotification_withAllFields()
shouldReturn400_whenCategoryIsInvalid()

// Bad
testNotification()
test1()
testCreate()
```

### 3. One Assertion Per Test (Guideline)
Focus each test on one behavior:

```java
// Good - focused
@Test
void shouldSetStatusToPending_whenCreated() {
    Notification n = Notification.create(...);
    assertEquals(NotificationStatus.PENDING, n.getStatus());
}

// Acceptable - related assertions
@Test
void shouldInitializeTimestamps_whenCreated() {
    Notification n = Notification.create(...);
    assertNotNull(n.getCreatedAt());
    assertNotNull(n.getUpdatedAt());
}
```

### 4. Test Independence
Tests should not depend on execution order:

```java
// Bad - depends on other test
@Test
void testSave() {
    repository.save(notification);
}

@Test
void testFind() {
    // Assumes testSave() ran first
    Notification found = repository.findById(1);
}

// Good - independent
@Test
void testFind() {
    repository.save(createTestNotification());
    Notification found = repository.findById(1);
}
```

### 5. Avoid Test Logic
Keep tests simple and linear:

```java
// Bad - has logic
@Test
void testSomething() {
    for (int i = 0; i < 10; i++) {
        if (i % 2 == 0) {
            // test even
        } else {
            // test odd
        }
    }
}

// Good - explicit
@Test
void testEvenNumbers() { ... }

@Test
void testOddNumbers() { ... }
```

## Continuous Integration

### Pre-commit
Run tests locally before committing:
```bash
./mvnw clean test
```

### CI Pipeline (when implemented)
1. Checkout code
2. Build project
3. Run unit tests
4. Run integration tests
5. Run E2E tests
6. Generate coverage report
7. Fail if coverage < 80%

## Future Improvements

1. Add mutation testing (PIT)
2. Implement contract testing for API
3. Add performance tests for high-load scenarios
4. Integrate test coverage into CI/CD
5. Add arquillian tests for complex scenarios

## Resources

- [Test Pyramid - Martin Fowler](https://martinfowler.com/articles/practical-test-pyramid.html)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
