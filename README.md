# Mentoring Modulith - Domain-Driven Design with Hexagonal Architecture

## Project Overview

This project demonstrates **Domain-Driven Design (DDD)** principles implemented through **Hexagonal Architecture** (Ports and Adapters pattern). The system manages course announcements, employee enrollments, and employee lifecycle events, built as a modular monolith using Spring Boot with event-driven communication between modules.

**Reference Repository**: Based on training materials from [Training360's DDD Training Repository](https://github.com/Training360/javax-ddd-training-2025-09-29).

### Current Architecture Status

The system currently implements:
- ✅ **Course Management**: Full CRUD operations for courses with enrollment tracking
- ✅ **Employee Management**: Complete employee lifecycle (join, leave) with cross-module integration
- ✅ **Enrollment System**: Complete enrollment workflow with business rules and cancellation
- ✅ **Employee Departure Handling**: Automatic course disenrollment when employees leave
- ✅ **CQRS Pattern**: Separate command and query operations
- ✅ **Event-Driven Communication**: Spring Application Events for cross-module communication
- ✅ **Domain Services**: Multi-aggregate business logic coordination
- ✅ **Cross-Module Communication**: Both gateway pattern and event-driven approaches

## Ubiquitous Language

### Core Domain Concepts

#### Course Management
- **Course** - A training program with enrollment capacity and business rules
- **Course Code** - Unique identifier (minimum 2 characters, validated)
- **Course Title** - Descriptive name (required, non-blank)
- **Course Limit** - Maximum number of enrollments allowed
- **Announce** - Factory method for creating new courses (`Course.announce()`)
- **Cancel Enrollment** - Remove employee from course (used during employee departure)

#### Employee Lifecycle
- **Employee** - Company staff member who can enroll in courses
- **Employee ID** - Unique identifier for employees (cross-module reference)
- **Join** - Employee joining the company (creates employee record)
- **Leave** - Employee departing the company (triggers course disenrollment)
- **Employee Has Leaved Event** - Domain event published when employee departs

#### Enrollment Domain
- **Enrollment** - Registration of an employee in a specific course
- **Enrollment Date** - Timestamp when enrollment occurred
- **Enrollment VO** - Value object for safe data transfer outside aggregate
- **Disenrollment** - Automatic removal from all courses when employee leaves

#### Business Rules & Invariants
- **Unique Course Codes** - No duplicate course identifiers allowed
- **Single Enrollment** - One employee can only enroll once per course
- **Capacity Management** - Courses cannot exceed their enrollment limit
- **Employee Validation** - Only existing employees can enroll in courses
- **Automatic Cleanup** - Departing employees are removed from all enrolled courses
- **Cross-Module Consistency** - Employee departure triggers course enrollment cleanup

### Architectural Patterns

#### Domain-Driven Design
- **Aggregate Root** - `Course` manages enrollment consistency boundary
- **Value Objects** - `CourseCode`, `EmployeeId`, `EnrollmentVO`
- **Factory Methods** - `Course.announce()` for controlled instantiation
- **Rich Domain Model** - Business logic encapsulated in domain entities
- **Domain Services** - `LeaveDomainService` for multi-aggregate operations
- **Domain Events** - `EmployeeHasLeaved` for cross-module communication

#### Hexagonal Architecture
- **Inbound Ports** - Application service interfaces (`CourseService`, `EmployeeApplicationService`)
- **Outbound Ports** - Repository and gateway interfaces
- **Primary Adapters** - REST controllers handling external requests
- **Secondary Adapters** - JPA repositories, event handlers, and inter-module gateways

#### Event-Driven Architecture
- **Domain Events** - `EmployeeHasLeaved` published from employees module
- **Event Handlers** - `EmployeeHasLeavedEventHandler` in courses module
- **Asynchronous Processing** - Spring Application Events for loose coupling
- **Cross-Module Integration** - Event-based communication replacing direct gateway calls

#### CQRS Implementation
- **Command Side** - State-changing operations (announce, enroll, join, leave)
- **Query Side** - Data retrieval operations (find courses, employees, enrollments)
- **Use Case Separation** - `AnnouncementUseCase`, `EnrollmentUseCase`, `EmployeeHasLeavedUseCase`

## Architecture Overview

```
┌───────────────────────────────────────────────────────────────────────────────────┐
│                              Mentoring Modulith                                   │
├───────────────────────────────────────────────────────────────────────────────────┤
│                                                                                   │
│  ┌─────────────────────────────┐         ┌─────────────────────────────┐          │
│  │       Courses Module        │         │      Employees Module       │          │
│  │                             │         │                             │          │
│  │  ┌─────────────────────────┐│         │ ┌─────────────────────────┐ │          │
│  │  │   REST Controller       ││         │ │   REST Controller       │ │          │
│  │  │   - CourseController    ││         │ │   - EmployeesController │ │          │
│  │  │   - Exception Handler   ││         │ │   - CRUD Operations     │ │          │
│  │  └─────────────────────────┘│         │ └─────────────────────────┘ │          │
│  │              ▼              │         │              ▼              │          │
│  │  ┌─────────────────────────┐│         │ ┌─────────────────────────┐ │          │
│  │  │   Application Layer     ││         │ │   Application Layer     │ │          │
│  │  │   - CourseAppService    ││         │ │   - EmployeeAppService  │ │          │
│  │  │   - QueryService        ││         │ │   - Query Services      │ │          │
│  │  │   - Use Cases:          ││         │ │   - Employee Lifecycle  │ │          │
│  │  │     * Announcement      ││         │ │   - Event Publishing    │ │          │
│  │  │     * Enrollment        ││◄────────┼─┤                         │ │          │
│  │  │     * Employee Leave    ││  Events │ │                         │ │          │
│  │  │   - Event Handlers      ││         │ │                         │ │          │
│  │  └─────────────────────────┘│         │ └─────────────────────────┘ │          │
│  │              ▼              │         │              ▼              │          │
│  │  ┌─────────────────────────┐│         │ ┌─────────────────────────┐ │          │
│  │  │     Domain Layer        ││         │ │     Domain Layer        │ │          │
│  │  │   Course Aggregate:     ││         │ │   Employee Aggregate:   │ │          │
│  │  │   - Course (Entity)     ││         │ │   - Employee (Entity)   │ │          │
│  │  │   - Enrollment (Entity) ││         │ │   - EmployeeId (VO)     │ │          │
│  │  │   - CourseCode (VO)     ││         │ │   - Domain Events       │ │          │
│  │  │   - EnrollmentVO (VO)   ││         │ │                         │ │          │
│  │  │   - Business Rules      ││         │ │                         │ │          │
│  │  │   Domain Services:      ││         │ │                         │ │          │
│  │  │   - LeaveDomainService  ││         │ │                         │ │          │
│  │  └─────────────────────────┘│         │ └─────────────────────────┘ │          │
│  │              ▼              │         │              ▲              │          │
│  │  ┌─────────────────────────┐│         │ ┌─────────────────────────┐ │          │
│  │  │   Infrastructure        ││         │ │   Infrastructure        │ │          │
│  │  │   - JPA Repository      ││◄────────┼─┤   - JPA Repository      │ │          │
│  │  │   - JPA Entities        ││Gateway  │ │   - JPA Entities        │ │          │
│  │  │   - Event Handlers      ││         │ │   - Event Publishers    │ │          │
│  │  │   - Employees Gateway   ││─────────┼─┤   - Gateway Impl        │ │          │
│  │  └─────────────────────────┘│         │ └─────────────────────────┘ │          │
│  └─────────────────────────────┘         └─────────────────────────────┘          │
│                                                                                   │
└───────────────────────────────────────────────────────────────────────────────────┘
```

## Domain Model Details

### Course Aggregate (Enhanced)

The Course aggregate now includes comprehensive enrollment lifecycle management:

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
    
    // NEW: Support for employee departure cleanup
    public void cancelEnrollment(EmployeeId employeeId) {
        // Removes employee from course when they leave company
    }
}
```

**Enhanced Business Rules:**
- Prevents duplicate enrollments per employee
- Enforces capacity limits during enrollment
- Validates course code format and uniqueness
- Encapsulates enrollment state management
- **NEW**: Supports enrollment cancellation for departing employees
- **NEW**: Integrates with domain services for multi-course operations

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

### Employee Aggregate (Expanded)

```java
public class Employee {
    private EmployeeId id;
    private String name;
    
