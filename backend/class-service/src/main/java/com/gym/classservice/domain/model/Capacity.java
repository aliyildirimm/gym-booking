package com.gym.classservice.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Capacity {

    @Column(name = "total_capacity", nullable = false)
    private int total;

    @Column(name = "available_spots", nullable = false)
    private int available;

    // JPA requires no-arg constructor
    protected Capacity() {
    }

    private Capacity(int total, int available) {
        if (total <= 0) {
            throw new IllegalArgumentException("Total capacity must be positive");
        }
        if (available < 0 || available > total) {
            throw new IllegalArgumentException(
                "Available spots must be between 0 and " + total
            );
        }
        this.total = total;
        this.available = available;
    }

    /**
     * Factory method for creating initial capacity
     */
    public static Capacity of(int total) {
        return new Capacity(total, total); // Initially all spots available
    }


    /**
     * Domain behavior: Reserve a spot
     * Returns a NEW Capacity (immutability)
     */
    public Capacity reserveSpot() {
        if (!hasAvailableSpots()) {
            throw new NoAvailableSpotsException("Class is fully booked");
        }
        return new Capacity(this.total, this.available - 1);
    }

    /**
     * Domain behavior: Release a spot (for cancellations)
     */
    public Capacity releaseSpot() {
        if (available >= total) {
            throw new IllegalStateException("Cannot release spot: already at full capacity");
        }
        return new Capacity(this.total, this.available + 1);
    }

    public boolean hasAvailableSpots() {
        return available > 0;
    }

    public boolean isFull() {
        return available == 0;
    }

    public int getTotal() {
        return total;
    }

    public int getAvailable() {
        return available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Capacity capacity = (Capacity) o;
        return total == capacity.total && available == capacity.available;
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, available);
    }

    @Override
    public String toString() {
        return available + "/" + total;
    }

    /**
     * Domain Exception (part of ubiquitous language)
     */
    public static class NoAvailableSpotsException extends RuntimeException {
        public NoAvailableSpotsException(String message) {
            super(message);
        }
    }
}
