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


## Tech Stack Details

### Backend (Spring Boot 3.x)
- **Framework**: Spring Boot
- **Database**: Spring Data JPA + PostgreSQL
- **gRPC**: `net.devh:grpc-spring-boot-starter`
- **Build**: Maven
- **Java Version**: 17 or 21

### Frontend
- **Framework**: React
- **UI Library**: Ionic
- **HTTP Client**: Axios
- **Build**: Vite or Create React App

### Infrastructure
- **Docker Compose**: PostgreSQL + 2 Java services + frontend
- **Database Init**: SQL scripts in docker-compose volumes

## Monorepo Structure
```
gym-booking/
├── plan.md                      # Implementation plan (this reference)
├── CLAUDE.md                    # This file
├── docker-compose.yml
├── backend/
│   ├── class-service/
│   │   ├── pom.xml
│   │   └── src/main/
│   │       ├── java/com/gym/classservice/
│   │       │   ├── ClassServiceApplication.java
│   │       │   ├── Class.java
│   │       │   ├── ClassRepository.java
│   │       │   ├── ClassController.java
│   │       │   └── ClassGrpcService.java
│   │       └── resources/
│   │           └── application.yml
│   ├── booking-service/
│   │   ├── pom.xml
│   │   └── src/main/
│   │       ├── java/com/gym/bookingservice/
│   │       │   ├── BookingServiceApplication.java
│   │       │   ├── Booking.java
│   │       │   ├── BookingRepository.java
│   │       │   ├── BookingController.java
│   │       │   └── BookingService.java
│   │       └── resources/
│   │           └── application.yml
│   └── shared/
│       ├── pom.xml
│       ├── proto/
│       │   └── class_service.proto
│       └── src/main/java/com/gym/shared/
│           └── BookingCreatedEvent.java
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
