package com.esneider.vrp.solver;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class AdaptiveOperatorSelector {
    private final Map<String, Double> weights = new LinkedHashMap<>();
    private final List<DestroyOperator> operators;

    public AdaptiveOperatorSelector(List<DestroyOperator> operators) {
        this.operators = List.copyOf(operators);
        for (DestroyOperator operator : operators) {
            weights.put(operator.name(), 1.0);
        }
    }

    public DestroyOperator select(Random random) {
        double totalWeight = weights.values().stream().mapToDouble(Double::doubleValue).sum();
        double ticket = random.nextDouble() * totalWeight;
        double cumulative = 0;

        for (DestroyOperator operator : operators) {
            cumulative += weights.get(operator.name());
            if (ticket <= cumulative) {
                return operator;
            }
        }

        return operators.get(operators.size() - 1);
    }

    public void reward(String operatorName, double reward) {
        weights.computeIfPresent(operatorName, (name, weight) -> Math.max(0.1, weight * 0.95 + reward));
    }

    public Map<String, Double> weights() {
        return Map.copyOf(weights);
    }
}
