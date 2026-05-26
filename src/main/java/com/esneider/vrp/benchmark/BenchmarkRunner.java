package com.esneider.vrp.benchmark;

import com.esneider.vrp.model.DeliveryJob;
import com.esneider.vrp.model.Location;
import com.esneider.vrp.model.RoutePlan;
import com.esneider.vrp.model.TimeWindow;
import com.esneider.vrp.model.VehicleProfile;
import com.esneider.vrp.solver.RoutingEngine;
import com.esneider.vrp.solver.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BenchmarkRunner {
    public static void main(String[] args) {
        BenchmarkRunner runner = new BenchmarkRunner();
        BenchmarkResult result = runner.runScenario("last-mile-cvrptw-80", 80, 6, 123L);

        System.out.printf(
                "%s jobs=%d routesUsed=%d unassigned=%d cost=%.2f runtimeMs=%d%n",
                result.scenario(),
                result.jobs(),
                result.routesUsed(),
                result.unassigned(),
                result.cost(),
                result.runtimeMillis()
        );
    }

    public BenchmarkResult runScenario(String scenario, int jobCount, int vehicleCount, long seed) {
        Location depot = new Location(50, 50);
        List<VehicleProfile> vehicles = vehicles(vehicleCount, depot);
        List<DeliveryJob> jobs = jobs(jobCount, seed);

        RoutingEngine engine = new RoutingEngine();
        long start = System.currentTimeMillis();
        SearchResult result = engine.solve(jobs, vehicles);
        long runtime = System.currentTimeMillis() - start;

        int routesUsed = (int) result.bestSolution().routes().stream()
                .filter(route -> !route.jobs().isEmpty())
                .count();

        return new BenchmarkResult(
                scenario,
                jobs.size(),
                routesUsed,
                result.bestSolution().unassigned().size(),
                result.bestSolution().cost(),
                runtime
        );
    }

    private List<VehicleProfile> vehicles(int count, Location depot) {
        List<VehicleProfile> vehicles = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int capacity = i % 2 == 0 ? 90 : 120;
            vehicles.add(new VehicleProfile(
                    "vehicle-" + i,
                    depot,
                    capacity,
                    8 * 60,
                    18 * 60,
                    120.0,
                    i % 2 == 0 ? 1.0 : 1.2
            ));
        }
        return vehicles;
    }

    private List<DeliveryJob> jobs(int count, long seed) {
        Random random = new Random(seed);
        List<DeliveryJob> jobs = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int windowStart = 8 * 60 + random.nextInt(360);
            int windowEnd = windowStart + 120 + random.nextInt(180);

            jobs.add(new DeliveryJob(
                    "job-" + i,
                    new Location(random.nextDouble(100), random.nextDouble(100)),
                    5 + random.nextInt(12),
                    5 + random.nextInt(8),
                    new TimeWindow(windowStart, windowEnd),
                    random.nextInt(5)
            ));
        }

        return jobs;
    }
}
