package com.orasaari;

import java.util.ArrayList;

/**
 * A data class for results of a single evaluation session, including 1-3 algorithms, 
 * 1-n scenarios and 1-n iterations for each algorith and scenario.
 */
class PerformanceEvaluationResults {
    
    int[] numberOfEvaluations = new int[3];
    long[] executionTime = new long[3];    
    int[] success = new int[3];
    int[] correctDistance = new int[3];
    int[] failures = new int[3];
    int[] evaluatedNodes = new int[3];
    int[] pathNodes = new int[3];
    ArrayList<PerformanceEvaluation> evaluations = new ArrayList<PerformanceEvaluation>(500);

    /**
     * Add a single evaluation to the results
     * 
     * @param evaluation    Evalution result for a single run of one algorithm, one scenario, one iteration.
     */
    void addEvaluationResult(PerformanceEvaluation evaluation) {
        Result result = evaluation.result;
        int algorithm = evaluation.algorithm;
        numberOfEvaluations[algorithm]++;
        executionTime[algorithm] += result.duration;
        if(result.success) {
            success[algorithm]++;
        } else {
            failures[algorithm]++;
        }
        if(evaluation.correctDistance) {
            correctDistance[algorithm]++;
        } else {
            correctDistance[algorithm]++;
        }        
        evaluatedNodes[algorithm] += result.numeOfEvaluatedNodes;
        pathNodes[algorithm] += result.numOfPathNodes;
        evaluations.add(evaluation);
    }

    /**
     * Construct a string representation for a single algorithm covering all scenarios and iterations.
     */
    private String algoritmResultString(int algorithm, String name) {
        return "\n\nAlgorithm: " + name + " (" + algorithm + ")" +
        "\n evaluations: " + numberOfEvaluations[algorithm] + 
        "\n success: " + success[algorithm] + 
        "\n failures: " + failures[algorithm] + 
        "\n correct distance: " + correctDistance[algorithm] + 
        "\n total execution time: " + executionTime[algorithm]  + 
        "\n average time: " + executionTime[algorithm] / numberOfEvaluations[algorithm] + 
        "\n avg. evaluated nodes: " + evaluatedNodes[algorithm] / numberOfEvaluations[algorithm] + 
        "\n avg. path nodes: " + pathNodes[algorithm] / numberOfEvaluations[algorithm];        
    }

    /** 
     * Construct a string representation of one evaluation session including all algorithms, scenarios and iterations.
    */
    public String toString() {
        String result = "\nEvaluation Results Summary:";

        if(numberOfEvaluations[MapUtil.ALGORITHM_DIJKSTRA] > 0) {
            result += algoritmResultString(MapUtil.ALGORITHM_DIJKSTRA, "Dijkstra");    
        } 

        if(numberOfEvaluations[MapUtil.ALGORITHM_ASTAR] > 0) {
            result += algoritmResultString(MapUtil.ALGORITHM_ASTAR, "A*");
        }

        if(numberOfEvaluations[MapUtil.ALGORITHM_JPS] > 0) {
            result += algoritmResultString(MapUtil.ALGORITHM_JPS, "JPS");
        }
        return result;
    }

}
