# Mentoring Modulith - Domain-Driven Design with Hexagonal Architecture

## Project Overview

This project demonstrates **Domain-Driven Design (DDD)** principles implemented through **Hexagonal Architecture** (Ports and Adapters pattern). The system manages course announcements and employee enrollments, built as a modular monolith using Spring Boot.

**Reference Repository**: Based on training materials from [Training360's DDD Training Repository](https://github.com/Training360/javax-ddd-training-2025-09-29).

### Current Architecture Status

The system currently implements:
- ✅ **Course Management**: Full CRUD operations for courses
- ✅ **Employee Management**: Basic employee domain with existence validation
- ✅ **Enrollment System**: Complete enrollment workflow with business rules
- ✅ **CQRS Pattern**: Separate command and query operations
- ✅ **Cross-Module Communication**: Courses module validates employees via gateway pattern

## Ubiquitous Language

### Core Domain Concepts

#### Course Management
- **Course** - A training program with enrollment capacity and business rules
- **Course Code** - Unique identifier (minimum 2 characters, validated)
- **Course Title** - Descriptive name (required, non-blank)
- **Course Limit** - Maximum number of enrollments allowed
- **Announce** - Factory method for creating new courses (`Course.announce()`)

#### Enrollment Domain
- **Enrollment** - Registration of an employee in a specific course
- **Employee ID** - Unique identifier for employees (cross-module reference)
- **Enrollment Date** - Timestamp when enrollment occurred
- **Enrollment VO** - Value object for safe data transfer outside aggregate

#### Business Rules & Invariants
- **Unique Course Codes** - No duplicate course identifiers allowed
- **Single Enrollment** - One employee can only enroll once per course
- **Capacity Management** - Courses cannot exceed their enrollment limit
- **Employee Validation** - Only existing employees can enroll in courses

### Architectural Patterns

#### Domain-Driven Design
- **Aggregate Root** - `Course` manages enrollment consistency boundary
- **Value Objects** - `CourseCode`, `EmployeeId`, `EnrollmentVO`
- **Factory Methods** - `Course.announce()` for controlled instantiation
- **Rich Domain Model** - Business logic encapsulated in domain entities

#### Hexagonal Architecture
- **Inbound Ports** - Application service interfaces (`CourseService`, `CourseQueryService`)
- **Outbound Ports** - Repository and gateway interfaces
- **Primary Adapters** - REST controllers handling external requests
- **Secondary Adapters** - JPA repositories and inter-module gateways

#### CQRS Implementation
- **Command Side** - State-changing operations (announce, enroll)
- **Query Side** - Data retrieval operations (find courses, enrollments)
- **Use Case Separation** - `AnnouncementUseCase`, `EnrollmentUseCase`

## Architecture Overview

```
┌────────────────────────────────────────────────────────────────────────────┐
│                           Mentoring Modulith                               │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                            │
│  ┌─────────────────────────────┐    ┌─────────────────────────────┐        │
│  │       Courses Module        │    │      Employees Module       │        │
│  │                             │    │                             │        │
│  │  ┌─────────────────────────┐│    │ ┌─────────────────────────┐ │        │
│  │  │   REST Controller       ││    │ │   REST Controller       │ │        │
│  │  │   - CourseController    ││    │ │   - EmployeeController  │ │        │
│  │  │   - Exception Handler   ││    │ │   - Exception Handler   │ │        │
│  │  └─────────────────────────┘│    │ └─────────────────────────┘ │        │
│  │              ▼              │    │              ▼              │        │
│  │  ┌─────────────────────────┐│    │ ┌─────────────────────────┐ │        │
│  │  │   Application Layer     ││    │ │   Application Layer     │ │        │
│  │  │   - CourseAppService    ││    │ │   - EmployeeAppService  │ │        │
│  │  │   - QueryService        ││    │ │   - Query Services      │ │        │
│  │  │   - Use Cases:          ││    │ │                         │ │        │
│  │  │     * Announcement      ││    │ │                         │ │        │
│  │  │     * Enrollment        ││    │ │                         │ │        │
│  │  └─────────────────────────┘│    │ └─────────────────────────┘ │        │
│  │              ▼              │    │              ▼              │        │
│  │  ┌─────────────────────────┐│    │ ┌─────────────────────────┐ │        │
│  │  │     Domain Layer        ││    │ │     Domain Layer        │ │        │
│  │  │   Course Aggregate:     ││    │ │   Employee Aggregate:   │ │        │
│  │  │   - Course (Entity)     ││    │ │   - Employee (Entity)   │ │        │
│  │  │   - Enrollment (Entity) ││    │ │   - EmployeeId (VO)     │ │        │
│  │  │   - CourseCode (VO)     ││    │ │                         │ │        │
│  │  │   - EnrollmentVO (VO)   ││    │ │                         │ │        │
│  │  │   - Business Rules      ││    │ │                         │ │        │
│  │  └─────────────────────────┘│    │ └─────────────────────────┘ │        │
│  │              ▼              │    │              ▲              │        │
│  │  ┌─────────────────────────┐│    │ ┌─────────────────────────┐ │        │
│  │  │   Infrastructure        ││    │ │   Infrastructure        │ │        │
│  │  │   - JPA Repository      ││◄───┼─┤   - JPA Repository      │ │        │
│  │  │   - JPA Entities        ││    │ │   - JPA Entities        │ │        │
│  │  │   - Employees Gateway   ││────┼─┤   - Gateway Impl        │ │        │
│  │  └─────────────────────────┘│    │ └─────────────────────────┘ │        │
│  └─────────────────────────────┘    └─────────────────────────────┘        │
│                                                                            │
└────────────────────────────────────────────────────────────────────────────┘
```

## Domain Model Details

### Course Aggregate (Primary)

The Course aggregate is the main business entity with sophisticated enrollment management:

```java
public class Course {
    private final CourseCode code;
    private final String title;
    private final int limit;
    private List<Enrollment> enrollments = new ArrayList<>();
    
    // Factory method for domain-driven instantiation
    public static Course announce(CourseCode code, String title, int limit) {
        return new Course(code, title, limit);
    }
    
    // Business logic with invariant enforcement
    public EnrollmentVO enroll(EmployeeId employeeId) {
        // Validates: no duplicate enrollments, capacity limits
        // Returns: safe value object, not internal entity
    }
}
```

**Key Business Rules Implemented:**
- Prevents duplicate enrollments per employee
- Enforces capacity limits during enrollment
- Validates course code format and uniqueness
- Encapsulates enrollment state management

### Enrollment Entity (Managed by Course)

```java
public class Enrollment {
    private EmployeeId employeeId;
    private LocalDateTime enrollmentDate;
}
```

**Design Decisions:**
- Not exposed directly outside the Course aggregate
- Enrollment logic handled by Course entity (aggregate root)
- External access via `EnrollmentVO` value objects

### Employee Aggregate (Supporting)

```java
public class Employee {
    private EmployeeId id;
    private String name;
}
```

**Current Implementation:**
- Basic employee domain for validation purposes
- Referenced by courses module via `EmployeesGateway`
- Demonstrates cross-module communication patterns

## Implementation Patterns

### Use Case Architecture

**Announcement Use Case:**
```java
@Component
public class AnnouncementUseCase {
    private final CourseRepository courseRepository;
    
    public void announceCourse(AnnouncementRequest request) {
        // 1. Validate course code uniqueness
        // 2. Create course using factory method
        // 3. Persist via repository port
    }
}
```

**Enrollment Use Case:**
```java
@Component 
public class EnrollmentUseCase {
    private final CourseRepository repository;
    private final EmployeesGateway employeesGateway;
    
    public void enroll(EnrollmentRequest request) {
        // 1. Load course aggregate
        // 2. Validate employee exists (cross-module)
        // 3. Delegate to domain logic
        // 4. Persist changes
    }
}
```

### Cross-Module Communication

**Gateway Pattern Implementation:**
```java
// Port (in courses module)
public interface EmployeesGateway {
    boolean existsById(EmployeeId employeeId);
}

// Adapter (implementation)
@Component
public class DelegatingEmployeesGateway implements EmployeesGateway {
    // Delegates to employees module
}
```

### Repository Pattern with Mapping

**Port Definition:**
```java
public interface CourseRepository {
    void save(Course courseEntity);
    boolean existsWithCode(CourseCode code);
    Course findById(CourseCode courseCode);
    List<CourseDto> findAll(); // Query-side optimized
}
```

**JPA Implementation:**
```java
@Repository
public class DelegatingCourseRepository implements CourseRepository {
    private final CourseJpaRepository jpaRepository;
    private final CourseMapper courseMapper;
    
    // Handles domain ↔ persistence mapping
    // Manages JPA entity relationships
}
```

## CQRS Implementation Details

### Command Side (Write Operations)

**Flow:** REST → Controller → Application Service → Use Case → Domain → Repository

- `AnnouncementRequest` → `AnnouncementUseCase` → `Course.announce()`
- `EnrollmentRequest` → `EnrollmentUseCase` → `Course.enroll()`

### Query Side (Read Operations)

**Flow:** REST → Controller → Query Service → Repository (optimized queries)

- Direct DTO projections for performance
- Separate query interfaces (`CourseQueryService`)
- Database-optimized read operations

## Technology Stack

- **Framework**: Spring Boot 3.5.6
- **Persistence**: Spring Data JPA with H2/PostgreSQL
- **Validation**: Bean Validation (JSR-303)
- **Mapping**: MapStruct for entity ↔ domain conversion
- **Build Tool**: Maven
- **Java Version**: 21+
- **Architecture**: 
  - Lombok (boilerplate reduction)
  - Spring Boot Actuator (monitoring)

## Running the Application

### Prerequisites
```bash
# PostgreSQL (optional - H2 embedded by default)
docker run --name mentoring_postgres -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres
```

### Application Startup
```bash
./mvnw spring-boot:run
```

### API Endpoints

**Course Management:**
```http
# Announce new course
POST /api/courses
Content-Type: application/json
{
  "code": "JAVA101",
  "title": "Java Fundamentals", 
  "limit": 20
}

# Get all courses
GET /api/courses

# Enroll employee
POST /api/courses/JAVA101/enrollments
Content-Type: application/json
{
  "employeeId": 1,
  "courseCode": "JAVA101"
}
```

## Key Design Decisions

### 1. Course as Enrollment Manager
- **Decision**: Course aggregate manages enrollments rather than separate aggregate
- **Rationale**: Simplifies consistency boundaries and business rule enforcement
- **Trade-off**: Less modular but more cohesive for POC purposes

### 2. Cross-Module Validation
- **Pattern**: Gateway pattern for employee existence validation
- **Benefit**: Maintains module boundaries while enabling business rule validation
- **Implementation**: `EmployeesGateway` port with delegating adapter

### 3. Value Object Usage
- **EnrollmentVO**: Safe external representation of internal enrollment data
- **CourseCode/EmployeeId**: Type-safe identifiers with validation
- **Benefit**: Prevents data corruption and enforces business rules

### 4. Factory Methods
- **Pattern**: `Course.announce()` instead of constructors
- **Benefit**: Domain-meaningful instantiation with clear business intent
- **Usage**: Enforces proper course creation workflow

## Testing Strategy

The architecture supports comprehensive testing at multiple levels:

- **Unit Tests**: Domain logic testing without external dependencies
- **Integration Tests**: Application service testing with test containers
- **Architecture Tests**: Module boundary enforcement via ArchUnit
- **Contract Tests**: API contract validation

## Future Enhancements

### Planned Architectural Improvements
1. **Event-Driven Communication**: Replace gateway calls with domain events
2. **Separate Enrollment Aggregate**: Extract enrollment to own bounded context
3. **Advanced CQRS**: Separate read/write databases
4. **Module Packaging**: Enforce compile-time module boundaries

### Scalability Considerations
- **Database Scaling**: Implement read replicas for query operations
- **Caching Layer**: Add Redis for frequently accessed course data
- **API Rate Limiting**: Protect enrollment endpoints from abuse
- **Monitoring**: Enhanced observability with distributed tracing

## Learning Resources

### Architecture Patterns
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/) - Original pattern description
- [Domain-Driven Design](https://www.domainlanguage.com/ddd/) - Eric Evans' foundational work
- [CQRS Pattern](https://martinfowler.com/bliki/CQRS.html) - Martin Fowler's analysis

### Implementation Guides
- [Spring Modulith](https://spring.io/projects/spring-modulith) - Modular monolith support
- [MapStruct Documentation](https://mapstruct.org/) - Domain ↔ DTO mapping
- [ArchUnit](https://www.archunit.org/) - Architecture testing framework

This README reflects the current implementation and provides guidance for understanding and extending the system's architecture.
