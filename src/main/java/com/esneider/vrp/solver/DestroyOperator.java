package com.esneider.vrp.solver;

import com.esneider.vrp.model.DeliveryJob;
import com.esneider.vrp.model.Solution;

import java.util.List;
import java.util.Random;

public interface DestroyOperator {
    String name();

    List<DeliveryJob> remove(Solution solution, int removeCount, Random random);
}
