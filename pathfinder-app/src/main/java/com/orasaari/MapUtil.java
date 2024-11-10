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
    static final String[] ALGORITHM_NAMES = {"Dijkstra"};
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

            byte[][] grid = new byte[width][height];
            String line;
            int j = 0;
            while((line = reader.readLine()) != null) {
                for(int i=0; i<line.length(); i++) {
                    if(line.charAt(i) == '.') {
                        grid[i][j] = (byte)1;
                    }
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
        Node previous = finishNode.previous;

        while(previous != null) {
            numOfPathNodes += 1;
            path.add(previous);
            previous = previous.previous;
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
     
}
