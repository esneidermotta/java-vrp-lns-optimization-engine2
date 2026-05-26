package com.esneider.vrp.solver;

import com.esneider.vrp.constraint.InsertionEvaluator;
import com.esneider.vrp.constraint.InsertionResult;
import com.esneider.vrp.model.DeliveryJob;
import com.esneider.vrp.model.RoutePlan;
import com.esneider.vrp.model.Solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class RegretInsertionRepair {
    private final InsertionEvaluator evaluator;
    private final SolutionScorer scorer;

    public RegretInsertionRepair(InsertionEvaluator evaluator, SolutionScorer scorer) {
        this.evaluator = evaluator;
        this.scorer = scorer;
    }

    public Solution repair(Solution partial, List<DeliveryJob> removedJobs) {
        List<RoutePlan> routes = new ArrayList<>(partial.routes());
        List<DeliveryJob> pending = new ArrayList<>(partial.unassigned());
        pending.addAll(removedJobs);
        List<DeliveryJob> stillUnassigned = new ArrayList<>();

        while (!pending.isEmpty()) {
            List<RegretCandidate> candidates = pending.stream()
                    .map(job -> bestRegret(routes, job))
                    .filter(candidate -> candidate.bestInsertion() != null)
                    .sorted(Comparator.comparingDouble(RegretCandidate::regret).reversed())
                    .toList();

            if (candidates.isEmpty()) {
                stillUnassigned.addAll(pending);
                break;
            }

            RegretCandidate selected = candidates.get(0);
            routes.set(selected.bestInsertion().routeIndex(), selected.bestInsertion().result().route());
            pending.remove(selected.job());
        }

        return scorer.withUpdatedCost(new Solution(routes, stillUnassigned, 0));
    }

    private RegretCandidate bestRegret(List<RoutePlan> routes, DeliveryJob job) {
        List<BestInsertion> feasible = new ArrayList<>();

        for (int routeIndex = 0; routeIndex < routes.size(); routeIndex++) {
            RoutePlan route = routes.get(routeIndex);
            for (int index = 0; index <= route.size(); index++) {
                InsertionResult result = evaluator.evaluate(route, job, index);
                if (result.feasible()) {
                    feasible.add(new BestInsertion(routeIndex, result));
                }
            }
        }

        feasible.sort(Comparator.comparingDouble(item -> item.result().additionalCost()));

        if (feasible.isEmpty()) {
            return new RegretCandidate(job, null, Double.NEGATIVE_INFINITY);
        }

        double best = feasible.get(0).result().additionalCost();
        double second = feasible.size() > 1 ? feasible.get(1).result().additionalCost() : best + 500.0;
        return new RegretCandidate(job, feasible.get(0), second - best);
    }

    private record RegretCandidate(DeliveryJob job, BestInsertion bestInsertion, double regret) {
    }

    private record BestInsertion(int routeIndex, InsertionResult result) {
    }
}
