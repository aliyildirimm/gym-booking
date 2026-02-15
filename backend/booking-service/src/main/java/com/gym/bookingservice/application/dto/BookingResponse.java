package com.gym.bookingservice.application.dto;

import com.gym.bookingservice.domain.model.Booking;

import java.time.LocalDateTime;

/**
 * Response DTO for Booking
 * Simple data structure for JSON serialization
 */
public class BookingResponse {

    private Long id;
    private Long classId;
    private String userName;
    private LocalDateTime createdAt;

    // Factory method: Create from domain model
    public static BookingResponse fromDomain(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.id = booking.getId();
        response.classId = booking.getClassId();
        response.userName = booking.getUserName();
        response.createdAt = booking.getCreatedAt();
        return response;
    }

    // Getters for JSON serialization
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
}