    // Business operations for employee lifecycle
}
```

**Enhanced Implementation:**
- Complete employee lifecycle management (join/leave)
- Event publishing for cross-module communication
- Integration with course enrollment system
- Domain event generation for employee departure

### Domain Services

**Leave Domain Service** - handles multi-aggregate business logic:
```java
@Component
public class LeaveDomainService {
    public void leave(List<Course> courses, EmployeeId employeeId) {
        // Coordinates employee removal across multiple course aggregates
        // Implements business rule: departing employees must be removed from all courses
    }
}
```

**Key Characteristics:**
- Operates across multiple aggregates (violates single aggregate rule purposefully)
- Encapsulates complex business logic that spans domain boundaries
- Used when business operations cannot be contained within single aggregate
- Maintains consistency across multiple Course entities

## Implementation Patterns

### Enhanced Use Case Architecture

**Employee Departure Use Case:**
```java
@Service
public class EmployeeHasLeavedUseCase {
    private final CourseRepository repository;
    private final EmployeesGateway employeesGateway;
    private final LeaveDomainService leaveDomainService;
    
    public void leave(EmployeeId employeeId) {
        // 1. Validate employee exists
        // 2. Load all courses
        // 3. Use domain service to coordinate departure
        // 4. Persist all changes atomically
    }
}
```

**Event-Driven Communication:**
```java
// Domain Event
public record EmployeeHasLeaved(long employeeId) {}

