package com.orasaari;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Since we validate also correct pathfinding results agains AI Moving Lab scenarios in performance evaluation,
 * we want to get this testing included in JUnit tests to get the testing coverage calculations right.
 */
public class PerformanceEvaluatorTest {
    
    /* 
     * Test performance evaluation with preselected scenarios.
     * Scenarios include 10 scenarios for each 30 1024x1024 city maps.
     */
    @Test
    public void shouldRunPerformanceEvaluationScenariosCorrectly() {
        List<Integer> algorithms = new ArrayList<Integer>(3);
        algorithms.add(MapUtils.ALGORITHM_DIJKSTRA);
        algorithms.add(MapUtils.ALGORITHM_ASTAR);
        algorithms.add(MapUtils.ALGORITHM_JPS);
        PerformanceEvaluator p = new PerformanceEvaluator();
        PerformanceEvaluationResults results = p.runEvaluation(9, MapUtils.SCENARIO_DIRECTORY + "performance-evaluation.scen", algorithms);
        System.out.println(results);
        results.saveToCSV();
    }
}
