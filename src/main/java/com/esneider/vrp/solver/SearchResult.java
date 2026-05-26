package com.esneider.vrp.solver;

import com.esneider.vrp.model.Solution;

import java.util.Map;

public record SearchResult(Solution bestSolution, Map<String, Double> operatorWeights) {
}
