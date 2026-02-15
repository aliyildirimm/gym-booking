package com.gym.bookingservice.application.service;

import com.gym.bookingservice.domain.model.Booking;
import com.gym.bookingservice.infrastructure.grpc.ClassServiceGrpcClient;
import com.gym.bookingservice.infrastructure.persistence.BookingJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application Service: Booking orchestration
 *
 * Responsibilities:
 * - Coordinate domain operations
 * - Call external services (gRPC)
 * - Publish events (future: Kafka/RabbitMQ)
 */
@Service
@Transactional
public class BookingService {

    private final BookingJpaRepository bookingRepository;
    private final ClassServiceGrpcClient classServiceClient;

    public BookingService(BookingJpaRepository bookingRepository,
                          ClassServiceGrpcClient classServiceClient) {
        this.bookingRepository = bookingRepository;
        this.classServiceClient = classServiceClient;
    }

    /**
     * Create a new booking
     *
     * Business logic:
     * 1. Validate class exists and has available spots (via gRPC)
     * 2. Create booking using domain factory
     * 3. Persist booking
     * 4. Publish event (TODO: add event publishing when event infrastructure is ready)
     */
    public Booking createBooking(Long classId, String userName) {
        // 1. Validate via gRPC
        classServiceClient.validateClassAvailability(classId);

        // 2. Create domain object
        Booking booking = Booking.create(classId, userName);

        // 3. Persist
        Booking savedBooking = bookingRepository.save(booking);

        // 4. Publish event (TODO: implement when event infrastructure is ready)
        // applicationEventPublisher.publishEvent(new BookingCreatedEvent(classId));

        return savedBooking;
    }
}
