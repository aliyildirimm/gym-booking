package com.gym.bookingservice.application.rest;

import com.gym.bookingservice.infrastructure.grpc.ClassServiceGrpcClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler
 *
 * Translates domain and infrastructure exceptions to HTTP responses
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle validation errors from @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse response = new ErrorResponse(
                "Validation failed",
                errors.toString(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle class not found (from gRPC client)
     */
    @ExceptionHandler(ClassServiceGrpcClient.ClassNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClassNotFound(ClassServiceGrpcClient.ClassNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                "Class not found",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handle class full (from gRPC client)
     */
    @ExceptionHandler(ClassServiceGrpcClient.ClassFullException.class)
    public ResponseEntity<ErrorResponse> handleClassFull(ClassServiceGrpcClient.ClassFullException ex) {
        ErrorResponse response = new ErrorResponse(
                "Class is fully booked",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handle booking not found
     */
    @ExceptionHandler(BookingController.BookingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookingNotFound(BookingController.BookingNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                "Booking not found",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handle gRPC communication errors
     */
    @ExceptionHandler(ClassServiceGrpcClient.GrpcCommunicationException.class)
    public ResponseEntity<ErrorResponse> handleGrpcCommunication(ClassServiceGrpcClient.GrpcCommunicationException ex) {
        ErrorResponse response = new ErrorResponse(
                "Service communication error",
                "Unable to communicate with Class Service. Please try again later.",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    /**
     * Handle all other unexpected errors
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(Exception ex) {
        ErrorResponse response = new ErrorResponse(
                "Internal server error",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Error response structure
     */
    public static class ErrorResponse {
        private String error;
        private String message;
        private LocalDateTime timestamp;

        public ErrorResponse(String error, String message, LocalDateTime timestamp) {
            this.error = error;
            this.message = message;
            this.timestamp = timestamp;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
}
