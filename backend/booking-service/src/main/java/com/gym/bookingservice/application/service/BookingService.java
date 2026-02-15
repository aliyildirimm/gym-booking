package com.gym.bookingservice.application.service;

import com.gym.bookingservice.domain.model.Booking;
import com.gym.bookingservice.infrastructure.grpc.ClassServiceGrpcClient;
import com.gym.bookingservice.infrastructure.persistence.BookingJpaRepository;
import com.gym.shared.events.BookingCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application Service: Booking orchestration
 *
 * Responsibilities:
 * - Coordinate domain operations
 * - Call external services (gRPC)
 * - Publish events (Spring ApplicationEvent for demo, Kafka/RabbitMQ for production)
 */
@Service
@Transactional
public class BookingService {

    private final BookingJpaRepository bookingRepository;
    private final ClassServiceGrpcClient classServiceClient;
    private final ApplicationEventPublisher eventPublisher;

    public BookingService(BookingJpaRepository bookingRepository,
                          ClassServiceGrpcClient classServiceClient,
                          ApplicationEventPublisher eventPublisher) {
        this.bookingRepository = bookingRepository;
        this.classServiceClient = classServiceClient;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Create a new booking
     *
     * Business logic:
     * 1. Validate class exists and has available spots (via gRPC)
     * 2. Create booking using domain factory
     * 3. Persist booking
     * 4. Publish event for Class Service to decrement spots
     */
    public Booking createBooking(Long classId, String userName) {
        // 1. Validate via gRPC
        classServiceClient.validateClassAvailability(classId);

        // 2. Create domain object
        Booking booking = Booking.create(classId, userName);

        // 3. Persist
        Booking savedBooking = bookingRepository.save(booking);

        // 4. Publish event
        BookingCreatedEvent event = new BookingCreatedEvent(
            classId,
            savedBooking.getId(),
            userName
        );
        eventPublisher.publishEvent(event);

        return savedBooking;
    }
}
