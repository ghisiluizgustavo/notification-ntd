# Tech Stack

This document describes the technologies used in the project and the policies around dependency management.

## Core Technologies

### Java 21
- **Version**: Java 21 (LTS)
- **Why**: Latest LTS version with modern features
- **Key features used**:
  - Records for DTOs and domain entities
  - Pattern matching
  - Text blocks
  - Stream API

### Spring Boot 4.0.1
- **Version**: 4.0.1
- **Modules used**:
  - `spring-boot-starter-data-jpa` - Database access
  - `spring-boot-starter-webmvc` - REST API
  - `spring-boot-starter-flyway` - Database migrations

**Configuration**: See `application.yml`

### PostgreSQL 14
- **Version**: 14 (via Docker)
- **Why**: Reliable, mature, well-supported by Flyway
- **Database**: `notification`
- **Schema management**: Flyway migrations in `src/main/resources/db/migration/`

### Flyway
- **Purpose**: Database version control
- **Migrations**: Located in `src/main/resources/db/migration/`
- **Naming**: `V{number}__{description}.sql`
- **Auto-run**: Executes on application startup

### Maven
- **Build tool**: Maven 3.9.12 (via wrapper)
- **Java version**: Configured to target Java 21
- **Commands**:
  - `./mvnw clean install` - Build
  - `./mvnw spring-boot:run` - Run
  - `./mvnw test` - Run tests

### Lombok 1.18.42
- **Purpose**: Reduce boilerplate code
- **Annotations used**:
  - `@Data` - Getters, setters, toString, equals, hashCode
  - `@Getter` - Only getters
  - `@RequiredArgsConstructor` - Constructor injection
  - `@Slf4j` - Logger
  - `@AllArgsConstructor` / `@NoArgsConstructor` - Constructors

## Runtime Environment

### Docker Compose
Used for PostgreSQL database.

**File**: `docker-compose.yml`

```yaml
services:
  postgres:
    image: postgres:14
    ports:
      - "5432:5432"
    environment:
      POSTGRES_DB: notification
      POSTGRES_USER: user
      POSTGRES_PASSWORD: pass
```

**Commands**:
- Start: `docker-compose up -d`
- Stop: `docker-compose down`
- Reset: `docker-compose down -v` (deletes data)

## Dependencies Policy

### IMPORTANT: Dependency Management Rules

⚠️ **DO NOT** add, remove, or update dependencies without explicit approval.

⚠️ **DO NOT** modify core configurations without explicit approval.

### What requires approval:
1. Adding new Maven dependencies
2. Updating existing dependency versions
3. Changing Java version
4. Modifying Spring Boot version
5. Changing database version or type
6. Adding new Spring Boot starters
7. Modifying `pom.xml` build configuration
8. Changing `application.yml` core settings

### What does NOT require approval:
- Adding new Java classes
- Creating new packages
- Modifying business logic
- Adding tests
- Updating documentation
- Adding SQL migrations (Flyway)

### Rationale
- Maintains project stability
- Prevents version conflicts
- Ensures compatibility
- Allows security audit of dependencies
- Keeps build reproducible

### Process for Adding Dependencies
1. Document the need and justification
2. Research alternatives using existing dependencies
3. If absolutely necessary, notify project lead
4. Wait for approval before proceeding
5. Document the decision in commit message

## Current Dependencies (pom.xml)

### Production Dependencies
```xml
<!-- Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-flyway</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>

<!-- Database -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-database-postgresql</artifactId>
</dependency>

<!-- Utilities -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.42</version>
</dependency>
```

### Test Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa-test</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-flyway-test</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc-test</artifactId>
    <scope>test</scope>
</dependency>
```

## Configuration Files

### application.yml
Main Spring Boot configuration:
- Database connection (PostgreSQL)
- JPA/Hibernate settings
- Flyway configuration
- Logging levels

**Location**: `src/main/resources/application.yml`

### docker-compose.yml
PostgreSQL container configuration.

**Location**: `docker-compose.yml` (project root)

## Development Tools

### IDE Support
- **IntelliJ IDEA**: Lombok plugin required
- **VS Code**: Spring Boot Extension Pack recommended
- **Eclipse**: Install Lombok via installer

### Required Plugins
- Lombok annotation processor
- Spring Boot support
- Maven support

## Version Compatibility

| Component      | Version  | Compatible With           |
|----------------|----------|---------------------------|
| Java           | 21       | Spring Boot 4.0.1         |
| Spring Boot    | 4.0.1    | Java 21, PostgreSQL 14    |
| PostgreSQL     | 14       | Flyway (auto-detected)    |
| Flyway         | (Spring) | PostgreSQL 14             |
| Lombok         | 1.18.42  | Java 21                   |

## Known Limitations

1. **Java 25 not supported** - Lombok compatibility issues
2. **PostgreSQL 15+ not recommended** - Flyway version in Spring Boot 4.0.1 has issues
3. **Lombok requires IDE plugin** - Must be installed for development

## Environment Variables

None required. All configuration is in `application.yml` with default values.

For production, consider externalizing:
- Database credentials
- Database URL
- Log levels

## Build Output

- **Compiled classes**: `target/classes/`
- **JAR file**: `target/notification-0.0.1-SNAPSHOT.jar`
- **Test results**: `target/surefire-reports/`

## Ports

- **Application**: 8080
- **PostgreSQL**: 5432

Make sure these ports are available before starting the services.
