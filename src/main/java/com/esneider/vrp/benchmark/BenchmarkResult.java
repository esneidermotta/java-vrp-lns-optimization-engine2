package com.esneider.vrp.benchmark;

public record BenchmarkResult(
        String scenario,
        int jobs,
        int routesUsed,
        int unassigned,
        double cost,
        long runtimeMillis
) {
}
