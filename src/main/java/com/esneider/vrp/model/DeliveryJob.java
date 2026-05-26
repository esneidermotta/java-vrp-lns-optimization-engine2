package com.esneider.vrp.model;

import java.util.Objects;

public final class DeliveryJob {
    private final String id;
    private final Location location;
    private final int demand;
    private final int serviceMinutes;
    private final TimeWindow timeWindow;
    private final int priority;

    public DeliveryJob(
            String id,
            Location location,
            int demand,
            int serviceMinutes,
            TimeWindow timeWindow,
            int priority
    ) {
        this.id = Objects.requireNonNull(id);
        this.location = Objects.requireNonNull(location);
        this.demand = demand;
        this.serviceMinutes = serviceMinutes;
        this.timeWindow = Objects.requireNonNull(timeWindow);
        this.priority = priority;
    }

    public String id() {
        return id;
    }

    public Location location() {
        return location;
    }

    public int demand() {
        return demand;
    }

    public int serviceMinutes() {
        return serviceMinutes;
    }

    public TimeWindow timeWindow() {
        return timeWindow;
    }

    public int priority() {
        return priority;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof DeliveryJob job && id.equals(job.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
