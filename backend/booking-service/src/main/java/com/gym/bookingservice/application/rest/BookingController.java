package com.gym.bookingservice.application.rest;

import com.gym.bookingservice.application.dto.BookingResponse;
import com.gym.bookingservice.application.dto.CreateBookingRequest;
import com.gym.bookingservice.application.service.BookingService;
import com.gym.bookingservice.domain.model.Booking;
import com.gym.bookingservice.infrastructure.persistence.BookingJpaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Application Layer: REST Controller
 *
 * Simplified responsibilities:
 * - Entry point for HTTP requests
 * - Validates input (@Valid in DTO)
 * - Orchestrates via BookingService
 * - Uses Spring Data JPA directly for queries
 */
@RestController
@RequestMapping("/bookings")
@Transactional
public class BookingController {

    private final BookingService bookingService;
    private final BookingJpaRepository bookingRepository;

    public BookingController(BookingService bookingService,
                             BookingJpaRepository bookingRepository) {
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
    }

    /**
     * POST /bookings
     * Create a new booking
     *
     * Business flow:
     * 1. Validate input (via @Valid)
     * 2. Call service to create booking (validates class via gRPC)
     * 3. Return booking response
     */
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody CreateBookingRequest request) {
        // Delegate to service for business logic
        Booking booking = bookingService.createBooking(request.getClassId(), request.getUserName());

        // Convert to response DTO
        BookingResponse response = BookingResponse.fromDomain(booking);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /bookings
     * List all bookings
     */
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();

        List<BookingResponse> response = bookings.stream()
                .map(BookingResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /bookings/{id}
     * Get a single booking by ID
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));

        BookingResponse response = BookingResponse.fromDomain(booking);

        return ResponseEntity.ok(response);
    }

    /**
     * Domain exception: Booking not found
     */
    public static class BookingNotFoundException extends RuntimeException {
        public BookingNotFoundException(String message) {
            super(message);
        }
    }
}
