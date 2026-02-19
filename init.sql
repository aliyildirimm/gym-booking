-- Create schemas for microservices
CREATE SCHEMA IF NOT EXISTS class_service;
CREATE SCHEMA IF NOT EXISTS booking_service;
CREATE SCHEMA IF NOT EXISTS auth_service;

-- Grant permissions
GRANT ALL PRIVILEGES ON SCHEMA class_service TO "user";
GRANT ALL PRIVILEGES ON SCHEMA booking_service TO "user";
GRANT ALL PRIVILEGES ON SCHEMA auth_service TO "user";