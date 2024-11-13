package com.orasaari;

import java.awt.Point;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Implementation of Dijkstra algorith. 
 */
class DijkstraPathfinder implements Pathfinder {

    private static final int[][] MOVES = {{-1,0}, {1,0}, {0,-1}, {0,1}, {-1,-1}, {1,1}, {-1,1}, {1,-1}};
    private static final double[] WEIGHTS = {1, 1, 1, 1, MapUtil.SQRT2, MapUtil.SQRT2, MapUtil.SQRT2, MapUtil.SQRT2};

    private class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return Double.compare(n1.distance, n2.distance);
        }
    }

    /**
     * Implement the pathfinding algoritm.
    */
    public Result navigate(GridMap map, Point start , Point finish) {
            
        System.out.println("DijstraPathfinder.navigate, start: " + start + ", finish: " + finish);

        long startTime = System.currentTimeMillis();

        // Init heap
        PriorityQueue<Node> heap = new PriorityQueue<Node>(new NodeComparator());

        // Initialize iteration
        boolean[][] grid = map.getGrid(); 
        int width = grid.length;
        int height = grid[0].length;

        // Nodes are created as needed. They are maintained in an array and re-used 
        // to minimize overhead of node creation and storing.
        Node[][] nodeList = new Node[grid.length][grid[0].length];

        Node currentNode = new Node(start.x, start.y);
        nodeList[start.x][start.y] = currentNode;
        currentNode.distance = 0;
        heap.add(currentNode);           
        int numeOfEvaluatedNodes = 0;

        // Execute iteration
        while(!heap.isEmpty()) {

            currentNode = heap.poll();

            // We are only calculating the the route so we can finish when in the end node
            if(currentNode.x == finish.x && currentNode.y == finish.y) {
                break;
            }

            // check, if the current node is in the handled list
            if(currentNode.handled) {
                continue;
            }

            currentNode.handled = true;
            numeOfEvaluatedNodes++;

            // Iterate over the potential edges of the current node
            for(int i=0; i<8; i++) {
                int nextNodeX = currentNode.x + MOVES[i][0];
                int nextNodeY = currentNode.y + MOVES[i][1];

                // check that the adjacent node (pixel) is inside the map limits
                if(nextNodeX < 0 || nextNodeY <0 || nextNodeX >= width || nextNodeY >= height) {
                    continue;
                }

                // check that the adjacent node is free to travel
                if(!grid[nextNodeX][nextNodeY]) {
                    continue;
                }

                // Check if we have already created a node object for the particular location. If yes, re-use it.
                Node nextNode = nodeList[nextNodeX][nextNodeY];
                if(nextNode == null) {
                    nextNode = new Node(nextNodeX, nextNodeY);
                    nodeList[nextNodeX][nextNodeY] = nextNode;
                }
                double edgeWeight = WEIGHTS[i];

                // check if our distance from the current node to the next node is smaller that the current shortest route to the next node
                double newDistance = currentNode.distance + edgeWeight;
                if(newDistance < nextNode.distance) {
                    nextNode.distance = newDistance;
                    heap.add(nextNode);
                    nextNode.previous = currentNode;
                }                
            }
        }

        long finishTime = System.currentTimeMillis();

        boolean success = currentNode.x == finish.x && currentNode.y == finish.y;
        Result result = MapUtil.collectResults(currentNode, startTime, finishTime, numeOfEvaluatedNodes, MapUtil.ALGORITHM_ASTAR, success);
        return result;
    }  
}
