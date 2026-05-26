package com.esneider.vrp.solver;

import com.esneider.vrp.constraint.InsertionEvaluator;
import com.esneider.vrp.model.RoutePlan;
import com.esneider.vrp.model.Solution;

public final class SolutionScorer {
    private final InsertionEvaluator evaluator;
    private final double unassignedPenalty;

    public SolutionScorer(InsertionEvaluator evaluator, double unassignedPenalty) {
        this.evaluator = evaluator;
        this.unassignedPenalty = unassignedPenalty;
    }

    public double score(Solution solution) {
        double cost = solution.unassigned().size() * unassignedPenalty;

        for (RoutePlan route : solution.routes()) {
            if (!route.jobs().isEmpty()) {
                cost += route.vehicle().fixedCost();
                cost += evaluator.distance(route) * route.vehicle().distanceCost();
            }
        }

        return cost;
    }

    public Solution withUpdatedCost(Solution solution) {
        return solution.withCost(score(solution));
    }
}
