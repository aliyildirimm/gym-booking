package com.gym.shared.events;

import java.io.Serializable;
import java.time.Instant;

/**
 * Domain Event: BookingCreatedEvent
 *
 * Published by Booking Service when a new booking is created.
 * Consumed by Class Service to decrement available spots.
 *
 * IMPORTANT LIMITATION:
 * - Spring ApplicationEvent only works within the same JVM (in-process)
 * - For distributed services, use a message broker:
 *   - Kafka (recommended for production)
 *   - RabbitMQ
 *   - Redis Pub/Sub
 *
 * For this demo, we accept this limitation.
 */
public class BookingCreatedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long classId;
    private final Long bookingId;
    private final String userName;
    private final Instant timestamp;

    public BookingCreatedEvent(Long classId, Long bookingId, String userName) {
        this.classId = classId;
        this.bookingId = bookingId;
        this.userName = userName;
        this.timestamp = Instant.now();
    }

    public Long getClassId() {
        return classId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getUserName() {
        return userName;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
