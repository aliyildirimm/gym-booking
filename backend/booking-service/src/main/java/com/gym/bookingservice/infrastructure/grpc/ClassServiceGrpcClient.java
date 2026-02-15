package com.gym.bookingservice.infrastructure.grpc;

import com.gym.shared.grpc.ClassServiceGrpc;
import com.gym.shared.grpc.GetClassRequest;
import com.gym.shared.grpc.GetClassResponse;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

/**
 * Infrastructure Layer: gRPC Client for Class Service
 *
 * Encapsulates communication with Class Service
 */
@Component
public class ClassServiceGrpcClient {

    @GrpcClient("class-service")
    private ClassServiceGrpc.ClassServiceBlockingStub classServiceStub;

    /**
     * Validate that a class exists and has available spots
     *
     * @param classId The ID of the class to validate
     * @throws ClassNotFoundException if class doesn't exist
     * @throws ClassFullException if class has no available spots
     * @throws GrpcCommunicationException if gRPC call fails
     */
    public void validateClassAvailability(Long classId) {
        try {
            GetClassRequest request = GetClassRequest.newBuilder()
                    .setId(classId)
                    .build();

            GetClassResponse response = classServiceStub.getClass(request);

            // Check if class exists
            if (!response.getExists()) {
                throw new ClassNotFoundException("Class with ID " + classId + " does not exist");
            }

            // Check if class has available spots
            if (response.getAvailableSpots() <= 0) {
                throw new ClassFullException("Class with ID " + classId + " is fully booked");
            }

        } catch (StatusRuntimeException e) {
            throw new GrpcCommunicationException(
                    "Failed to communicate with Class Service: " + e.getMessage(), e
            );
        }
    }

    /**
     * Domain exception: Class not found
     */
    public static class ClassNotFoundException extends RuntimeException {
        public ClassNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Domain exception: Class is full
     */
    public static class ClassFullException extends RuntimeException {
        public ClassFullException(String message) {
            super(message);
        }
    }

    /**
     * Infrastructure exception: gRPC communication error
     */
    public static class GrpcCommunicationException extends RuntimeException {
        public GrpcCommunicationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
