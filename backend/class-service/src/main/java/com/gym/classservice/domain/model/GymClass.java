package com.gym.classservice.domain.model;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * GymClass Aggregate Root
 *
 * Simplified DDD approach:
 * - JPA entity with domain behavior (no separate persistence model)
 * - String name with validation in DTO layer
 * - Embedded Capacity value object (has real behavior)
 * - No domain events infrastructure (YAGNI for simple CRUD)
 */
@Entity
@Table(name = "classes")
public class GymClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Embedded
    private Capacity capacity;

    // JPA requires no-arg constructor
    protected GymClass() {
    }

    // Package-private constructor for controlled creation
    private GymClass(String name, Capacity capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    /**
     * Factory method: Create new class
     */
    public static GymClass create(String name, Capacity capacity) {
        return new GymClass(name, capacity);
    }

    /**
     * Domain behavior: Reserve a spot for a booking
     */
    public void reserveSpot() {
        this.capacity = this.capacity.reserveSpot();
    }

    /**
     * Domain behavior: Release a spot (for cancellations)
     */
    public void releaseSpot() {
        this.capacity = this.capacity.releaseSpot();
    }

    /**
     * Query method: Check if class is available
     */
    public boolean isAvailable() {
        return capacity.hasAvailableSpots();
    }

    // Getters (no setters! State changes only through domain methods)

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GymClass gymClass = (GymClass) o;
        return Objects.equals(id, gymClass.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "GymClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
