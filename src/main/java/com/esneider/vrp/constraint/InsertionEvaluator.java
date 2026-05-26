package com.esneider.vrp.constraint;

import com.esneider.vrp.model.DeliveryJob;
import com.esneider.vrp.model.Location;
import com.esneider.vrp.model.RoutePlan;
import com.esneider.vrp.model.VehicleProfile;

import java.util.List;

public final class InsertionEvaluator {
    private final double minutesPerDistanceUnit;

    public InsertionEvaluator(double minutesPerDistanceUnit) {
        this.minutesPerDistanceUnit = minutesPerDistanceUnit;
    }

    public InsertionResult evaluate(RoutePlan route, DeliveryJob job, int index) {
        if (index < 0 || index > route.jobs().size()) {
            return InsertionResult.infeasible("invalid_index");
        }

        RoutePlan candidate = route.insert(index, job);
        Feasibility feasibility = check(candidate);

        if (!feasibility.feasible()) {
            return InsertionResult.infeasible(feasibility.reason());
        }

        double delta = distance(candidate) - distance(route);
        return InsertionResult.feasible(candidate, delta * route.vehicle().distanceCost());
    }

    public Feasibility check(RoutePlan route) {
        VehicleProfile vehicle = route.vehicle();
        List<DeliveryJob> jobs = route.jobs();

        int load = 0;
        int time = vehicle.shiftStartMinute();
        Location previous = vehicle.depot();

        for (DeliveryJob job : jobs) {
            load += job.demand();
            if (load > vehicle.capacity()) {
                return new Feasibility(false, "capacity_exceeded");
            }

            time += travelMinutes(previous, job.location());
            time = job.timeWindow().waitUntilOpen(time);

            if (!job.timeWindow().contains(time)) {
                return new Feasibility(false, "time_window_missed");
            }

            time += job.serviceMinutes();
            previous = job.location();
        }

        time += travelMinutes(previous, vehicle.depot());
        if (time > vehicle.shiftEndMinute()) {
            return new Feasibility(false, "vehicle_shift_exceeded");
        }

        return new Feasibility(true, "ok");
    }

    public double distance(RoutePlan route) {
        Location previous = route.vehicle().depot();
        double total = 0;

        for (DeliveryJob job : route.jobs()) {
            total += previous.distanceTo(job.location());
            previous = job.location();
        }

        total += previous.distanceTo(route.vehicle().depot());
        return total;
    }

    private int travelMinutes(Location from, Location to) {
        return (int) Math.ceil(from.distanceTo(to) * minutesPerDistanceUnit);
    }

    public record Feasibility(boolean feasible, String reason) {
    }
}
