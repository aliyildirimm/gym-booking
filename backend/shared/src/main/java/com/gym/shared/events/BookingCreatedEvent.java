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

    private Long classId;
    private Long bookingId;
    private String userName;
    private Instant timestamp;

    /** For Jackson/Redis deserialization (no-arg constructor required). */
    public BookingCreatedEvent() {
    }

    /** For creating the event when publishing (timestamp set to now). */
    public BookingCreatedEvent(Long classId, Long bookingId, String userName) {
        this(classId, bookingId, userName, Instant.now());
    }

    /** Full constructor (all fields from JSON). */
    public BookingCreatedEvent(Long classId, Long bookingId, String userName, Instant timestamp) {
        this.classId = classId;
        this.bookingId = bookingId;
        this.userName = userName;
        this.timestamp = timestamp;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
