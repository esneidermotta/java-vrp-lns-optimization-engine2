package com.esneider.vrp.model;

import java.util.Objects;

public final class VehicleProfile {
    private final String id;
    private final Location depot;
    private final int capacity;
    private final int shiftStartMinute;
    private final int shiftEndMinute;
    private final double fixedCost;
    private final double distanceCost;

    public VehicleProfile(
            String id,
            Location depot,
            int capacity,
            int shiftStartMinute,
            int shiftEndMinute,
            double fixedCost,
            double distanceCost
    ) {
        this.id = Objects.requireNonNull(id);
        this.depot = Objects.requireNonNull(depot);
        this.capacity = capacity;
        this.shiftStartMinute = shiftStartMinute;
        this.shiftEndMinute = shiftEndMinute;
        this.fixedCost = fixedCost;
        this.distanceCost = distanceCost;
    }

    public String id() {
        return id;
    }

    public Location depot() {
        return depot;
    }

    public int capacity() {
        return capacity;
    }

    public int shiftStartMinute() {
        return shiftStartMinute;
    }

    public int shiftEndMinute() {
        return shiftEndMinute;
    }

    public double fixedCost() {
        return fixedCost;
    }

    public double distanceCost() {
        return distanceCost;
    }
}
