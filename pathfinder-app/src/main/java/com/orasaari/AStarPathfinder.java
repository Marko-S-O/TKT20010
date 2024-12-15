package com.orasaari;

import java.util.PriorityQueue;

/**
 * Implementation of the A* pathfinding algorithm.
 */
public class AStarPathfinder extends Pathfinder {

    /**
     * Implement the A* pathfinding.
     * 
     * @see Pathfinder.findpath(GridMap map, int startX, int startY, int goalX, int goalY)
    */
    public PathfindingResult findPath(GridMap map, int startX , int startY, int goalX, int goalY) {
            
        // Initialize the data structures
        long startTime = System.currentTimeMillis();
        PriorityQueue<Node> heap = new PriorityQueue<Node>();
        double[][] distances = new double[map.getWidth()][map.getHeight()];
        boolean[][] handled = new boolean[map.getWidth()][map.getHeight()];

        // Initialize the priority heap with the starting node
        Node startNode = new Node(startX, startY);
        startNode.priority = octileDistance(startX, startY, goalX, goalY);
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
                double distance = distances[node.x][node.y] + Move.EDGE_WEIGHTS[i];

                // Check if we have found a shorter path. If yes, update heap.
                if(distances[nextX][nextY] == 0.0 || distance < distances[nextX][nextY]) {                    
                    distances[nextX][nextY] = distance;
                    // priority = distance to the node + octile distance to the goal, written open here to optimize performance
                    double dx = Math.abs(nextX - goalX);
                    double dy = Math.abs(nextY - goalY);
                    double od = Math.max(dx, dy) + MapUtils.SQRT2_1 * Math.min(dx, dy);
                    // double priority = distance + Math.min(Math.abs(nextX - goalX), Math.abs(nextY - goalY)) * MapUtils.SQRT2 + Math.abs(Math.abs(nextX - goalX) - Math.abs(nextY - goalY));
                    double priority = distance + od;
                    Node nextNode = new Node(nextX, nextY, priority, node);                   
                    heap.add(nextNode);
                }                
            }
        }

        // Iteration finished, collect results and return
        PathfindingResult result = collectResults(node, startTime, evaluatedNodes, ALGORITHM_ASTAR, goalFound, distances[node.x][node.y]);
        return result;
    }  
}
