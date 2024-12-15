package com.orasaari;

import java.util.PriorityQueue;

/**
 * Implementation of the basic Dijkstra algorith.
 */
class DijkstraPathfinder extends Pathfinder {


    /**
     * Implement the Dijkstra pathfinding.
     * 
     * @see Pathfinder.findpath(GridMap map, int startX, int startY, int goalX, int goalY)
     *
    */
    public PathfindingResult findPath(GridMap map, int startX, int startY, int goalX, int goalY) {
            
        // Initialize the data structures
        long startTime = System.currentTimeMillis();
        PriorityQueue<Node> heap = new PriorityQueue<Node>();
        double[][] distances = new double[map.getWidth()][map.getHeight()];
        boolean[][] handled = new boolean[map.getWidth()][map.getHeight()];

        // Initialize the priority heap with the starting node
        Node startNode = new Node(startX, startY);
        heap.add(startNode);           

        // Run the pathfinding
        int evaluatedNodes = 0;        
        boolean goalFound = false;
        Node node = null;
        while(!heap.isEmpty()) {
            node = heap.poll();
            
            if(handled[node.x][node.y]) {
                continue;
            } else {
                handled[node.x][node.y] = true;
                evaluatedNodes++;
            }

            if(node.x == goalX && node.y == goalY) {
                goalFound = true;
                break; 
            }            

            // Evaluate all moving directions. 
            for(int i=0; i<8; i++) {

                if(!map.isTraversable(node.x, node.y, i)) {
                    continue;
                }

                int nextX = node.x + Move.MOVE_DIRECTIONS[i].directionX;
                int nextY = node.y + Move.MOVE_DIRECTIONS[i].directionY;
                double distance = distances[node.x][node.y] + MapUtils.WEIGHTS[i];

                // Check if we have found a shorter path. If yes, update heap.
                if(distances[nextX][nextY] == 0.0 || distance < distances[nextX][nextY]) {                    
                    distances[nextX][nextY] = distance;
                    Node nextNode = new Node(nextX, nextY, distance, node);                   
                    heap.add(nextNode);
                }                
            }
        }

        // Iteration finished, collect results and return
        PathfindingResult result = collectResults(node, startTime, evaluatedNodes, MapUtils.ALGORITHM_ASTAR, goalFound, distances[node.x][node.y]);
        return result;
    }  
}
