package com.esneider.vrp.solver;

import com.esneider.vrp.benchmark.BenchmarkRunner;
import com.esneider.vrp.benchmark.BenchmarkResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

final class RegretInsertionRepairTest {
    @Test
    void keepsMostJobsAssignedInMediumScenario() {
        BenchmarkRunner runner = new BenchmarkRunner();

        BenchmarkResult result = runner.runScenario("test-cvrptw-50", 50, 6, 99L);

        assertTrue(result.unassigned() < 15);
    }
}
