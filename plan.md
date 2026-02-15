# Gym Booking Monorepo - Implementation Plan

## Overview
Build gym class booking system: 2 Spring Boot microservices + React frontend, all local Docker.

**Domain**: Users book spots in gym classes (yoga, spin, etc)
**Scope**: CRUD only - create/list classes, create/list bookings
**Learning focus**: Spring Boot basics, gRPC, event-driven, Docker

---

## Architecture Decisions

### 1. Event Bus (In-Memory)
**Decision**: Use Spring Application Events (not Google Pub/Sub)
- **Why**: Local only, no GCP needed, built into Spring
- **How**: Shared event library, services publish/subscribe via `@EventListener`
- **Event**: `BookingCreatedEvent` → Class Service decrements available spots

### 2. gRPC Communication
**Decision**: Spring Boot gRPC starter
- **Library**: `net.devh:grpc-spring-boot-starter`
- **Use case**: Booking Service calls Class Service to verify class exists + has capacity
- **Proto**: Define `ClassService.proto` with `GetClass(id)` RPC

### 3. Database
**Decision**: Single PostgreSQL instance, separate schemas
- `class_service` schema → classes table
- `booking_service` schema → bookings table
- Docker Compose: 1 postgres container, init scripts create schemas

### 4. Monorepo Structure
```
gym-booking/
├── docker-compose.yml
├── backend/
│   ├── class-service/          # Port 8081
│   ├── booking-service/        # Port 8082
│   └── shared/                 # Event models, proto files
└── frontend/                   # Port 3000, React + Ionic
```

---

## Incremental Build Plan

### Phase 1: Foundation (30-45 min)
**Goal**: Working monorepo skeleton

1. Create monorepo structure
2. Setup `docker-compose.yml`: postgres + 2 Java services + frontend
3. Create shared event library (empty for now)
4. Init postgres schemas via SQL scripts

**Deliverable**: `docker-compose up` runs, postgres accessible

---

### Phase 2: Class Service (60-90 min)
**Goal**: Standalone CRUD service (easiest, learn Spring Boot here)

**Files to create**:
- `backend/class-service/pom.xml` - Spring Boot deps (web, jpa, postgres)
- `backend/class-service/src/main/resources/application.yml` - DB config, port 8081
- `backend/class-service/src/main/java/.../`
  - `ClassServiceApplication.java` - Main entry
  - `Class.java` - Entity (id, name, capacity, availableSpots)
  - `ClassRepository.java` - JPA repo
  - `ClassController.java` - REST endpoints

**Endpoints**:
- `POST /classes` - Create class
- `GET /classes` - List all classes
- `GET /classes/{id}` - Get one class

**Event handling**:
- Listen to `BookingCreatedEvent` → decrement `availableSpots`

**Deliverable**: Curl/Postman can create + list classes

---

### Phase 3: gRPC Setup (30 min)
**Goal**: Class Service exposes gRPC endpoint

**Files to create**:
- `backend/shared/proto/class_service.proto`
  ```proto
  service ClassService {
    rpc GetClass(GetClassRequest) returns (GetClassResponse);
  }
  ```
- Update `class-service/pom.xml`: add gRPC deps + protobuf plugin
- `backend/class-service/.../ClassGrpcService.java` - Implements gRPC

**Deliverable**: gRPC endpoint running on 9091, testable via grpcurl

---

### Phase 4: Booking Service (60 min)
**Goal**: Uses gRPC to validate, publishes events

**Files to create**:
- `backend/booking-service/pom.xml` - Spring Boot + gRPC client
- `backend/booking-service/src/main/resources/application.yml` - DB config, port 8082, gRPC target
- `backend/booking-service/src/main/java/.../`
  - `BookingServiceApplication.java`
  - `Booking.java` - Entity (id, classId, userName)
  - `BookingRepository.java`
  - `BookingController.java` - REST endpoints
  - `BookingService.java` - Business logic

**Endpoints**:
- `POST /bookings` - Create booking (validates via gRPC, publishes event)
- `GET /bookings` - List bookings

**Logic in POST /bookings**:
1. Call Class Service gRPC to check class exists + has spots
2. If valid, save booking to DB
3. Publish `BookingCreatedEvent` (Spring ApplicationEventPublisher)

