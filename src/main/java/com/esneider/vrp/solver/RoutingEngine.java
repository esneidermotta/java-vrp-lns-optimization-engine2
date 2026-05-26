package com.esneider.vrp.solver;

import com.esneider.vrp.constraint.InsertionEvaluator;
import com.esneider.vrp.model.DeliveryJob;
import com.esneider.vrp.model.Solution;
import com.esneider.vrp.model.VehicleProfile;

import java.util.List;

public final class RoutingEngine {
    private final InitialSolutionBuilder initialSolutionBuilder;
    private final LargeNeighborhoodSearch lns;

    public RoutingEngine() {
        InsertionEvaluator evaluator = new InsertionEvaluator(2.0);
        SolutionScorer scorer = new SolutionScorer(evaluator, 10_000.0);
        RegretInsertionRepair repair = new RegretInsertionRepair(evaluator, scorer);

        this.initialSolutionBuilder = new InitialSolutionBuilder(evaluator, scorer);
        this.lns = new LargeNeighborhoodSearch(
                List.of(new RandomRemoval(), new WorstCostRemoval()),
                repair,
                scorer,
                new SimulatedAnnealingAcceptance(250.0, 0.995),
                750,
                8,
                42L
        );
    }

    public SearchResult solve(List<DeliveryJob> jobs, List<VehicleProfile> vehicles) {
        Solution initial = initialSolutionBuilder.build(jobs, vehicles);
        return lns.improve(initial);
    }
}
