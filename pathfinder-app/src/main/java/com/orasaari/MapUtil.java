package com.orasaari;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/** 
 * A static util class to implement utility methods and store global constants.
 */
class MapUtil {

    static final double SQRT2 = Math.sqrt(2.0);
    static final int ALGORITHM_DIJKSTRA = 0;
    static final int ALGORITHM_ASTAR = 1;
    static final int ALGORITHM_JPS = 2;
    static final String[] ALGORITHM_NAMES = {"Dijkstra", "A-Star", "JPS"};
    static final int GRID_BLOCKED = 0;

    /** Possible moving directions in the 2D grid. Intended to simplify code and make algorithms faster. Used by A* and Dijkstra */
    static final int[][] MOVES = {{-1,0}, {1,0}, {0,-1}, {0,1}, {-1,-1}, {1,1}, {-1,1}, {1,-1}};

    /** Distances of corresponding MOVES. Intended to simplify code and make algorithms faster. Used by A* and Dijkstra */
    static final double[] WEIGHTS = {1, 1, 1, 1, MapUtil.SQRT2, MapUtil.SQRT2, MapUtil.SQRT2, MapUtil.SQRT2};

    /** Moving directions are running around the clock from north-east to north. This makes them more intuitive and easier to use in forced neighbors calculations. */
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

    static final int LEFT = 0;
    static final int RIGHT = 1;
    static final int UP = 2;    
    static final int DOWN = 3;
    static final int LEFT_UP = 4;    
    static final int RIGHT_DOWN = 5;
    static final int LEFT_DOWN = 6;
    static final int RIGHT_UP = 7;

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

    static final int[] DIAGONAL_ONLY_PATHS = {
        -1,
        -1,
        -1,
        -1,
        RIGHT_DOWN,
        LEFT_UP,
        LEFT_DOWN,
        RIGHT_UP
    };

    /**
     * Map direction given in coordinates to the numeric direction code. Numeric direction codes are defined in MapUtil
     * and are mapping to traversability map given by GridMap.getTravellability().
     * @param directionX
     * @param directionY
     * @return
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
            } else if(directionY == 0) {
                throw new IllegalArgumentException("Invalid direction: " + directionX + ", " + directionY);
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
            throw new IllegalArgumentException("Invalid direction: " + directionX + ", " + directionY);
        }

    }

    static final String MAP_DIRECTORY = "c:/users/ext/TKT20010/pathfinder-app/data/";
        
    /** 
     * Utility method to load a map file from a disc and convert it to two-dimensional boolean array format.
     * Currently, only maps of type of .map are supported.
     *@see https://www.movingai.com/benchmarks/grids.html
     * 
     */
    static GridMap loadMap(String filename) {

        GridMap map = null;
        File file = new File(filename);

        // deduct the file type from the suffix
        String filenameSuffix = filename.substring(filename.lastIndexOf('.'));
        if(filenameSuffix.equals(".map")) {
            map = loadMapFile(file);
        }
        return map;
        
    }

    /** 
     * Implementation to load a .map type file. 
     * 
     * @param   file    the file in the local file system
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
     * Collect the result of pathfinding, including the found shortes route from the nodes that were linked in iteration. 
     * For JPS, fill the jump paths with jump nodes.
     * 
     * @param   finishNode  the found end node, or the last evaluated node if path was not found
     * @param   startTime   start time of pathfinding for performance evaluation
     * @param   finishTime  end time of pathfinding for performance evaluation
     * @param   numOfEvaluatedNodes number of nodes that the algorithm handled during pathfinding
     * @param   algorithm   numeric code of the algorithm, codes defined in class MapUtil
     * @param   success     true if the pathfinder reached the end node, false if there was no route
     * 
     * @return  Collected results to be used by UI and performance evaluation
     */
    static Result collectResults(Node finishNode, long startTime, long finishTime, int numeOfEvaluatedNodes, int algorithm, boolean success) {

        Result result = new Result();

        List<Node> path = new ArrayList<Node>();
        path.add(finishNode);
        int numOfPathNodes = 1;
        double distance = finishNode.distance;
        Node nextNode = finishNode;
        Node previousNode = finishNode.previous;

        while(previousNode != null) {
            numOfPathNodes += 1;

            //System.out.println("numOfPathNodes: " + numOfPathNodes);
            //System.out.println("previous: " + previous.x + ", " + previous.y);

            // Check if there is a jump created by JPS on the path
            int deltaX = nextNode.x - previousNode.x;
            int deltaY = nextNode.y - previousNode.y;
            if(deltaX > 1 || deltaY > 1) {

                // fill the pixels between the jump end and start points
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
        path = path.reversed();
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
        double octileDistance = Math.min(deltaX, deltaY) * MapUtil.SQRT2 + Math.abs(deltaX - deltaY);
        return octileDistance;
    }


    /* 
     * Calculate euclidian distance between points (x0, y0) and (x1, y1). 
     * Used to evaluate effect of different heuristic function in JPS and A*.
    */
    static double euclideanDistance(int x0, int y0, int x1, int y1) {
        double e = Math.sqrt(Math.pow(Math.abs(x0-x1), 2) + Math.pow(Math.abs(y0-y1), 2));
        return e;
    }
     
}
