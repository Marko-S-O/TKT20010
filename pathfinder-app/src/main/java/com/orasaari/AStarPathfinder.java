package com.orasaari;

import java.awt.Point;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Implementation of the A* algorith. 
 * This is a copy of Dijkstra with only minor modifications in the the navigate function: 
 * heap is prioritized according to distance + heuristic function instead of just distance from the start node.
 * Heuristic function = octile distance from a node in the end of edge to the finishing node.
 */
class AStarPathfinder implements Pathfinder {

    // MOVES and WEIGHTS are just supporting variables that allow writing cleaner code in the navigate function.
    //private static final int[][] MOVES = {{-1,0}, {1,0}, {0,-1}, {0,1}, {-1,-1}, {1,1}, {-1,1}, {1,-1}};
    //private static final double[] WEIGHTS = {1, 1, 1, 1, MapUtil.SQRT2, MapUtil.SQRT2, MapUtil.SQRT2, MapUtil.SQRT2};

    /** 
     * Internal class to compare place of two nodes in the priority queue. 
    */
    private class ANodeComparator implements Comparator<Node> {        
        public int compare(Node n1, Node n2) {
            return Double.compare(n1.priority, n2.priority);
        }
    }

    public Result navigate(GridMap map, Point start , Point finish) {
        return navigate(map, start, finish, false);
    }   

    /**
     * Implement the pathfinding algoritm.
    */
    public Result navigate(GridMap map, Point start , Point finish, boolean cutCorners) {
            
        // System.out.println("AStartPathfinder.navigate, start: " + start + ", finish: " + finish);



        boolean[][] grid = map.getGrid(); 
        boolean[][][] travellability = map.getTraversability(cutCorners);

        // Initialize the timer. Calculating travallebility of nodes is not included in performance evaluation.
        long startTime = System.currentTimeMillis();

        // Init heap
        PriorityQueue<Node> heap = new PriorityQueue<Node>(new ANodeComparator());



        // Nodes are created as needed. They are maintained in an array and re-used 
        // to minimize overhead of node creation and storing.
        Node[][] nodeList = new Node[grid.length][grid[0].length];
        Node currentNode = new Node(start.x, start.y);
        nodeList[start.x][start.y] = currentNode;
        currentNode.distance = 0;
        double octileDistance = MapUtil.octileDistance(start.x, start.y, finish.x, finish.y);
        currentNode.priority = octileDistance;
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

            // Iterate over the potential edges of the current node. 
            // Currently, no edges are calculated to adjancency lists beforehand but movement options are evaluated on the fly.
            // Another option would be to describe available transfer options as aboolean table for each node (pixel).
            for(int i=0; i<8; i++) {

                if(!travellability[currentNode.x][currentNode.y][i]) {
                    continue;
                }

                int nextNodeX = currentNode.x + MapUtil.MOVES[i][0];
                int nextNodeY = currentNode.y + MapUtil.MOVES[i][1];

                // Check if we have already created a node object for the particular location. If yes, re-use it.
                Node nextNode = nodeList[nextNodeX][nextNodeY];
                if(nextNode == null) {
                    nextNode = new Node(nextNodeX, nextNodeY);
                    nodeList[nextNodeX][nextNodeY] = nextNode;
                }
                double edgeWeight = MapUtil.WEIGHTS[i];

                // Update the Dijkstra algorithm to cover A* priority handling: add use of field aPriority to prioritize the heap.
                // The heap comparator (internal class NodeComparator) needs to be updated accordingly.
                double newDistance = currentNode.distance + edgeWeight;
                if(newDistance < nextNode.distance) {
                    nextNode.distance = newDistance;

                    // Change new priority for the heap.
                    // Priority = distance + heuristic function (in Dijkstra only distance).
                    // Heuristic function = octile distance to the finish node.                
                    octileDistance = MapUtil.octileDistance(nextNodeX, nextNodeY, finish.x, finish.y);                    
                    nextNode.priority = newDistance + octileDistance;
                    heap.add(nextNode);
                    nextNode.previous = currentNode;
                }                
            }
        }

        long finishTime = System.currentTimeMillis();
        boolean success = currentNode.x == finish.x && currentNode.y == finish.y;
        Result result = MapUtil.collectResults(currentNode, startTime, finishTime, numeOfEvaluatedNodes, MapUtil.ALGORITHM_DIJKSTRA, success);
        return result;
    }  
}
