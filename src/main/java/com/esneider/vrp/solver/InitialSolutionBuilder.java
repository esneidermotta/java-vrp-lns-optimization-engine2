package com.esneider.vrp.solver;

import com.esneider.vrp.constraint.InsertionEvaluator;
import com.esneider.vrp.constraint.InsertionResult;
import com.esneider.vrp.model.DeliveryJob;
import com.esneider.vrp.model.RoutePlan;
import com.esneider.vrp.model.Solution;
import com.esneider.vrp.model.VehicleProfile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class InitialSolutionBuilder {
    private final InsertionEvaluator evaluator;
    private final SolutionScorer scorer;

    public InitialSolutionBuilder(InsertionEvaluator evaluator, SolutionScorer scorer) {
        this.evaluator = evaluator;
        this.scorer = scorer;
    }

    public Solution build(List<DeliveryJob> jobs, List<VehicleProfile> vehicles) {
        List<RoutePlan> routes = vehicles.stream().map(RoutePlan::new).collect(java.util.stream.Collectors.toCollection(ArrayList::new));
        List<DeliveryJob> unassigned = new ArrayList<>();

        List<DeliveryJob> sortedJobs = new ArrayList<>(jobs);
        sortedJobs.sort(Comparator.comparingInt(DeliveryJob::priority).reversed());

        for (DeliveryJob job : sortedJobs) {
            BestInsertion best = findBestInsertion(routes, job);
            if (best == null) {
                unassigned.add(job);
            } else {
                routes.set(best.routeIndex(), best.result().route());
            }
        }

        return scorer.withUpdatedCost(new Solution(routes, unassigned, 0));
    }

    private BestInsertion findBestInsertion(List<RoutePlan> routes, DeliveryJob job) {
        BestInsertion best = null;

        for (int routeIndex = 0; routeIndex < routes.size(); routeIndex++) {
            RoutePlan route = routes.get(routeIndex);
            for (int index = 0; index <= route.size(); index++) {
                InsertionResult result = evaluator.evaluate(route, job, index);
                if (!result.feasible()) {
                    continue;
                }

                if (best == null || result.additionalCost() < best.result().additionalCost()) {
                    best = new BestInsertion(routeIndex, result);
                }
            }
        }

        return best;
    }

    private record BestInsertion(int routeIndex, InsertionResult result) {
    }
}
