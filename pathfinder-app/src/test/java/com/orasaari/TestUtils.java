package com.orasaari;

import java.util.ArrayList;
import java.util.List;

/** 
 * Utility methods for JUnit testing. 
*/
public class TestUtils {
    
    static final String TEST_DATA_DIRECTORY = "c:/users/ext/TKT20010/pathfinder-app/src/test/resources/";

    /**
     * Create a testing path from (5,5) to (1000,500).
     * 
     * @return  A list of nodes to be used in JUnit tests.
     */
    static List<Node> getTestPath() {
        Node start = new Node(5, 5);
        Node goal = new Node(1000, 500);
        Node previeousNode = start;
        List<Node> path = new ArrayList<Node>();
        double distance = 0;

        // Diagonal move (5,5) -> (500,500)
        for(int i=6; i<=500; i++) {
            distance += MapUtils.SQRT2;
            Node node = new Node(i, i);
            node.previousNode = previeousNode;
            node.distanceFromStart = distance;
            path.add(node);
            previeousNode = node;
        }

        // Horizontal move (500,500) -> (1000,500)
        for(int i=501; i<=1000; i++) {
            distance += 1;      
            Node node = new Node(i, 500);
            node.previousNode = previeousNode;
            node.distanceFromStart = distance;
            path.add(node);
            previeousNode = node;
        }

        goal.previousNode = previeousNode;
        return path;
    }

    /*
     * Test a single scenario in a Moving AI Lab city map.
     */
    static boolean testInCityMap(Pathfinder pathfinder, String mapFilename, int startX, int startY, int goalX, int goalY, double distance) {
        GridMap map = new GridMap(MapUtils.MAP_DIRECTORY + "street-map\\" + mapFilename);
        PathfindingResult result = pathfinder.findPath(map, startX, startY, goalX, goalY);
        boolean foundPath = result.goalFound;
        boolean correctDistance = Math.abs(result.distance - distance) < 0.01;
        return foundPath && correctDistance;
    }

    /**
     * Test a pathfinding scenario in another (empty or randomized) map.
     */
    static boolean testInOtherMap(Pathfinder pathfinder, GridMap map, int startX, int startY, int goalX, int goalY, double distance) {
        PathfindingResult result = pathfinder.findPath(map, startX, startY, goalX, goalY);
        boolean foundPath = result.goalFound;
        boolean correctDistance = Math.abs(result.distance - distance) < 0.01;
        return foundPath && correctDistance;
    }
}
