package com.orasaari;

import com.orasaari.PerformanceEvaluator.Evaluation;

class PerformanceResults {
    
    int[] numberOfEvaluations = new int[3];
    long[] executionTime = new long[3];    
    int[] success = new int[3];
    int[] correctDistance = new int[3];
    int[] failures = new int[3];
    int[] evaluatedNodes = new int[3];
    int[] pathNodes = new int[3];

    void addEvaluationResult(Evaluation evaluation) {
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
    }

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

    public String toString() {
        String result = "EVALUATION RESULTS\n\n";

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
