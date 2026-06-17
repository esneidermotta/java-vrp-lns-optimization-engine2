# Java VRP / LNS Optimization Engine

This repository is an anonymized Java routing optimization sample based on previous last-mile delivery planning work.

The original project had a JSprit-based routing core, but this repository keeps the implementation framework-independent so the algorithmic parts are easy to read without private code or customer data. The structure mirrors the same concepts I used in JSprit work: problem modeling, constraint evaluation, insertion cost, destroy/repair operators, acceptance criteria, adaptive operator weighting, and benchmark comparison.

## Problem Modeled

The sample solves a Capacitated Vehicle Routing Problem with Time Windows (CVRPTW-style):

- multiple delivery jobs
- heterogeneous vehicles
- depot start/end
- vehicle capacity
- customer time windows
- service duration
- route duration cost
- unassigned job penalty
- benchmark metrics for runtime and solution quality

The goal is not to produce a commercial solver. The goal is to show how I reason about routing changes before shipping them to a live planning engine.

## Why This Matters

In real routing systems, the first valid solution is usually not the hardest part.

The hard part is controlling second-order effects:

- adding a hard constraint can make insertion feasibility much slower
- a soft penalty can improve lateness while increasing vehicle count
- a more aggressive ruin operator can improve objective value but destabilize runtime
- a new objective can move cost from distance to route count or unassigned jobs
- one local improvement can break performance on larger instances

This project is built around that kind of engineering work.

## Architecture

```text
src/main/java/com/esneider/vrp/
  model/
    DeliveryJob.java
    VehicleProfile.java
    RoutePlan.java
    RouteStop.java
    Solution.java

  constraint/
    InsertionEvaluator.java
    InsertionResult.java

  solver/
    RoutingEngine.java
    InitialSolutionBuilder.java
    LargeNeighborhoodSearch.java
    DestroyOperator.java
    RandomRemoval.java
    WorstCostRemoval.java
    RegretInsertionRepair.java
    AdaptiveOperatorSelector.java
    SimulatedAnnealingAcceptance.java
    SolutionScorer.java

  benchmark/
    BenchmarkRunner.java
    BenchmarkResult.java
```

## Algorithm Flow

1. Build an initial feasible solution with greedy insertion.
2. Select a destroy operator using adaptive weights.
3. Remove jobs from current routes.
4. Repair the partial solution using regret-2 insertion.
5. Accept or reject the candidate using simulated annealing style acceptance.
6. Update operator weights based on improvement.
7. Compare solution quality and runtime against the baseline.

## JSprit Mapping

In a JSprit project, the same ideas map naturally to:

| This repo | JSprit-style concept |
|---|---|
| `DeliveryJob` | Service / Shipment |
| `VehicleProfile` | VehicleImpl / VehicleTypeImpl |
| `InsertionEvaluator` | hard/soft constraint + activity insertion cost |
| `RoutePlan` | VehicleRoute |
| `DestroyOperator` | ruin strategy |
| `RegretInsertionRepair` | insertion strategy |
| `AdaptiveOperatorSelector` | adaptive LNS operator selection |
| `SimulatedAnnealingAcceptance` | acceptor behavior |
| `BenchmarkRunner` | baseline vs modified algorithm experiments |

## Main Code Files

The most useful files to review are:

- `LargeNeighborhoodSearch.java`
- `RegretInsertionRepair.java`
- `InsertionEvaluator.java`
- `BenchmarkRunner.java`

These show the practical routing logic: destroy/repair, feasibility checks, time-window handling, capacity handling, and benchmark reporting.

## Running

```bash
mvn test
mvn -q exec:java -Dexec.mainClass=com.esneider.vrp.benchmark.BenchmarkRunner
```

If your Maven setup does not include the exec plugin, run the benchmark class from the IDE.

## What I Would Add in a Production Solver

- real JSprit integration with custom constraints and state managers
- route-level state caching for capacity, arrival time, slack, and cost deltas
- benchmark dataset loader for Solomon / customer production instances
- multi-objective scoring with weighted distance, time, lateness, vehicle count, and priority
- separate experiments for CVRP, VRPTW, PDPTW, multi-depot, and open-route variants
- regression tests for constraints that previously caused bad insertions
- runtime guardrails for max iterations, max stagnation, and max candidate evaluations

## Repository Purpose

This repository shows how I work on Java routing optimization:

- model the VRP clearly
- keep feasibility checks separate from scoring
- measure changes against baseline cases
- tune destroy and repair behavior carefully
- avoid treating the solver as a black box
- check solution quality and runtime together
