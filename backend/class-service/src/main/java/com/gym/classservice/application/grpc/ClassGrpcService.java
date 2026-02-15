package com.gym.classservice.application.grpc;

import com.gym.classservice.domain.model.GymClass;
import com.gym.classservice.infrastructure.persistence.ClassJpaRepository;
import com.gym.shared.grpc.ClassServiceGrpc;
import com.gym.shared.grpc.GetClassRequest;
import com.gym.shared.grpc.GetClassResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * gRPC Service Implementation for Class Service
 *
 * Application Layer: Entry point for gRPC requests
 * Converts between gRPC proto messages and domain objects
 */
@GrpcService
public class ClassGrpcService extends ClassServiceGrpc.ClassServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(ClassGrpcService.class);

    private final ClassJpaRepository classRepository;

    public ClassGrpcService(ClassJpaRepository classRepository) {
        this.classRepository = classRepository;
    }

    @Override
    public void getClass(GetClassRequest request, StreamObserver<GetClassResponse> responseObserver) {
        logger.info("gRPC GetClass called for id: {}", request.getId());

        Optional<GymClass> gymClassOpt = classRepository.findById(request.getId());

        GetClassResponse response;
        if (gymClassOpt.isPresent()) {
            GymClass gymClass = gymClassOpt.get();
            response = GetClassResponse.newBuilder()
                    .setId(gymClass.getId())
                    .setName(gymClass.getName())
                    .setTotalCapacity(gymClass.getCapacity().getTotal())
                    .setAvailableSpots(gymClass.getCapacity().getAvailable())
                    .setExists(true)
                    .build();
            logger.info("Class found: {} (available: {})", gymClass.getName(), gymClass.getCapacity().getAvailable());
        } else {
            response = GetClassResponse.newBuilder()
                    .setExists(false)
                    .build();
            logger.warn("Class with id {} not found", request.getId());
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
