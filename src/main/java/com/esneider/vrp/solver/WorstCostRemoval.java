package com.esneider.vrp.solver;

import com.esneider.vrp.model.DeliveryJob;
import com.esneider.vrp.model.RoutePlan;
import com.esneider.vrp.model.Solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public final class WorstCostRemoval implements DestroyOperator {
    @Override
    public String name() {
        return "worst-cost-removal";
    }

    @Override
    public List<DeliveryJob> remove(Solution solution, int removeCount, Random random) {
        List<JobCost> jobCosts = new ArrayList<>();

        for (RoutePlan route : solution.routes()) {
            List<DeliveryJob> jobs = route.jobs();
            for (int i = 0; i < jobs.size(); i++) {
                DeliveryJob previous = i == 0 ? null : jobs.get(i - 1);
                DeliveryJob current = jobs.get(i);
                DeliveryJob next = i == jobs.size() - 1 ? null : jobs.get(i + 1);
                double localCost = localDistance(route, previous, current, next);
                jobCosts.add(new JobCost(current, localCost));
            }
        }

        jobCosts.sort(Comparator.comparingDouble(JobCost::cost).reversed());
        return jobCosts.stream()
                .limit(removeCount)
                .map(JobCost::job)
                .toList();
    }

    private double localDistance(RoutePlan route, DeliveryJob previous, DeliveryJob current, DeliveryJob next) {
        var from = previous == null ? route.vehicle().depot() : previous.location();
        var to = next == null ? route.vehicle().depot() : next.location();

        double before = from.distanceTo(current.location()) + current.location().distanceTo(to);
        double after = from.distanceTo(to);
        return before - after;
    }

    private record JobCost(DeliveryJob job, double cost) {
    }
}
