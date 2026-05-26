package com.esneider.vrp.solver;

import com.esneider.vrp.model.DeliveryJob;
import com.esneider.vrp.model.RoutePlan;
import com.esneider.vrp.model.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class RandomRemoval implements DestroyOperator {
    @Override
    public String name() {
        return "random-removal";
    }

    @Override
    public List<DeliveryJob> remove(Solution solution, int removeCount, Random random) {
        List<DeliveryJob> candidates = new ArrayList<>();
        for (RoutePlan route : solution.routes()) {
            candidates.addAll(route.jobs());
        }

        Collections.shuffle(candidates, random);
        return candidates.subList(0, Math.min(removeCount, candidates.size()));
    }
}
