package com.orasaari;

import java.io.FileWriter;
import java.util.ArrayList;

/**
 * A class for results of an evaluation session, including 1-3 algorithms, 
 * 1-n scenarios and 1-n iterations for each algorith and scenario.
 */
class PerformanceEvaluationResults {
    
    int[] numberOfEvaluations = new int[3];
    long[] executionTime = new long[3];    
    int[] success = new int[3];
    int[] correctDistance = new int[3];
    int[] failures = new int[3];
    long[] evaluatedNodes = new long[3];
    int[] pathNodes = new int[3];
    ArrayList<PerformanceEvaluation> evaluations = new ArrayList<PerformanceEvaluation>(8100);

    /**
     * Add a single evaluation to the results
     * 
     * @param evaluation    Evalution result for a single run of one algorithm, one scenario, one iteration.
     */
    void addEvaluationResult(PerformanceEvaluation evaluation) {
        PathfindingResult result = evaluation.result;
        int algorithm = evaluation.algorithm;
        numberOfEvaluations[algorithm]++;
        executionTime[algorithm] += result.searchDuration;
        if(result.goalFound) {
            success[algorithm]++;
        } else {
            failures[algorithm]++;
        }
        if(evaluation.correctDistance) {
            correctDistance[algorithm]++;
        } else {
            correctDistance[algorithm]++;
        }        
        evaluatedNodes[algorithm] += result.nodesEvaluated;
        pathNodes[algorithm] += result.numberOfPathNodes;
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
        "\n avg. evaluated nodes: " + (evaluatedNodes[algorithm] / numberOfEvaluations[algorithm]) + 
        "\n avg. path nodes: " + (pathNodes[algorithm] / numberOfEvaluations[algorithm]);        
    }

    /** 
     * Construct a string representation of one evaluation session including all algorithms, scenarios and iterations.
    */
    public String toString() {
        String result = "\nEvaluation Results Summary:";

        if(numberOfEvaluations[MapUtils.ALGORITHM_DIJKSTRA] > 0) {
            result += algoritmResultString(MapUtils.ALGORITHM_DIJKSTRA, "Dijkstra");    
        } 

        if(numberOfEvaluations[MapUtils.ALGORITHM_ASTAR] > 0) {
            result += algoritmResultString(MapUtils.ALGORITHM_ASTAR, "A*");
        }

        if(numberOfEvaluations[MapUtils.ALGORITHM_JPS] > 0) {
            result += algoritmResultString(MapUtils.ALGORITHM_JPS, "JPS");
        }
        return result;
    }

    /**
     * Get a line of a single algorithm for the summary CSV.
     * 
     */
     private String getSummaryCSVline(int algorithm, String algorithmName) {
        return MapUtils.ALGORITHM_NAMES[algorithm] + ',' + 
            numberOfEvaluations[algorithm] + ',' + 
            success[algorithm] + ',' + 
            correctDistance[algorithm] + ',' + 
            executionTime[algorithm] + ',' + 
            executionTime[algorithm] / numberOfEvaluations[algorithm] + ',' + 
            pathNodes[algorithm] / numberOfEvaluations[algorithm] + ',' + 
            evaluatedNodes[algorithm] / numberOfEvaluations[algorithm] + '\n';
    }

    private String toSummaryCSVString() {

        StringBuffer sb = new StringBuffer(500);
        sb.append("Algorith,Evaluations,Success,Correct Distance,Total Time,Average Time,Avg. Path Nodes,Avg. Eval. Nodes\n");        
        if(numberOfEvaluations[MapUtils.ALGORITHM_DIJKSTRA] > 0) 
            sb.append(getSummaryCSVline(MapUtils.ALGORITHM_DIJKSTRA, MapUtils.ALGORITHM_NAMES[MapUtils.ALGORITHM_DIJKSTRA]));            
        if(numberOfEvaluations[MapUtils.ALGORITHM_ASTAR] > 0) 
            sb.append(getSummaryCSVline(MapUtils.ALGORITHM_ASTAR, MapUtils.ALGORITHM_NAMES[MapUtils.ALGORITHM_ASTAR]));            
        if(numberOfEvaluations[MapUtils.ALGORITHM_JPS] > 0) 
            sb.append(getSummaryCSVline(MapUtils.ALGORITHM_JPS, MapUtils.ALGORITHM_NAMES[MapUtils.ALGORITHM_JPS]));            
        return new String(sb);

    }

    private String toDetailsCSVString() {
        StringBuffer sb = new StringBuffer(5000000);
        sb.append("Time,Duration,Algorithm,Distance,Success,Correct Distance,Path Nodes,Eval. Nodes\n");
        for(PerformanceEvaluation evaluation : evaluations) {
            sb.append(evaluation.toCsvString() + "\n");
        }
        return new String(sb);
    }

    /**
     * Save the evaluation results to a CSV files to be used further, for example, in Excel.
     */
    void saveToCSV() {
        
        // Write detailed results of all evaluations to be used, for example, in Excel.
        try (FileWriter writer = new FileWriter("evaluation_details.csv")) {
            writer.write(toDetailsCSVString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Write summary lines for each algorithm to a file.
        try (FileWriter writer = new FileWriter("evaluation_summary.csv")) {
            writer.write(toSummaryCSVString());
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }  

}
