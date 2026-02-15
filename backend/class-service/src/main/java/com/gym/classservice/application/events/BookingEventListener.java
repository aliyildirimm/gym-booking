package com.gym.classservice.application.events;

import com.gym.classservice.domain.model.GymClass;
import com.gym.classservice.infrastructure.persistence.ClassJpaRepository;
import com.gym.shared.events.BookingCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Event Listener: Handles BookingCreatedEvent from Redis Pub/Sub
 *
 * This listener is invoked by Redis MessageListenerAdapter when a message
 * is published to the "booking-created" channel.
 */
@Component
public class BookingEventListener {

    private static final Logger log = LoggerFactory.getLogger(BookingEventListener.class);

    private final ClassJpaRepository classRepository;

    public BookingEventListener(ClassJpaRepository classRepository) {
        this.classRepository = classRepository;
    }

    /**
     * Handle BookingCreatedEvent from Redis: Decrement available spots
     *
     * This method is called by Redis MessageListenerAdapter.
     * Method name must match the one configured in RedisConfig.
     */
    @Transactional
    public void handleBookingCreated(BookingCreatedEvent event) {
        log.info("Received BookingCreatedEvent from Redis: classId={}, bookingId={}",
            event.getClassId(), event.getBookingId());

        Long classId = event.getClassId();

        // Find the class
        GymClass gymClass = classRepository.findById(classId)
            .orElseThrow(() -> new IllegalStateException(
                "Class not found with id: " + classId
            ));

        // Reserve a spot (domain method)
        gymClass.reserveSpot();

        // Save (spot is decremented)
        classRepository.save(gymClass);

        log.info("Successfully decremented available spots for class {} (booking {})",
            classId, event.getBookingId());
    }
}
