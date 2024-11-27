package com.orasaari;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

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
            node.previous = previeousNode;
            node.distance = distance;
            path.add(node);
            previeousNode = node;
        }

        // Horizontal move (500,500) -> (1000,500)
        for(int i=501; i<=1000; i++) {
            distance += 1;      
            Node node = new Node(i, 500);
            node.previous = previeousNode;
            node.distance = distance;
            path.add(node);
            previeousNode = node;
        }

        goal.previous = previeousNode;
        return path;
    }

    static boolean testInCityMap(Pathfinder pathfinder, String mapFilename, int startX, int startY, int goalX, int goalY, double distance) {
        GridMap map = MapUtils.loadMap(MapUtils.MAP_DIRECTORY + "street-map\\" + mapFilename);
        Point start = new Point(startX, startY);
        Point goal = new Point(goalX, goalY);
        Result result = pathfinder.navigate(map, start, goal);
        System.out.println(mapFilename + ": " + result.distance + " (" + startX + "," + startY + ") -> (" + goalX + "," + goalY + "), correct: " + distance);
        System.out.println(result.toString());
        boolean foundPath = result.success;
        boolean correctDistance = Math.abs(result.distance - distance) < 0.01;
        System.out.println("Found path: " + foundPath + ", correct distance: " + correctDistance);
        return foundPath && correctDistance;
    }

    static boolean testInOtherMap(Pathfinder pathfinder, GridMap map, int startX, int startY, int goalX, int goalY, double distance) {
        Point start = new Point(startX, startY);
        Point goal = new Point(goalX, goalY);
        Result result = pathfinder.navigate(map, start, goal);
        boolean foundPath = result.success;
        boolean correctDistance = Math.abs(result.distance - distance) < 0.01;
        return foundPath && correctDistance;
    }

}
