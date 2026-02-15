package com.gym.classservice.application.rest;

import com.gym.classservice.application.dto.ClassResponse;
import com.gym.classservice.application.dto.CreateClassRequest;
import com.gym.classservice.domain.model.Capacity;
import com.gym.classservice.domain.model.GymClass;
import com.gym.classservice.infrastructure.persistence.ClassJpaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Application Layer: REST Controller
 *
 * Simplified responsibilities:
 * - Entry point for HTTP requests
 * - Validates input (@Valid in DTO)
 * - Orchestrates domain logic
 * - Uses Spring Data JPA directly (no repository abstraction layer)
 */
@RestController
@RequestMapping("/classes")
@Transactional
public class ClassController {

    private final ClassJpaRepository classRepository;

    public ClassController(ClassJpaRepository classRepository) {
        this.classRepository = classRepository;
    }

    @PostMapping
    public ResponseEntity<ClassResponse> createClass(@Valid @RequestBody CreateClassRequest request) {
        // 1. Create value object for capacity
        Capacity capacity = Capacity.of(request.getCapacity());

        // 2. Create aggregate using domain factory method
        GymClass gymClass = GymClass.create(request.getName(), capacity);

        // 3. Persist via Spring Data JPA
        GymClass savedClass = classRepository.save(gymClass);

        // 4. Convert to response DTO
        ClassResponse response = ClassResponse.fromDomain(savedClass);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<ClassResponse>> getAllClasses() {
        List<GymClass> classes = classRepository.findAll();

        List<ClassResponse> response = classes.stream()
                .map(ClassResponse::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /classes/{id}
     * Get a single gym class by ID
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ClassResponse> getClassById(@PathVariable Long id) {
        GymClass gymClass = classRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Class not found with id: " + id
                ));

        ClassResponse response = ClassResponse.fromDomain(gymClass);

        return ResponseEntity.ok(response);
    }
}
