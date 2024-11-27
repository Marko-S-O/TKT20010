package com.orasaari;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/** 
 * A static util class to implement utility methods and store global constants.
 */
class MapUtils {

    // Define some constants for convienience and code readability.
    static final double SQRT2 = Math.sqrt(2.0);
    static final int ALGORITHM_DIJKSTRA = 0;
    static final int ALGORITHM_ASTAR = 1;
    static final int ALGORITHM_JPS = 2;
    static final String[] ALGORITHM_NAMES = {"Dijkstra", "A-Star", "JPS"};
    static final int GRID_BLOCKED = 0;
    static final int LEFT = 0;
    static final int RIGHT = 1;
    static final int UP = 2;    
    static final int DOWN = 3;
    static final int LEFT_UP = 4;    
    static final int RIGHT_DOWN = 5;
    static final int LEFT_DOWN = 6;
    static final int RIGHT_UP = 7;

    // Directories of the used files. If deploying the code to another environment, these need to be adjusted.
    static final String MAP_DIRECTORY = "c:/users/ext/TKT20010/pathfinder-app/data/";
    static final String SCENARIO_DIRECTORY = "c:/users/ext/TKT20010/pathfinder-app/data/test/";    
    static final String STREET_MAP_DIRECTORY = "c:/users/ext/TKT20010/pathfinder-app/data/street-map/"; 

    /** Possible moving directions in the 2D grid. Intended to simplify code and make algorithms faster. Used by A* and Dijkstra */
    static final int[][] MOVES = {{-1,0}, {1,0}, {0,-1}, {0,1}, {-1,-1}, {1,1}, {-1,1}, {1,-1}};

    /** Distances of corresponding MOVES. Intended to simplify code and make algorithms faster. Used by A* and Dijkstra */
    static final double[] WEIGHTS = {1, 1, 1, 1, MapUtils.SQRT2, MapUtils.SQRT2, MapUtils.SQRT2, MapUtils.SQRT2};

    /** Moving directions are running around the clock from north-east to north. These are used by JPS but the order is the same as A* and mapping to the traversabilty table indices. */
    static final Move[] MOVE_DIRECTIONS = {
        new Move(-1,0),  // left
        new Move(1,0),   // right
        new Move(0,-1),  // up   
        new Move(0,1),   // right
        new Move(-1,-1), // left-up
        new Move(1,1),  // right-down
        new Move(-1,1), // left-down
        new Move(1,-1)   // right-up
    };

    /** Get the vertical components of the diagonal paths. For example, for right-down return down. */
    static final int[] VERTICAL_ONLY_PATHS = {
        -1,
        -1,
        -1,
        -1,
        UP,
        DOWN,
        DOWN,
        UP
    };

    /** Get the horizontal components of the diagonal paths. For example, for right-down return right. */
    static final int[] HORIZONTAL_ONLY_PATHS = {
        -1,
        -1,
        -1,
        -1,
        LEFT,
        RIGHT,
        LEFT,
        RIGHT,
    };

    /**
     * Map direction given in coordinates to the numeric direction code mapping to the traversability array indices. 
     * 
     * @see GridMap.getTravellability().
     * 
     * @param directionX    x component of the movement (-1, 0, 1)
     * @param directionY    y component of the movement (-1, 0, 1)
     * 
     * @return  The numeric direction code mapping the the traversability matrix.
     */
    static int getDirection(int directionX, int directionY) {

        if(directionX == -1) {
            if(directionY == -1) {
                return LEFT_UP;
            } else if(directionY == 0) {
                return LEFT;
            } else {
                return LEFT_DOWN;
            }
        } else if(directionX == 0) {
            if(directionY == -1) {
                return UP;
            } else {
                return DOWN;
            }
        } else if(directionX == 1) {
            if(directionY == -1) {
                return RIGHT_UP;
            } else if(directionY == 0) {
                return RIGHT;
            } else {
                return RIGHT_DOWN;
            }
        } else {
            return -1;
        }
    }
 
    /** 
     * Read a Movin AI Lab .map file from the disc and convert it to two-dimensional boolean array format.
     *
     *@see https://www.movingai.com/benchmarks/grids.html 
     */
    static GridMap loadMap(String filename) {
        File file = new File(filename);
        String filenameSuffix = filename.substring(filename.lastIndexOf('.'));
        if(filenameSuffix.equals(".map")) {
            GridMap map = loadMapFile(file);
            return map;
        } else {
            throw new IllegalArgumentException("Currently, only .map files from Moving AI Lab are supported.");
        }
    }

