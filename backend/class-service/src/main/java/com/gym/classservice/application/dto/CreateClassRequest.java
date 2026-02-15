package com.gym.classservice.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class CreateClassRequest {

    @NotBlank(message = "Class name is required")
    @Size(max = 100, message = "Class name cannot exceed 100 characters")
    private String name;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    public CreateClassRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
