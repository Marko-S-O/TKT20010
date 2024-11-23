package com.orasaari;

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
            distance += MapUtil.SQRT2;
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

}
