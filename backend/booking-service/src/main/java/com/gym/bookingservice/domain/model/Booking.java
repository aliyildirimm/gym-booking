package com.gym.bookingservice.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Booking Aggregate Root
 *
 * Simplified DDD approach:
 * - JPA entity with domain behavior (no separate persistence model)
 * - String userName with validation in DTO layer
 * - Long classId (reference to GymClass in other service)
 * - Timestamps for audit trail
 */
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_id", nullable = false)
    private Long classId;

    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // JPA requires no-arg constructor
    protected Booking() {
    }

    // Package-private constructor for controlled creation
    private Booking(Long classId, String userName) {
        this.classId = classId;
        this.userName = userName;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Factory method: Create new booking
     */
    public static Booking create(Long classId, String userName) {
        return new Booking(classId, userName);
    }

    // Getters (no setters! Bookings are immutable after creation)

    public Long getId() {
        return id;
    }

    public Long getClassId() {
        return classId;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", classId=" + classId +
                ", userName='" + userName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
