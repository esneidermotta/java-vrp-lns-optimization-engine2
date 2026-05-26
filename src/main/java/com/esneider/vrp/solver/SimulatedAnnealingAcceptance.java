package com.esneider.vrp.solver;

import java.util.Random;

public final class SimulatedAnnealingAcceptance {
    private final double initialTemperature;
    private final double coolingRate;

    public SimulatedAnnealingAcceptance(double initialTemperature, double coolingRate) {
        this.initialTemperature = initialTemperature;
        this.coolingRate = coolingRate;
    }

    public boolean accept(double currentCost, double candidateCost, int iteration, Random random) {
        if (candidateCost < currentCost) {
            return true;
        }

        double temperature = Math.max(0.001, initialTemperature * Math.pow(coolingRate, iteration));
        double probability = Math.exp((currentCost - candidateCost) / temperature);
        return random.nextDouble() < probability;
    }
}
