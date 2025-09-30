# Mentoring Modulith - Domain-Driven Design with Hexagonal Architecture POC

## Project Overview

This is a **proof-of-concept (POC)** project demonstrating **Domain-Driven Design (DDD)** principles implemented through **Hexagonal Architecture** (also known as Ports and Adapters pattern). The project showcases a course management system built as a modular monolith using Spring Boot.

**Reference Repository**: This project is based on the training materials from [Training360's DDD Training Repository](https://github.com/Training360/javax-ddd-training-2025-09-29).

### Architectural Evolution Note

> **⚠️ Pivot Decision (2025-09-30)**: After this commit, the **courses aggregate** will be responsible for handling **jelentkezések (registrations)**, not just announcing courses. This architectural decision was made to save development time and simplify the domain model for the POC. In a production system, registrations might warrant their own bounded context and aggregate.

## Ubiquitous Language

This section documents the domain terminology used throughout the system. These terms form the **ubiquitous language** that bridges the gap between domain experts and developers.

### Core Domain Concepts

#### Course Management
- **Course** - A training program that can be announced and enrolled in
- **Course Code** - Unique identifier for a course (minimum 2 characters, alphanumeric)
- **Course Title** - Descriptive name of the course
- **Announce** - The act of making a course available for enrollment
- **Announcement** - The process/event of creating a new course offering

#### Registration & Enrollment (Planned)
- **Jelentkezés** - Hungarian term for "registration" or "enrollment"
- **Enrollment** - The act of registering for a course
- **Employee ID** - Identifier for the person enrolling in the course
- **Enrollment Date** - When the registration took place
- **Headcount** - Maximum number of participants allowed in a course
- **Max Headcount** - The enrollment limit for a course

### Business Rules & Constraints

#### Course Rules
- **Unique Course Code** - No two courses can have the same code
- **Non-blank Title** - Course must have a meaningful title
- **Single Registration** - One person can only register once per course
- **Capacity Limit** - Courses have maximum enrollment capacity

#### Validation Rules
- **Course Code Validation** - Must not be null, blank, and minimum 2 characters
- **Title Validation** - Must not be null or blank
- **Duplicate Prevention** - System prevents duplicate course codes

### Technical Domain Language

#### Architecture Patterns
- **Aggregate** - Consistency boundary around related domain objects
- **Entity** - Domain object with identity that can change over time
- **Value Object (VO)** - Immutable domain object defined by its attributes
- **Factory Method** - Pattern for creating domain objects (e.g., `Course.announce()`)
- **Domain Service** - Service containing domain logic that doesn't belong to entities

#### Ports & Adapters
- **Inbound Port** - Interface defining application use cases
- **Outbound Port** - Interface defining external dependencies
- **Primary Adapter** - External interface adapting to application (REST controllers)
- **Secondary Adapter** - Implementation adapting application to external systems (repositories)

#### CQRS Terminology
- **Command** - Request to modify system state
- **Query** - Request to retrieve data without modification
- **Command Handler** - Processes state-changing operations
- **Query Handler** - Processes data retrieval operations

### Data Transfer Objects (DTOs)

#### Input/Output Models
- **AnnouncementRequest** - Command DTO for creating courses
- **CourseDto** - Query DTO for course data representation
- **CourseJpaEntity** - Persistence model for database mapping

### Domain Events (Future)
- **Course Announced** - Event published when a new course is created
- **Student Enrolled** - Event published when enrollment occurs
- **Enrollment Rejected** - Event published when enrollment fails (capacity/duplicate)

### Bounded Context Terms
- **Course Management Context** - The boundary containing course and enrollment logic
- **Employee Context** - (Future) Context managing employee information
- **Training Context** - (Future) Context managing training content and materials

### Hungarian Domain Terms
- **Jelentkezés** - Registration/Enrollment
- **Kurzus** - Course
- **Képzés** - Training
- **Résztvevő** - Participant
- **Létszám** - Headcount/Capacity

### Design Patterns in Domain
- **Factory Pattern** - `Course.announce()` creates new course instances
- **Builder Pattern** - (Future) For complex course creation
- **Repository Pattern** - Data access abstraction
- **Specification Pattern** - (Future) For complex business rules

### Validation Concepts
- **Defensive Programming** - Validating inputs at domain boundaries
- **Invariant** - Business rule that must always be true
- **Consistency** - Ensuring domain rules are maintained
- **Domain Validation** - Business rule validation within domain objects

This ubiquitous language ensures consistent terminology across the development team and aligns technical implementation with business concepts.

## Key Architectural Concepts Demonstrated

- **Hexagonal Architecture**: Clear separation between business logic and external concerns
- **Domain-Driven Design**: Rich domain models with business logic encapsulation
- **CQRS Pattern**: Separation of command (write) and query (read) operations
- **Ports and Adapters**: Dependency inversion through interfaces
- **Modular Monolith**: Bounded contexts within a single deployable unit

## Architecture Overview

```
┌───────────────────────────────────────────────────────────────┐
│                    Hexagonal Architecture                     │
├───────────────────────────────────────────────────────────────┤
│  Primary Adapters (Inbound)    │  Secondary Adapters (Out)    │
│  ┌─────────────────────────┐   │  ┌─────────────────────────┐ │
│  │   REST Controller       │   │  │   JPA Repository        │ │
│  │   - CourseController    │   │  │   - DelegatingCourse    │ │
│  │   - Exception Handler   │   │  │     Repository          │ │
│  └─────────────────────────┘   │  └─────────────────────────┘ │
│              │                 │               ▲              │
│              ▼                 │               │              │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │              Application Layer (Ports)                   │ │
│  │  ┌─────────────────────┐   ┌─────────────────────────┐   │ │
│  │  │   Inbound Ports     │   │   Outbound Ports        │   │ │
│  │  │   - CourseService   │   │   - CourseRepository    │   │ │
│  │  │   - CourseQuery     │   │   (Interfaces)          │   │ │
│  │  │     Service         │   │                         │   │ │
│  │  └─────────────────────┘   └─────────────────────────┘   │ │
│  │              │                          ▲                │ │
│  │              ▼                          │                │ │
│  │  ┌─────────────────────────────────────────────────────┐ │ │
│  │  │         Application Services                        │ │ │
│  │  │  - CourseApplicationService                         │ │ │
│  │  │  - DelegatingCourseQueryService                     │ │ │
│  │  └─────────────────────────────────────────────────────┘ │ │
│  └──────────────────────────────────────────────────────────┘ │
│                             │                                 │
│                             ▼                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │                   Domain Layer                           │ │
│  │  ┌─────────────────────────────────────────────────────┐ │ │
│  │  │              Course Aggregate                       │ │ │
│  │  │  - Course (Entity)                                  │ │ │
│  │  │  - CourseCode (Value Object)                        │ │ │
│  │  │  - Business Rules & Invariants                      │ │ │
│  │  └─────────────────────────────────────────────────────┘ │ │
│  └──────────────────────────────────────────────────────────┘ │
└───────────────────────────────────────────────────────────────┘
```

## Domain Model

### Course Aggregate
The **Course** aggregate is the central business concept with the following characteristics:

- **Entity**: `Course` - Represents a training course with business logic
- **Value Object**: `CourseCode` - Immutable identifier with validation rules
- **Factory Method**: `Course.announce()` - Creates new course instances
- **Business Rules**:
  - Course code cannot be null or blank and must be at least 2 characters
  - Course title cannot be null or blank
  - Course codes must be unique across the system

```java
// Domain Entity with Factory Method
public class Course {
    private final CourseCode code;
    private final String title;
    
    public static Course announce(CourseCode code, String title) {
        return new Course(code, title);
    }
}

// Value Object with Business Rules
public record CourseCode(String code) {
    public CourseCode {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Course code cannot be null or blank");
        }
        if (code.length() < 2) {
            throw new IllegalArgumentException("Course code must be longer than 1 characters long");
        }
    }
}
```

## Hexagonal Architecture Implementation

### Inbound Ports (Primary Ports)
Define the application's use cases and external interfaces:

- **`CourseService`** - Command operations (course announcement)
- **`CourseQueryService`** - Query operations (course retrieval)

### Outbound Ports (Secondary Ports)
Define dependencies on external systems:

- **`CourseRepository`** - Data persistence abstraction

### Primary Adapters (Inbound)
Handle external requests and translate them to application calls:

- **`CourseController`** - REST API endpoints
- **`CourseExceptionHandler`** - Global exception handling

### Secondary Adapters (Outbound)
Implement external system integrations:

- **`DelegatingCourseRepository`** - JPA implementation
- **`CourseJpaEntity`** - Database mapping
- **`CourseJpaRepository`** - Spring Data JPA interface

## CQRS Implementation

The project demonstrates **Command Query Responsibility Segregation**:

### Command Side (Write Operations)
- **Command**: `AnnouncementRequest` DTO
- **Handler**: `CourseApplicationService.announceCourse()`
- **Business Logic**: Domain validation and course creation
- **Persistence**: Through `CourseRepository` port

### Query Side (Read Operations)
- **Query Handler**: `DelegatingCourseQueryService.findAll()`
- **Response**: `CourseDto` list
- **Optimization**: Direct database queries optimized for reading

## Key Design Principles Applied

### 1. Dependency Inversion
- Application layer depends on abstractions (ports), not implementations
- External dependencies are injected through interfaces

### 2. Single Responsibility
- Each class has a focused responsibility
- Clear separation between layers

### 3. Domain Purity
- Domain objects contain no infrastructure dependencies
- Business rules are encapsulated within domain entities

### 4. Interface Segregation
- Separate interfaces for different concerns (command vs query)
- Focused, cohesive contracts

## Technology Stack

- **Framework**: Spring Boot 3.5.6
- **Persistence**: Spring Data JPA with PostgreSQL
- **Validation**: Bean Validation (JSR-303)
- **Build Tool**: Maven
- **Java Version**: 25
- **Additional Features**: 
  - Spring Boot Actuator (monitoring)
  - Spring HATEOAS (REST maturity)
  - Lombok (boilerplate reduction)

## Running the Application

```shell
docker run --name my_postgres  -e POSTGRES_PASSWORD=password  -p 5432:5432   -d postgres
```

## Learning Path: From Web Developer to Enterprise Architecture

### Architecture Patterns

#### Hexagonal Architecture (Ports and Adapters)
- **Description**: Isolates the core business logic from external concerns
- **Key Concepts**: Ports (interfaces), Adapters (implementations), Dependency Inversion
- **Resources**:
  - [Hexagonal Architecture by Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
  - [Clean Architecture by Robert Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- **Practice**: Refactor existing MVC applications to hexagonal structure

#### CQRS (Command Query Responsibility Segregation)
- **Description**: Separate read and write operations using different models
- **Key Concepts**: Command side, Query side, Event sourcing integration
- **Resources**:
  - [CQRS by Martin Fowler](https://martinfowler.com/bliki/CQRS.html)
  - [Event Sourcing by Martin Fowler](https://martinfowler.com/eaaDev/EventSourcing.html)
- **Practice**: Build a simple e-commerce system with separate read/write models

#### Event-Driven Architecture
- **Description**: Components communicate through events rather than direct calls
- **Key Concepts**: Event producers, Event consumers, Event stores, Saga patterns
- **Resources**:
  - [Building Event-Driven Microservices](https://www.oreilly.com/library/view/building-event-driven-microservices/9781492057888/)
  - [Event Storming](https://www.eventstorming.com/)
- **Practice**: Design an order processing system using events

### Distributed Systems

#### Kafka (Message Broker, Event Streaming)
- **Description**: Distributed streaming platform for building real-time data pipelines
- **Key Concepts**: Topics, Partitions, Producers, Consumers, Stream processing
- **Resources**:
  - [Kafka: The Definitive Guide](https://www.confluent.io/resources/kafka-the-definitive-guide/)
  - [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- **Setup**: `docker run -p 9092:9092 apache/kafka:latest`
- **Practice**: Build a real-time analytics pipeline

#### Microservices Architecture
- **Description**: Architectural style that structures an application as a collection of services
- **Key Concepts**: Service boundaries, API gateways, Service mesh, Circuit breakers
- **Resources**:
  - [Microservices by Martin Fowler](https://martinfowler.com/articles/microservices.html)
  - [Building Microservices by Sam Newman](https://samnewman.io/books/building_microservices/)
- **Practice**: Break down a monolith into microservices

#### Dead Letter Queues and Retries
- **Description**: Handle failed message processing gracefully
- **Key Concepts**: Retry policies, Exponential backoff, DLQ patterns, Poison messages
- **Resources**:
  - [Enterprise Integration Patterns](https://www.enterpriseintegrationpatterns.com/)
  - [AWS SQS Dead Letter Queues](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-dead-letter-queues.html)
- **Practice**: Implement retry logic with Spring Boot and RabbitMQ

### Data Technologies

#### Elasticsearch for Search Capabilities
- **Description**: Distributed search and analytics engine
- **Key Concepts**: Inverted indexes, Analyzers, Aggregations, Mapping
- **Resources**:
  - [Elasticsearch: The Definitive Guide](https://www.elastic.co/guide/en/elasticsearch/guide/current/index.html)
  - [Elastic Stack Documentation](https://www.elastic.co/guide/index.html)
- **Setup**: `docker run -p 9200:9200 -p 9300:9300 elasticsearch:8.11.0`
- **Practice**: Build a product search with faceted navigation

#### MongoDB as Alternative to Traditional SQL
- **Description**: Document-oriented NoSQL database
- **Key Concepts**: Documents, Collections, Aggregation pipeline, Sharding
- **Resources**:
  - [MongoDB University](https://university.mongodb.com/)
  - [MongoDB: The Definitive Guide](https://www.oreilly.com/library/view/mongodb-the-definitive/9781491954454/)
- **Setup**: `docker run -p 27017:27017 mongo:latest`
- **Practice**: Build a content management system with flexible schemas

#### Debezium for Database Synchronization
- **Description**: Change data capture (CDC) platform
- **Key Concepts**: CDC, Connectors, Change events, Kafka Connect
- **Resources**:
  - [Debezium Documentation](https://debezium.io/documentation/)
  - [Change Data Capture with Debezium](https://developers.redhat.com/blog/2018/12/14/change-data-capture-with-debezium-kafka-connector)
- **Practice**: Sync data between PostgreSQL and Elasticsearch in real-time

#### Data Warehousing Concepts (Star Schema, OLAP)
- **Description**: Analytical data storage and processing patterns
- **Key Concepts**: Fact tables, Dimension tables, ETL/ELT, Data cubes, OLAP operations
- **Resources**:
  - [The Data Warehouse Toolkit by Ralph Kimball](https://www.kimballgroup.com/data-warehouse-business-intelligence-resources/books/)
  - [Apache Spark Documentation](https://spark.apache.org/docs/latest/)
- **Practice**: Build a sales analytics data warehouse

### Advanced Concepts

#### CAP Theorem
- **Description**: Consistency, Availability, Partition tolerance - choose two
- **Key Concepts**: Strong consistency, Eventual consistency, Partition tolerance
- **Resources**:
  - [CAP Theorem by Eric Brewer](https://www.infoq.com/articles/cap-twelve-years-later-how-the-rules-have-changed/)
  - [Designing Data-Intensive Applications](https://dataintensive.net/)
- **Practice**: Analyze different database systems through CAP lens

#### Eventual Consistency vs. ACID
- **Description**: Trade-offs between consistency models
- **Key Concepts**: ACID properties, BASE properties, Conflict resolution
- **Resources**:
  - [Eventually Consistent by Werner Vogels](https://www.allthingsdistributed.com/2008/12/eventually_consistent.html)
  - [ACID vs BASE](https://neo4j.com/blog/acid-vs-base-consistency-models-explained/)
- **Practice**: Implement eventually consistent systems with compensation

#### Denormalization Strategies
- **Description**: Trading storage for query performance
- **Key Concepts**: Materialized views, Read replicas, Data duplication patterns
- **Resources**:
  - [High Performance MySQL](https://www.oreilly.com/library/view/high-performance-mysql/9781449332471/)
  - [Database Denormalization Techniques](https://www.vertabelo.com/blog/database-denormalization/)
- **Practice**: Optimize slow queries through strategic denormalization

#### Database Scaling Approaches
- **Description**: Techniques for handling growing data and traffic
- **Key Concepts**: Vertical scaling, Horizontal scaling, Sharding, Read replicas
- **Resources**:
  - [Scalability Rules by Martin Abbott](https://scalabilityrules.com/)
  - [Database Sharding Explained](https://www.digitalocean.com/community/tutorials/understanding-database-sharding)
- **Practice**: Implement database sharding for a multi-tenant application

## Recommended Learning Order

1. **Start with Architecture**: Hexagonal Architecture → CQRS → Event-driven
2. **Add Messaging**: Kafka basics → Dead letter queues
3. **Explore Data**: MongoDB → Elasticsearch → Debezium
4. **Scale Systems**: Microservices → Database scaling
5. **Master Concepts**: CAP theorem → Consistency models → Warehousing

## Hands-On Projects

1. **E-commerce Platform**: Implement with hexagonal architecture, CQRS, and Kafka
2. **Real-time Analytics**: Use Kafka + Elasticsearch + MongoDB
3. **Multi-tenant SaaS**: Practice sharding and microservices
4. **Data Pipeline**: Build ETL with Debezium and data warehousing

## Tools to Install

```bash
# Message Brokers
docker run -p 9092:9092 apache/kafka:latest
docker run -p 5672:5672 rabbitmq:management

# Databases
docker run -p 27017:27017 mongo:latest
docker run -p 9200:9200 elasticsearch:8.11.0

# Monitoring
docker run -p 3000:3000 grafana/grafana
docker run -p 9090:9090 prom/prometheus
```
