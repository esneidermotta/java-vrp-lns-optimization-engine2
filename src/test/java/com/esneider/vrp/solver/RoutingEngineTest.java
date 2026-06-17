package com.esneider.vrp.solver;

import java.util.List;
import com.esneider.vrp.model.DeliveryJob;
import com.esneider.vrp.model.Location;
import com.esneider.vrp.model.TimeWindow;
import com.esneider.vrp.benchmark.BenchmarkRunner;
import com.esneider.vrp.benchmark.BenchmarkResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void rejectsEmptyJobList() {
        RoutingEngine engine = new RoutingEngine();

        assertThrows(
                IllegalArgumentException.class,
                () -> engine.solve(List.of(), List.of())
        );
    }

    @Test
    void rejectsNullJobList() {
        RoutingEngine engine = new RoutingEngine();

        assertThrows(
                IllegalArgumentException.class,
                () -> engine.solve(null, List.of())
        );
    }

    @Test
    void rejectsNullVehicleList() {
        RoutingEngine engine = new RoutingEngine();

        assertThrows(
                IllegalArgumentException.class,
                () -> engine.solve(List.of(), null)
        );
    }
    @Test
    void rejectsEmptyVehicleList() {
        RoutingEngine engine = new RoutingEngine();

        DeliveryJob job = new DeliveryJob(
                "job-1",
                new Location(0.0, 0.0),
                1,
                10,
                new TimeWindow(0, 100),
                1
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> engine.solve(List.of(job), List.of())
        );
    }
}