// Event Handler in Courses Module
@EventListener
public void handleEvent(EmployeeHasLeaved event) {
    courseService.employeeHasLeaved(event.employeeId());
}
```

### Cross-Module Communication Evolution

**Dual Communication Patterns:**

1. **Gateway Pattern** (Original):
```java
public interface EmployeesGateway {
    boolean existsById(EmployeeId employeeId);
}
```

2. **Event-Driven Pattern** (New):
```java
// Published from Employees Module
@EventListener
public void handleEmployeeDeparture(EmployeeHasLeaved event) {
    // Automatic course cleanup
}
```

**Benefits of Event-Driven Approach:**
- Loose coupling between modules
- Asynchronous processing capabilities
- Better scalability and resilience
- Clear separation of concerns
- Easier testing and module independence

## Enhanced API Endpoints

### Course Management
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

### Employee Management (NEW)
```http
# Employee joins company
POST /api/employees
Content-Type: application/json
{
  "id": 1,
  "name": "John Doe"
}

# Get all employees
GET /api/employees

# Employee leaves company (triggers course cleanup)
DELETE /api/employees/1
```

## Key Design Decisions

### 1. Event-Driven Architecture Introduction
- **Decision**: Implement Spring Application Events for cross-module communication
- **Benefit**: Loose coupling, better scalability, clearer separation of concerns
- **Implementation**: `EmployeeHasLeaved` event with `EmployeeHasLeavedEventHandler`
- **Trade-off**: Added complexity but improved maintainability and testability

### 2. Domain Services for Multi-Aggregate Operations
- **Decision**: Introduce `LeaveDomainService` for employee departure coordination
- **Rationale**: Business logic spans multiple Course aggregates
- **Benefit**: Centralized complex business logic, maintains consistency
- **DDD Consideration**: Acceptable violation of single aggregate rule for legitimate business needs

### 3. Employee Lifecycle Management
- **Decision**: Full employee CRUD with lifecycle events
- **Implementation**: Join/Leave operations with automatic course cleanup
- **Business Value**: Maintains data consistency across employee lifecycle
- **Integration**: Event-driven cleanup ensures no orphaned enrollments

### 4. Dual Communication Patterns
- **Pattern**: Both Gateway and Event-driven communication coexist
- **Gateway**: For synchronous validation (employee existence)
- **Events**: For asynchronous lifecycle management (employee departure)
- **Benefit**: Right tool for right use case, demonstrates architectural evolution

## Enhanced Testing Strategy

The evolved architecture supports comprehensive testing:

- **Unit Tests**: Domain logic with rich business rules and domain services
- **Integration Tests**: Event-driven communication between modules
- **Architecture Tests**: Module boundaries and event flow validation
- **Contract Tests**: API contracts and event schemas
- **Domain Service Tests**: Multi-aggregate operation validation

## Future Enhancements

### Planned Architectural Improvements
1. **Enhanced Event Sourcing**: Store events for audit and replay capabilities
2. **Saga Pattern**: Handle complex multi-step business processes
3. **Event Store**: Dedicated storage for domain events
4. **Advanced CQRS**: Separate read/write databases with event projection
5. **Module Packaging**: Enforce compile-time module boundaries with Spring Modulith

### Scalability Considerations
- **Event Streaming**: Replace in-memory events with Apache Kafka
- **Database Per Module**: Separate databases for true microservice preparation
- **Event Replay**: Capability to rebuild state from events
- **Distributed Tracing**: Enhanced observability across event flows

## Technology Stack Updates

- **Framework**: Spring Boot 3.5.6
- **Persistence**: Spring Data JPA with H2/PostgreSQL
- **Events**: Spring Application Events (in-memory)
- **Validation**: Bean Validation (JSR-303)
- **Mapping**: MapStruct for entity ↔ domain conversion
- **Build Tool**: Maven
- **Java Version**: 21+
- **Architecture**: 
  - Lombok (boilerplate reduction)
  - Spring Boot Actuator (monitoring)
  - **NEW**: Spring Events for cross-module communication

This README reflects the current implementation and provides guidance for understanding and extending the system's architecture.
