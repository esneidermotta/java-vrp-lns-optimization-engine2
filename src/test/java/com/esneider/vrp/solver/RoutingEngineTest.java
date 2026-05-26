package com.esneider.vrp.solver;

import com.esneider.vrp.benchmark.BenchmarkRunner;
import com.esneider.vrp.benchmark.BenchmarkResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

final class RoutingEngineTest {
    @Test
    void solvesSmallCvrptwScenario() {
        BenchmarkRunner runner = new BenchmarkRunner();

        BenchmarkResult result = runner.runScenario("test-cvrptw-30", 30, 4, 7L);

        assertTrue(result.routesUsed() > 0);
        assertTrue(result.cost() > 0);
        assertTrue(result.runtimeMillis() < 10_000);
    }
}