    /** 
     * Load a .map type file. 
     * 
     * @param   file    The file in the local file system.
     * 
     * @return  The GridMap object representing the map, or null in case of any error.
    */
    private static GridMap loadMapFile(File file) {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // Skip the type ("octile")
            String line2 = reader.readLine();
            int height = Integer.parseInt(line2.substring(line2.indexOf(' ')+1));
            String line3 = reader.readLine();
            int width = Integer.parseInt(line3.substring(line3.indexOf(' ')+1));
            reader.readLine(); // skip the map header ("map")

            boolean[][] grid = new boolean[width][height];
            String line;
            int j = 0;
            while((line = reader.readLine()) != null) {
                for(int i=0; i<line.length(); i++) {
                    grid[i][j] = line.charAt(i) == '.';
                }
                j++;
            }
                        
            return new GridMap(grid);

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Collect the result of pathfinding. For JPS, fill the jumped parts of the grid are filled with jump passthrough nodes for visualization purposes.
     * 
     * @param   goalNode                The found end node, or the last evaluated node if the goal was not found.
     * @param   startTime               Start time of pathfinding (ms).
     * @param   finishTime              End time of pathfinding for performance evaluation (ms)
     * @param   numOfEvaluatedNodes     Number of nodes that the algorithm handled during pathfinding (dirrerent from the number of nodes in the path).
     * @param   algorithm               Numeric code of the algorithm, codes defined in class MapUtil.
     * @param   success                 True if the pathfinder reached the goal node, false if there was no route.
     * 
     * @return  Collected results to be used by UI and performance evaluation.
     */
    static Result collectResults(Node goalNode, long startTime, long finishTime, int numeOfEvaluatedNodes, int algorithm, boolean success) {

        Result result = new Result();

        List<Node> path = new ArrayList<Node>();
        path.add(goalNode);
        int numOfPathNodes = 1;
        double distance = goalNode.distance;
        Node nextNode = goalNode;
        Node previousNode = goalNode.previous;

        while(previousNode != null) {
            numOfPathNodes += 1;

            // Check if there is a jump created by JPS on the path
            int deltaX = nextNode.x - previousNode.x;
            int deltaY = nextNode.y - previousNode.y;
            if(Math.abs(deltaX) > 1 || Math.abs(deltaY) > 1) {

                // fill the nodes between the jump end and start points with jump passthrough nodes
                int directionX = deltaX > 0 ? -1: (deltaX == 0 ? 0 : 1);
                int directiony = deltaY > 0 ? -1 : (deltaY == 0 ? 0 : 1);
                int middleNodeX = nextNode.x + directionX;
                int middleNodeY  = nextNode.y + directiony;
                while(! (middleNodeX==previousNode.x && middleNodeY==previousNode.y) ) {
                    Node middleNode = new Node(middleNodeX, middleNodeY);
                    middleNode.jumpPassthrough = true;
                    middleNodeX = middleNodeX + directionX;
                    middleNodeY  = middleNodeY + directiony;
                    path.add(middleNode);
                }
            }
            
            path.add(previousNode);
            nextNode = previousNode;
            previousNode = previousNode.previous;
        }
        path = path.reversed(); // path was collected from goal to start -> reverse

        result.path = path;
        result.numOfPathNodes = numOfPathNodes;
        result.startTime = startTime;
        result.finishTime = finishTime;
        result.duration = finishTime - startTime;
        result.numeOfEvaluatedNodes = numeOfEvaluatedNodes;
        result.algorithm = algorithm;
        result.distance = distance;
        result.success = success;
        
        return result;
    }

    /**
     * Calculate octile distance between points (x0, y0) and (x1, y1).
     */
    static double octileDistance(int x0, int y0, int x1, int y1) {
        int deltaX = Math.abs(x0 - x1);
        int deltaY = Math.abs(y0 - y1);
        double octileDistance = Math.min(deltaX, deltaY) * MapUtils.SQRT2 + Math.abs(deltaX - deltaY);
        return octileDistance;
    }

    /**
     * Write a summary line for a single algorithm to the summary CSV file.
     * 
     * @param writer        The file to be used.
     * @param algorithm     The algorithm id, mapping to MapUtil.ALGORITHM_*
     * @param algorithmName Name of the algorithm to be written to the file.
     * @throws IOException  Thrown automatically in case of I/O errors.
     */

     private static void writeSummaryLine(FileWriter writer, int algorithm, String algorithmName, PerformanceEvaluationResults results) throws IOException {
        writer.write("algorithmName," + 
            results.numberOfEvaluations[algorithm] + ',' + 
            results.success[algorithm] + ',' + 
            results.correctDistance[algorithm] + ',' + 
            results.executionTime[algorithm] + ',' + 
            results.executionTime[algorithm] / results.numberOfEvaluations[algorithm] + ',' + 
            results.pathNodes[algorithm] + ',' + 
            results.evaluatedNodes[algorithm] + '\n'); 
    }

    /**
     * Save the performance evaluation results to a CSV files to be used further, for example, in Excel.
     */
    static void saveToCsv(PerformanceEvaluationResults results) {
        
        // Write detailed results of all evaluations to be used, for example, in Excel.
        try (FileWriter writer = new FileWriter("evaluation_details.csv")) {
            writer.write("Timestamp,Duration,Algorithm,Distance,Success,CorrectDistance,PathNodes,EvaluatedNodes\n");
            for(PerformanceEvaluation evaluation : results.evaluations) {
                writer.write(evaluation.toCsvString() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Write summary lines for each algorithms to another file.
        try (FileWriter writer = new FileWriter("evaluation_summary.csv")) {
            writer.write("Algorithm,NumberOfEvaluations,Success,CorrectDistance,TotalTime,AverageTime,AveragePathNodes,AverageEvaluatedNodes\n");
            if(results.numberOfEvaluations[MapUtils.ALGORITHM_DIJKSTRA] > 0) {
                writeSummaryLine(writer, MapUtils.ALGORITHM_DIJKSTRA, "Dijkstra", results);
            } 
            if(results.numberOfEvaluations[MapUtils.ALGORITHM_ASTAR] > 0) {
                writeSummaryLine(writer, MapUtils.ALGORITHM_ASTAR, "A*", results);
            }
            if(results.numberOfEvaluations[MapUtils.ALGORITHM_JPS] > 0) {
                writeSummaryLine(writer, MapUtils.ALGORITHM_JPS, "JPS", results);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }        

    }
     
}
