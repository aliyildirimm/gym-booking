package com.gym.classservice.infrastructure.persistence;

import com.gym.classservice.domain.model.GymClass;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA Repository
 * Simplified: Works directly with GymClass domain model
 * Spring Data generates all CRUD operations
 */
public interface ClassJpaRepository extends JpaRepository<GymClass, Long> {
}
