package com.esneider.vrp.constraint;

import com.esneider.vrp.model.RoutePlan;

public final class InsertionResult {
    private final boolean feasible;
    private final RoutePlan route;
    private final double additionalCost;
    private final String reason;

    private InsertionResult(boolean feasible, RoutePlan route, double additionalCost, String reason) {
        this.feasible = feasible;
        this.route = route;
        this.additionalCost = additionalCost;
        this.reason = reason;
    }

    public static InsertionResult feasible(RoutePlan route, double additionalCost) {
        return new InsertionResult(true, route, additionalCost, "ok");
    }

    public static InsertionResult infeasible(String reason) {
        return new InsertionResult(false, null, Double.POSITIVE_INFINITY, reason);
    }

    public boolean feasible() {
        return feasible;
    }

    public RoutePlan route() {
        return route;
    }

    public double additionalCost() {
        return additionalCost;
    }

    public String reason() {
        return reason;
    }
}
