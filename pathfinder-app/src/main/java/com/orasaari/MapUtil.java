package com.orasaari;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/** Static util class to handle map file operations */
class MapUtil {

    static final double SQRT2 = Math.sqrt(2.0);
    static final int ALGORITHM_DIJKSTRA = 0;
    static final int ALGORITHM_ASTAR = 1;
    static final int ALGORITHM_JPS = 2;
    static final String[] ALGORITHM_NAMES = {"Dijkstra", "A-Star", "JPS"};
    static final int GRID_BLOCKED = 0;

    /** 
     * Load a .map type file. 
     * See https://www.movingai.com/benchmarks/grids.html
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
     * Utility method to load a map file from a disc and convert it to two-dimensional byte array format 
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
     * Collect the result of pathfinding, including navigation route from the nodes that were linked in iteration.
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


    static double euclideanDistance(int x0, int y0, int x1, int y1) {
        double e = Math.sqrt(Math.pow(Math.abs(x0-x1), 2) + Math.pow(Math.abs(y0-y1), 2));
        return e;
    }
     
}
