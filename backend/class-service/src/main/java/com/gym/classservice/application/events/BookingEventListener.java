package com.gym.classservice.application.events;

import com.gym.classservice.domain.model.GymClass;
import com.gym.classservice.infrastructure.persistence.ClassJpaRepository;
import com.gym.shared.events.BookingCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Event Listener: Handles BookingCreatedEvent
 *
 * IMPORTANT LIMITATION:
 * - Spring @EventListener only works within the same JVM (in-process)
 * - For distributed services, replace with:
 *   - @KafkaListener (Kafka)
 *   - @RabbitListener (RabbitMQ)
 *   - Redis Pub/Sub
 *
 * For this demo, we accept this limitation as services run in separate containers.
 * In production, use a proper message broker.
 */
@Component
public class BookingEventListener {

    private static final Logger log = LoggerFactory.getLogger(BookingEventListener.class);

    private final ClassJpaRepository classRepository;

    public BookingEventListener(ClassJpaRepository classRepository) {
        this.classRepository = classRepository;
    }

    /**
     * Handle BookingCreatedEvent: Decrement available spots
     *
     * NOTE: This will NOT work across separate Spring Boot applications.
     * It only works if both services are in the same JVM.
     */
    @EventListener
    @Transactional
    public void handleBookingCreated(BookingCreatedEvent event) {
        log.info("Received BookingCreatedEvent: {}", event);

        Long classId = event.getClassId();

        // Find the class
        GymClass gymClass = classRepository.findById(classId)
            .orElseThrow(() -> new IllegalStateException(
                "Class not found with id: " + classId + " (event: " + event + ")"
            ));

        // Reserve a spot (domain method)
        gymClass.reserveSpot();

        // Save (spot is decremented)
        classRepository.save(gymClass);

        log.info("Decremented available spots for class {} (booking {})",
            classId, event.getBookingId());
    }
}
