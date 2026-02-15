package com.gym.bookingservice.infrastructure.persistence;

import com.gym.bookingservice.domain.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Infrastructure Layer: JPA Repository for Bookings
 *
 * Simplified approach: Spring Data JPA works directly with domain model
 */
@Repository
public interface BookingJpaRepository extends JpaRepository<Booking, Long> {

    /**
     * Find all bookings for a specific class
     */
    List<Booking> findByClassId(Long classId);

    /**
     * Find all bookings for a specific user
     */
    List<Booking> findByUserName(String userName);
}
