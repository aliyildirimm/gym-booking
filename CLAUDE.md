# Project Context for Claude

## Project Overview
**Name**: Gym Booking System


## Architecture

### System Design
```
Monorepo with:
- 2 Spring Boot microservices (Class Service, Booking Service)
- React + Ionic frontend
- PostgreSQL database
- Event-driven communication (in-memory Spring Events)
- gRPC for synchronous calls
- All local development via Docker Compose
```

### Service Ports
- Class Service: 8081 (REST), 9091 (gRPC)
- Booking Service: 8082 (REST)
- Frontend: 3000
- PostgreSQL: 5432

### Communication Patterns
1. **gRPC**: Booking Service → Class Service (validate class exists + capacity)
2. **Events**: Booking Service publishes `BookingCreatedEvent` → Class Service decrements spots

## Domain Model

### Class Service
**Endpoints**:
- `POST /classes` - Create gym class
- `GET /classes` - List all classes
- `GET /classes/{id}` - Get single class

### Booking Service
**Endpoints**:
- `POST /bookings` - Create booking (calls gRPC, publishes event)
- `GET /bookings` - List all bookings


## Architecture Pattern: Domain-Driven Design (DDD)

We use a **3-layer architecture** with **Hexagonal Architecture** (Ports & Adapters):

### 1. Application Layer
**Purpose**: Entry points that orchestrate domain logic
**Contains**:
- REST Controllers (HTTP entry points)
- Request/Response DTOs
- Exception handlers
- Validates input and converts between DTOs and domain objects

### 2. Domain Layer (Core)
**Purpose**: Business logic, isolated from infrastructure
**Contains**:
- **Value Objects**: Immutable, self-validating (e.g., `ClassName`, `Capacity`)
- **Aggregates**: Entities with behavior, enforce invariants (e.g., `GymClass`)
- **Repository Interfaces (Ports)**: Define persistence API without implementation
- **Domain Events**: Represent domain occurrences (e.g., `ClassCreatedEvent`)

**Key principle**: Domain layer does NOT depend on infrastructure (no JPA annotations!)

### 3. Infrastructure Layer
**Purpose**: Implementation details (database, external services)
**Contains**:
- **Repository Implementations (Adapters)**: Implement domain repository interfaces
- **JPA Entities**: Separate from domain model, anemic (just persistence)
- **Mappers**: Convert between domain objects and JPA entities
- **Database configuration**

**Key principle**: Infrastructure depends on domain (implements domain interfaces)

---

## Tech Stack Details

### Backend (Spring Boot 3.x)
- **Framework**: Spring Boot
- **Architecture**: DDD with Hexagonal Architecture (3-layer)
- **Database**: Spring Data JPA + PostgreSQL
- **gRPC**: `net.devh:grpc-spring-boot-starter`
- **Build**: Maven
- **Java Version**: 17

### Frontend
- **Framework**: React 18
- **UI Library**: Ionic React 8
- **Routing**: React Router v5 (via @ionic/react-router)
- **HTTP Client**: Axios
- **Build Tool**: Vite 6
- **TypeScript**: Full TypeScript support

### Infrastructure
- **Docker Compose**: PostgreSQL + 2 Java services + frontend
- **Database Init**: SQL scripts in docker-compose volumes

## Monorepo Structure (Simplified DDD Architecture)
```
gym-booking/
├── plan.md                      # Implementation plan
├── CLAUDE.md                    # This file
├── docker-compose.yml
├── backend/
│   ├── class-service/           # Simplified DDD structure
│   │   ├── pom.xml
│   │   └── src/main/
│   │       ├── java/com/gym/classservice/
│   │       │   ├── ClassServiceApplication.java
│   │       │   │
│   │       │   ├── application/              # APPLICATION LAYER
│   │       │   │   ├── rest/
│   │       │   │   │   ├── ClassController.java       # Entry point, uses Spring Data directly
│   │       │   │   │   └── GlobalExceptionHandler.java
│   │       │   │   └── dto/
│   │       │   │       ├── CreateClassRequest.java    # DTO validation (@NotBlank, @Size)
│   │       │   │       └── ClassResponse.java
│   │       │   │
│   │       │   ├── domain/                   # DOMAIN LAYER (Core business logic)
│   │       │   │   └── model/
│   │       │   │       ├── GymClass.java              # Aggregate root (@Entity, has behavior)
│   │       │   │       └── Capacity.java              # Value object (@Embeddable, has behavior)
│   │       │   │
│   │       │   └── infrastructure/           # INFRASTRUCTURE LAYER
│   │       │       └── persistence/
│   │       │           └── ClassJpaRepository.java    # Spring Data JPA (works directly with GymClass)
│   │       │
│   │       └── resources/
│   │           └── application.yml
│   │
│   ├── booking-service/         # (Will follow same simplified structure)
│   │   ├── pom.xml
│   │   └── src/main/
│   │       ├── java/com/gym/bookingservice/
│   │       │   ├── BookingServiceApplication.java
│   │       │   ├── application/              # Entry points
│   │       │   ├── domain/                   # Business logic
│   │       │   └── infrastructure/           # Implementation
│   │       └── resources/
│   │           └── application.yml
│   │
│   └── shared/
│       ├── pom.xml
│       ├── proto/
│       │   └── class_service.proto
│       └── src/main/java/com/gym/shared/
│           └── BookingCreatedEvent.java
│
└── frontend/
    ├── package.json
    └── src/
        └── pages/
            ├── ClassList.jsx
            └── BookingForm.jsx
```