**Deliverable**: Can create bookings, Class Service spots decrement

---

### Phase 5: Shared Events Library (15 min)
**Goal**: Wire event-driven communication

**Files**:
- `backend/shared/src/main/java/.../BookingCreatedEvent.java`
  ```java
  public class BookingCreatedEvent {
    private Long classId;
    // getters/setters
  }
  ```

**Both services**:
- Add shared library as Maven dependency
- Booking Service: `applicationEventPublisher.publishEvent(new BookingCreatedEvent(classId))`
- Class Service: `@EventListener` on method that decrements spots

**Note**: For true distributed events, you'd need Kafka/RabbitMQ. Spring Events only work in-process. For this demo, accept limitation or add Kafka to docker-compose (adds complexity).

**Deliverable**: Events flow between services (if in same JVM) OR document limitation

---

### Phase 6: Frontend Skeleton (30-45 min)
**Goal**: Basic React + Ionic UI

**Files**:
- `frontend/package.json` - React, Ionic, axios
- `frontend/src/pages/ClassList.jsx` - Fetch + display classes
- `frontend/src/pages/BookingForm.jsx` - Form to create booking

**Features**:
- List all classes (GET /classes)
- Click class → booking form
- Submit booking (POST /bookings)

**Deliverable**: Can book classes via UI

---

## Critical Files Summary

**Docker**:
- `docker-compose.yml` - All services + postgres

**Backend - Class Service**:
- `backend/class-service/pom.xml`
- `backend/class-service/src/main/resources/application.yml`
- `backend/class-service/src/main/java/.../ClassServiceApplication.java`
- `backend/class-service/src/main/java/.../Class.java`
- `backend/class-service/src/main/java/.../ClassController.java`
- `backend/class-service/src/main/java/.../ClassGrpcService.java`

**Backend - Booking Service**:
- `backend/booking-service/pom.xml`
- `backend/booking-service/src/main/resources/application.yml`
- `backend/booking-service/src/main/java/.../BookingServiceApplication.java`
- `backend/booking-service/src/main/java/.../Booking.java`
- `backend/booking-service/src/main/java/.../BookingController.java`
- `backend/booking-service/src/main/java/.../BookingService.java`

**Shared**:
- `backend/shared/proto/class_service.proto`
- `backend/shared/src/main/java/.../BookingCreatedEvent.java`

**Frontend**:
- `frontend/src/pages/ClassList.jsx`
- `frontend/src/pages/BookingForm.jsx`

---

## Verification Plan

**Phase 2 verification**:
```bash
curl -X POST http://localhost:8081/classes \
  -H "Content-Type: application/json" \
  -d '{"name":"Yoga","capacity":10}'

curl http://localhost:8081/classes
```

**Phase 4 verification**:
```bash
curl -X POST http://localhost:8082/bookings \
  -H "Content-Type: application/json" \
  -d '{"classId":1,"userName":"Ali"}'

curl http://localhost:8082/bookings
curl http://localhost:8081/classes/1  # Check availableSpots decreased
```

**Phase 6 verification**:
- Open http://localhost:3000
- See classes list
- Click class, submit booking
- Check DB: `docker exec -it postgres psql -U user -d gym -c "SELECT * FROM booking_service.bookings;"`

---

## Known Limitations (Learning Project)

1. **Spring Events limitation**: Only work in-process, not across services
   - **Fix**: Add Kafka/RabbitMQ to docker-compose (30-60 min extra)
   - **For demo**: Document as known issue or run services in same JVM with shared event bus

2. **No authentication**: Anyone can book anything
   - **Acceptable**: CRUD scope, add later if time

3. **No transaction safety**: Booking could succeed but event fail
   - **Acceptable**: For interview demo, mention as production concern

4. **No frontend error handling**: Happy path only
   - **Acceptable**: CRUD scope

---

## Timeline Estimate
(Note: User is new to Java, first time estimates)

- Phase 1: 30-45 min
- Phase 2: 60-90 min (learning Spring Boot)
- Phase 3: 30 min
- Phase 4: 60 min
- Phase 5: 15 min (or skip if using Kafka)
- Phase 6: 30-45 min

**Total**: 3.5-4.5 hours core work + debugging buffer

**1-day plan**: Focus on Phases 1-4, get backend working. Frontend optional if time tight.