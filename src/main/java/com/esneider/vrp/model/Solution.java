package com.esneider.vrp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Solution {
    private final List<RoutePlan> routes;
    private final List<DeliveryJob> unassigned;
    private final double cost;

    public Solution(List<RoutePlan> routes, List<DeliveryJob> unassigned, double cost) {
        this.routes = new ArrayList<>(routes);
        this.unassigned = new ArrayList<>(unassigned);
        this.cost = cost;
    }

    public List<RoutePlan> routes() {
        return Collections.unmodifiableList(routes);
    }

    public List<DeliveryJob> unassigned() {
        return Collections.unmodifiableList(unassigned);
    }

    public double cost() {
        return cost;
    }

    public Solution withCost(double newCost) {
        return new Solution(routes, unassigned, newCost);
    }
}
