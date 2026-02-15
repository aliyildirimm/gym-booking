package com.gym.classservice.application.dto;

import com.gym.classservice.domain.model.GymClass;

/**
 * Response DTO for Class
 * Simple data structure for JSON serialization
 */
public class ClassResponse {

    private Long id;
    private String name;
    private int totalCapacity;
    private int availableSpots;

    // Factory method: Create from domain model
    public static ClassResponse fromDomain(GymClass gymClass) {
        ClassResponse response = new ClassResponse();
        response.id = gymClass.getId();
        response.name = gymClass.getName();
        response.totalCapacity = gymClass.getCapacity().getTotal();
        response.availableSpots = gymClass.getCapacity().getAvailable();
        return response;
    }

    // Getters for JSON serialization
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public int getAvailableSpots() {
        return availableSpots;
    }
}
