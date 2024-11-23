package com.orasaari;

import java.awt.Point;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Implementation of the basic Dijkstra algorith.
 */
class DijkstraPathfinder implements Pathfinder {

    /** 
     * A comparator class for the priority heap to calculate the place in open list.
     * Dijkstra, this is simply the distance from the starting point and noheuristic function is used.
     * 
    */
    private class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return Double.compare(n1.distance, n2.distance);
        }
    }

    public Result navigate(GridMap map, Point start, Point finish) {
        return navigate(map, start, finish, true);
    }

    /**
     * Implement the pathfinding algoritm.
     * 
     * @param   map     2D grid map
     * @param   start   starting point in the 2D gripd (x, y)
     * @param   finish  finishing point (goal) in the 2D gripd (x, y)
     * 
     * @return  result  result to be used in the UI and performance evaluation
     * 
    */
    public Result navigate(GridMap map, Point start, Point finish, boolean cutCorners) {
            
        System.out.println("DijstraPathfinder.navigate, start: " + start + ", finish: " + finish);


        // Initialize valiables needed in iterating the route
        boolean[][] grid = map.getGrid(); 
        boolean[][][] travellability = map.getTraversability(cutCorners);

        long startTime = System.currentTimeMillis();
        
        // Initialize the priority heap
        PriorityQueue<Node> heap = new PriorityQueue<Node>(new NodeComparator());

        // Nodes are created as needed. They are maintained in an array and re-used 
        // to minimize overhead of node creation and storing.
        Node[][] nodeList = new Node[grid.length][grid[0].length];

        // Initialize the priority heap with the starting node
        Node currentNode = new Node(start.x, start.y);
        nodeList[start.x][start.y] = currentNode;
        currentNode.distance = 0;
        heap.add(currentNode);           
        int numeOfEvaluatedNodes = 0;

        // Execute iterating the route
        while(!heap.isEmpty()) {

            currentNode = heap.poll();

            // We are only calculating the the route (not distance to each node) so we can finish when the finishing node is found.
            if(currentNode.x == finish.x && currentNode.y == finish.y) {
                break;
            }

            // check, if the current node is in already handled
            if(currentNode.handled) {
                continue;
            }

            // mark the current node handled to prevent it to be handled multiple times
            currentNode.handled = true;
            numeOfEvaluatedNodes++;

            // Iterate over the potential edges of the current node
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
