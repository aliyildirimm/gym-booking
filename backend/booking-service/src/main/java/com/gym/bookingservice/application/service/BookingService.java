package com.gym.bookingservice.application.service;

import com.gym.bookingservice.domain.model.Booking;
import com.gym.bookingservice.infrastructure.grpc.ClassServiceGrpcClient;
import com.gym.bookingservice.infrastructure.persistence.BookingJpaRepository;
import com.gym.bookingservice.infrastructure.redis.RedisConfig;
import com.gym.shared.events.BookingCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application Service: Booking orchestration
 *
 * Responsibilities:
 * - Coordinate domain operations
 * - Call external services (gRPC)
 * - Publish events via Redis Pub/Sub
 */
@Service
@Transactional
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingJpaRepository bookingRepository;
    private final ClassServiceGrpcClient classServiceClient;
    private final RedisTemplate<String, BookingCreatedEvent> redisTemplate;

    public BookingService(BookingJpaRepository bookingRepository,
                          ClassServiceGrpcClient classServiceClient,
                          RedisTemplate<String, BookingCreatedEvent> redisTemplate) {
        this.bookingRepository = bookingRepository;
        this.classServiceClient = classServiceClient;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Create a new booking
     *
     * Business logic:
     * 1. Validate class exists and has available spots (via gRPC)
     * 2. Create booking using domain factory
     * 3. Persist booking
     * 4. Publish event to Redis for Class Service to decrement spots
     */
    public Booking createBooking(Long classId, String userName) {
        // 1. Validate via gRPC
        classServiceClient.validateClassAvailability(classId);

        // 2. Create domain object
        Booking booking = Booking.create(classId, userName);

        // 3. Persist
        Booking savedBooking = bookingRepository.save(booking);

        // 4. Publish event to Redis
        BookingCreatedEvent event = new BookingCreatedEvent(
            classId,
            savedBooking.getId(),
            userName
        );

        redisTemplate.convertAndSend(RedisConfig.BOOKING_CREATED_CHANNEL, event);
        log.info("Published BookingCreatedEvent to Redis: classId={}, bookingId={}",
            classId, savedBooking.getId());

        return savedBooking;
    }
}
