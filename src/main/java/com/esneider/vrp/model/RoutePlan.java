package com.esneider.vrp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RoutePlan {
    private final VehicleProfile vehicle;
    private final List<DeliveryJob> jobs;

    public RoutePlan(VehicleProfile vehicle) {
        this(vehicle, new ArrayList<>());
    }

    private RoutePlan(VehicleProfile vehicle, List<DeliveryJob> jobs) {
        this.vehicle = vehicle;
        this.jobs = jobs;
    }

    public VehicleProfile vehicle() {
        return vehicle;
    }

    public List<DeliveryJob> jobs() {
        return Collections.unmodifiableList(jobs);
    }

    public int size() {
        return jobs.size();
    }

    public RoutePlan insert(int index, DeliveryJob job) {
        List<DeliveryJob> copy = new ArrayList<>(jobs);
        copy.add(index, job);
        return new RoutePlan(vehicle, copy);
    }

    public RoutePlan remove(DeliveryJob job) {
        List<DeliveryJob> copy = new ArrayList<>(jobs);
        copy.remove(job);
        return new RoutePlan(vehicle, copy);
    }

    public RoutePlan replaceJobs(List<DeliveryJob> replacement) {
        return new RoutePlan(vehicle, new ArrayList<>(replacement));
    }
}