## Useful Commands

### Docker
```bash
docker-compose up -d           # Start all services
docker-compose logs -f class-service
docker-compose down -v         # Stop and remove volumes
```

### Maven
```bash
./mvnw clean install           # Build
./mvnw spring-boot:run         # Run locally (outside Docker)
./mvnw test                    # Run tests
```

### gRPC Testing
```bash
grpcurl -plaintext localhost:9091 list
grpcurl -plaintext -d '{"id": 1}' localhost:9091 ClassService/GetClass
```

---

## DDD Design Principles (Simplified Pragmatic Approach)

**Philosophy**: Keep DDD benefits, remove unnecessary complexity. Perfect for learning Java.

### Application Layer Rules
✅ Controllers orchestrate domain logic (no business logic in controllers)
✅ Validate input via DTO annotations (`@NotBlank`, `@Size`, `@Min`)
✅ Call domain methods (aggregate methods like `gymClass.reserveSpot()`)
✅ Use Spring Data JPA directly (no abstraction layer for simple cases)
✅ Convert domain objects → DTOs for response
❌ NO business logic or calculations

### Domain Layer Rules (Simplified)
✅ Business logic lives here (in aggregates and value objects)
✅ Use value objects when they have **behavior** (e.g., `Capacity` with `reserveSpot()`)
✅ Skip value objects for simple strings (use validation in DTO instead)
✅ Aggregates are JPA entities (`@Entity`) with domain behavior
✅ Aggregates enforce invariants (use methods like `reserveSpot()`, not setters)
✅ Embedded value objects use `@Embeddable`
❌ NO setters on aggregates (use methods for state changes)
❌ NO domain events infrastructure (use YAGNI - add when actually needed)

### Infrastructure Layer Rules
✅ Spring Data JPA repositories work directly with domain model
✅ No separate persistence model for simple entities
✅ No mapper layer (domain = persistence for simple models)
❌ NO business logic

### Data Flow Pattern (Simplified)
```
HTTP Request → DTO Validation → Domain Factory → Aggregate Method → Spring Data Save → DTO Response
```

**Example**:
```java
// Controller (Application Layer)
@PostMapping
public ResponseEntity<ClassResponse> createClass(@Valid @RequestBody CreateClassRequest request) {
    // 1. Create value object (only if it has behavior)
    Capacity capacity = Capacity.of(request.getCapacity());

    // 2. Use domain factory (String name validated in DTO)
    GymClass gymClass = GymClass.create(request.getName(), capacity);

    // 3. Persist via Spring Data JPA
    GymClass saved = classRepository.save(gymClass);

    // 4. Convert to DTO
    return ResponseEntity.ok(ClassResponse.fromDomain(saved));
}
```

### Simplifications Made
- ❌ **Removed**: Separate JPA entity + mapper (domain IS the entity)
- ❌ **Removed**: Domain events infrastructure (YAGNI for simple CRUD)
- ❌ **Removed**: Value objects for simple strings (validation moved to DTO)
- ❌ **Removed**: Repository abstraction layer (Spring Data is already abstraction)
- ✅ **Kept**: 3-layer architecture (Application/Domain/Infrastructure)
- ✅ **Kept**: Value objects with behavior (Capacity)
- ✅ **Kept**: Domain behavior in aggregates (reserveSpot, releaseSpot)

### Key Benefits
- **Simpler**: 60% less boilerplate, easier to learn Java
- **Testable**: Domain logic still isolated in domain methods
- **Pragmatic**: Add complexity when you need it (YAGNI principle)
- **DDD Lite**: Keeps core DDD benefits without enterprise overhead
