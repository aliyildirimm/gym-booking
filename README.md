# Gym Booking System

A microservices-based gym class booking system for learning Spring Boot, gRPC, and event-driven architecture.

## Quick Start

### Phase 1: Foundation Setup

Start the PostgreSQL database:
```bash
docker-compose up -d
```

Verify PostgreSQL is running:
```bash
docker exec -it gym-postgres psql -U user -d gym -c "\dn"
```

You should see the `class_service` and `booking_service` schemas.

### Running Services Locally (for development)

**Class Service:**
```bash
cd backend/class-service
../../mvnw spring-boot:run
```

**Booking Service:**
```bash
cd backend/booking-service
../../mvnw spring-boot:run
```

**Frontend:**
```bash
cd frontend
npm install
npm start
```

## Project Structure

```
gym-booking/
├── docker-compose.yml          # PostgreSQL container
├── init.sql                    # Database schema initialization
├── backend/
│   ├── pom.xml                # Parent Maven configuration
│   ├── shared/                # Shared library (events, proto)
│   ├── class-service/         # Port 8081 (REST), 9091 (gRPC)
│   └── booking-service/       # Port 8082 (REST)
└── frontend/                  # Port 3000 (React + Ionic)
```

## Next Steps

See `plan.md` for the full implementation plan. Phase 2 will implement the Class Service CRUD operations.
