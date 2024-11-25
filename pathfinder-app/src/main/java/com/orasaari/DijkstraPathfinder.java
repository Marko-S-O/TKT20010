package com.orasaari;

import java.awt.Point;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Implementation of the basic Dijkstra algorith.
 */
class DijkstraPathfinder implements Pathfinder {

     /** 
     * Internal class to decide the order of two nodes in the priority queue.  
     * In Dijkstra, the priority is only the distance of the node from the starting point.
    */
    private class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return Double.compare(n1.distance, n2.distance);
        }
    }

    /* 
     * @see com.orasaari.DijkstraPathfinder#navigate(com.orasaari.GridMap, java.awt.Point, java.awt.Point, boolean)
    */
    public Result navigate(GridMap map, Point start, Point finish) {
        return navigate(map, start, finish, false);
    }

    /**
     * Implement the Dijkstra pathfinding.
     * 
     * @param map           The 2D grid map where the path is searched.
     * @param start         The starting point of the path.
     * @param goal          The finishing point of the path.
     * @param cutCorners    If false, diagonal movement is allowed only if both of the adjacent (vertical and horizontal neighbors 
     *                      towards the moving direction) nodes are traversable. Currently, only using false value in the performance evaluation
     *                      to have comparable results with the Moving AI Lab scenarios.
     * 
     * @return              the Result object wrapping the pathfinding results
    */
    public Result navigate(GridMap map, Point start, Point finish, boolean cutCorners) {
            
        boolean[][] grid = map.getGrid(); 
        boolean[][][] travellability = map.getTraversability(cutCorners); // pre-calculated traversability to the adjacent nodes
 
        
        // Initialize the timer. Calculating travallebility of nodes is not included in performance evaluation.
        long startTime = System.currentTimeMillis();
        
        // Initialize the priority heap
        PriorityQueue<Node> heap = new PriorityQueue<Node>(new NodeComparator());

        // Nodes are created only as needed and maintained in an array and re-used to minimize overhead.
        Node[][] nodeList = new Node[grid.length][grid[0].length];

        // Initialize the priority heap with the starting node
        Node currentNode = new Node(start.x, start.y);
        nodeList[start.x][start.y] = currentNode;
        currentNode.distance = 0;
        heap.add(currentNode);           
        int numeOfEvaluatedNodes = 0;

        // Execute the pathfinding iteration
        while(!heap.isEmpty()) {
            currentNode = heap.poll();

            if(currentNode.x == finish.x && currentNode.y == finish.y) {
                break; // goal met, finish and collect results
            }

            // check, if the current node is in already handled
            if(currentNode.handled) {
                continue; // skip already handled nodes
            }

            currentNode.handled = true; // handle node & mark it as handled
            numeOfEvaluatedNodes++; // number of evaluated nodes is additional information for performance evaluation

            // Iterate over the potential edges of the current node. 
            for(int i=0; i<8; i++) {

                if(!travellability[currentNode.x][currentNode.y][i]) {
                    continue; // blocked edger or ouside grid, skip
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

                // Check if our distance from the current node to the next node is smaller that the current shortest route 
                // to the next node. If yes, update to the object and add it to the priority heap.
                double newDistance = currentNode.distance + edgeWeight;
                if(newDistance < nextNode.distance) {
                    nextNode.distance = newDistance;
                    heap.add(nextNode);
                    nextNode.previous = currentNode;
                }                
            }
        }

        // Iteration finished, collect results and return
        long finishTime = System.currentTimeMillis();
        boolean success = currentNode.x == finish.x && currentNode.y == finish.y;
        Result result = MapUtil.collectResults(currentNode, startTime, finishTime, numeOfEvaluatedNodes, MapUtil.ALGORITHM_ASTAR, success);
        return result;
    }  
}
