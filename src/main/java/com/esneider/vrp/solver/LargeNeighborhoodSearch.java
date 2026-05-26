package com.esneider.vrp.solver;

import com.esneider.vrp.model.DeliveryJob;
import com.esneider.vrp.model.RoutePlan;
import com.esneider.vrp.model.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class LargeNeighborhoodSearch {
    private final List<DestroyOperator> destroyOperators;
    private final RegretInsertionRepair repair;
    private final SolutionScorer scorer;
    private final SimulatedAnnealingAcceptance acceptance;
    private final int iterations;
    private final int removeCount;
    private final Random random;

    public LargeNeighborhoodSearch(
            List<DestroyOperator> destroyOperators,
            RegretInsertionRepair repair,
            SolutionScorer scorer,
            SimulatedAnnealingAcceptance acceptance,
            int iterations,
            int removeCount,
            long seed
    ) {
        this.destroyOperators = List.copyOf(destroyOperators);
        this.repair = repair;
        this.scorer = scorer;
        this.acceptance = acceptance;
        this.iterations = iterations;
        this.removeCount = removeCount;
        this.random = new Random(seed);
    }

    public SearchResult improve(Solution initial) {
        AdaptiveOperatorSelector selector = new AdaptiveOperatorSelector(destroyOperators);
        Solution current = scorer.withUpdatedCost(initial);
        Solution best = current;

        for (int iteration = 0; iteration < iterations; iteration++) {
            DestroyOperator operator = selector.select(random);
            List<DeliveryJob> removed = operator.remove(current, removeCount, random);
            Solution partial = removeJobs(current, removed);
            Solution candidate = repair.repair(partial, removed);

            boolean accepted = acceptance.accept(current.cost(), candidate.cost(), iteration, random);
            if (accepted) {
                current = candidate;
            }

            if (candidate.cost() < best.cost()) {
                best = candidate;
                selector.reward(operator.name(), 3.0);
            } else if (accepted) {
                selector.reward(operator.name(), 1.0);
            } else {
                selector.reward(operator.name(), 0.05);
            }
        }

        return new SearchResult(best, selector.weights());
    }

    private Solution removeJobs(Solution solution, List<DeliveryJob> removed) {
        List<RoutePlan> routes = new ArrayList<>();
        for (RoutePlan route : solution.routes()) {
            RoutePlan updated = route;
            for (DeliveryJob job : removed) {
                updated = updated.remove(job);
            }
            routes.add(updated);
        }

        return scorer.withUpdatedCost(new Solution(routes, new ArrayList<>(), 0));
    }
}
