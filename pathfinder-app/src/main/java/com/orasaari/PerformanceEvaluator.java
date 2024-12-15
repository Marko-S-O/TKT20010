package com.orasaari;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class to execute performance evaluations. Can be run in the command line or from the UI.
 */
public class PerformanceEvaluator {
    
    private Map<String, GridMap> mapMap = new HashMap<String, GridMap>();

    /** 
     * A private class representing a scenario that is used in performance evaluation. 
     * One scenario can be run multiple times and with multiple algorithms.
    */
    private static class Scenario {
        GridMap map;
        int startX;
        int startY;
        int goalX;
        int goalY;
        double distance;

        Scenario(int scenarioIndex, GridMap map, int startX, int startY, int goalX, int goalY, double distance) {
            this.map = map;
            this.startX = startX;
            this.startY = startY;
            this.goalX = goalX;
            this.goalY = goalY;
            this.distance = distance;            
        }   
    }

    /**
     * Load the scenario list from a file. The file format is the Moving AI Lab .scen format.
     * Also, load the maps that are used in the scenarios.
     * 
     * @param filename  filename of the file that is located in MapUtil.SCENARIO_DIRECTORY;
     * 
     * @return  a list of scenarios including the loaded grid maps
     */
    private List<Scenario> loadScenarios(String filename) {
        
        List<Scenario> scenarioList = new ArrayList<Scenario>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) { 
            String line;
            boolean firstLine = true;
            int scenarioIndex = 0;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
                if(firstLine) {
                    // skep the first line "version..." 
                    firstLine = false;
                    continue;
                }   
                String[] fields = line.split("\\t+");
                GridMap map = mapMap.get(fields[1]);

                // Maps are taking a lot of heap. Load each map only once and store it in the map map.
                if(map == null) {
                    String mapFilename = MapUtils.STREET_MAP_DIRECTORY + fields[1];
                    map = new GridMap(mapFilename);
                    mapMap.put(fields[1], map);
                }                 
                int startX = Integer.parseInt(fields[4]);
                int startY = Integer.parseInt(fields[5]);
                int goalX = Integer.parseInt(fields[6]);
                int goalY = Integer.parseInt(fields[7]);
                double distance = Double.parseDouble(fields[8]);
                scenarioIndex++;
                Scenario scenario = new Scenario(scenarioIndex, map, startX, startY, goalX, goalY, distance);
                scenarioList.add(scenario);                
            }
        } catch(Exception e) {
            System.out.println("Error reading scenario file: " + filename);
            e.printStackTrace();
        } 
        return scenarioList;
    }

    /**
     * Construct each used pathfinder only once and re-use them in the evaluation.
     * 
     * @param algorithms    a list of algorithms to be used in the evaluation, mapping to constants in MapUtil
     * 
     * @return  A map of pathfinders: the key is the algorithm code Mapping to MapUtil.ALGORITHM_* and the value is the Pathfinder object.
    */
    private Map<Integer, Pathfinder> getPatfinders(List<Integer> algorithms) {
        Map<Integer, Pathfinder> map = new HashMap<Integer, Pathfinder>(3);
        for(int i=0; i<algorithms.size(); i++) {
            int algorithm = algorithms.get(i);
            if(algorithm == Pathfinder.ALGORITHM_DIJKSTRA) {
                map.put(algorithm, new DijkstraPathfinder());
            } else if(algorithm == Pathfinder.ALGORITHM_ASTAR) {
                map.put(algorithm, new AStarPathfinder());
            } else {
                map.put(algorithm, new JPSPathfinder());
            }
        }
        return map;
    }

    /**
     * Collect the evaluation results from Evaluation objects to a PerformanceEvaluationResults.
     * 
     * @param evaluationList   list of all evaluations from a single run
     * 
     * @return  composed results of evaluations
    */
    private PerformanceEvaluationResults collectEvaluationResults(List<PerformanceEvaluation> evaluationList) {
        PerformanceEvaluationResults performanceResults = new PerformanceEvaluationResults();
        for(int i=0; i<evaluationList.size(); i++) {
            PerformanceEvaluation evaluation = evaluationList.get(i);
            performanceResults.addEvaluationResult(evaluation);
        }
        return performanceResults;
    }

    /* 
     * Run the all the evaluations listed in the scenario file with given number of iterations and selected algorithms.
     * 
     * @param iterations    number of iterations for each scenario * algorithm
     * @param filename      filename of the scenario file
     * @param algorithms    a list of algorithms (MapUtil.ALGORITHM_*)
     * 
     * @return  composed results of evaluations
    */  
    PerformanceEvaluationResults runEvaluation(int iterations, String filename, List<Integer> algorithms) {

        Map<Integer, Pathfinder> pathfinderMap = getPatfinders(algorithms);
        List<Scenario> scenarioList = loadScenarios(filename);
        List<PerformanceEvaluation> evaluationList = new ArrayList<PerformanceEvaluation>(iterations * algorithms.size() * scenarioList.size());

        for(int i=0; i<scenarioList.size(); i++) {
            System.out.println("Running scenario: " + i);
            Scenario scenario = scenarioList.get(i);

            for(int j=0; j<iterations; j++) {
                for(int k=0; k<algorithms.size(); k++) {

                    // swap the order of algorithms in each iteration to minimize the noise in the results
                    int algorithmIndex = (j + k) % algorithms.size();
                    int algorithm = algorithms.get(algorithmIndex); 
                    Pathfinder pathfinder = pathfinderMap.get(algorithm);                    
                    PerformanceEvaluation evaluation = new PerformanceEvaluation();
                    evaluation.algorithm = algorithm;
                    evaluation.startTime = System.currentTimeMillis();
                    evaluation.result = pathfinder.findPath(scenario.map, scenario.startX, scenario.startY, scenario.goalX, scenario.goalY);
                    evaluation.finishTime = System.currentTimeMillis();
                    evaluation.correctDistance = Math.abs(evaluation.result.distance - scenario.distance) < 0.01;
                    evaluationList.add(evaluation);
                }
            }
        }

        PerformanceEvaluationResults performanceResults = collectEvaluationResults(evaluationList);
        return performanceResults;
    }

    /**
     * The entry point for cases we want to execute from the command line instead of UI.
     * When run from the command line, there is no option to save the results in CSV files.
     * 
     * @param args  No args are used, prepare run options directly to the main method code.
     */
    public static void main(String[] args) {
        List<Integer> algorithms = new ArrayList<Integer>(3);
        algorithms.add(Pathfinder.ALGORITHM_DIJKSTRA);
        algorithms.add(Pathfinder.ALGORITHM_ASTAR);
        algorithms.add(Pathfinder.ALGORITHM_JPS);
        PerformanceEvaluator p = new PerformanceEvaluator();
        PerformanceEvaluationResults results = p.runEvaluation(9, MapUtils.SCENARIO_DIRECTORY + "performance-evaluation.scen", algorithms);
        System.out.println(results);
        results.saveToCSV();
    }       
}
