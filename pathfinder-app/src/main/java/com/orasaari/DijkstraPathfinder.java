package com.orasaari;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Implementation of the basic Dijkstra algorith.
 */
class DijkstraPathfinder extends Pathfinder {

    /** 
     * Internal class to decide the order of two nodes in the priority queue.  
     * In Dijkstra, the priority is only the distance of the node from the starting point.
    */
    private class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return Double.compare(n1.distanceFromStart, n2.distanceFromStart);
        }
    }

    /**
     * Implement the Dijkstra pathfinding.
     * 
     * @see Pathfinder.findpath(GridMap map, int startX, int startY, int goalX, int goalY)
     *
    */
    public PathfindingResult findPath(GridMap map, int startX, int startY, int goalX, int goalY) {
            
        // Initialize the data structures
        long startTime = System.currentTimeMillis();
        PriorityQueue<Node> heap = new PriorityQueue<Node>(new NodeComparator());
        Node[][] nodeList = new Node[map.getWidth()][map.getHeight()];

       // Initialize the priority heap with the starting node
        Node startNode = new Node(startX, startY);
        nodeList[startX][startY] = startNode;
        startNode.distanceFromStart = 0;
        heap.add(startNode);           

        // Run the pathfinding
        int evaluatedNodes = 0;
        Node node = null;
        boolean goalFound = false;        
        while(!heap.isEmpty()) {
            node = heap.poll();

            if(node.x == goalX && node.y == goalY) {
                goalFound = true;
                break; 
            }
            if(node.handled) {
                continue; 
            }

            node.handled = true; 
            evaluatedNodes++; 

            // Evaluate all moving directions. 
            for(int i=0; i<8; i++) {

                if(!map.isTraversable(node.x, node.y, i)) {
                    continue; 
                }

                int nextX = node.x + MapUtils.MOVE_DIRECTIONS[i].directionX;
                int nextY = node.y + MapUtils.MOVE_DIRECTIONS[i].directionY;

                // Only create each node once to save memory.
                Node nextNode = nodeList[nextX][nextY];
                if(nextNode == null) {
                    nextNode = new Node(nextX, nextY);
                    nodeList[nextX][nextY] = nextNode;
                }
                double weight = MapUtils.WEIGHTS[i];

                // Check if we have found a shorter path. If yes, update heap.
                double newDistance = node.distanceFromStart + weight;
                if(newDistance < nextNode.distanceFromStart) {
                    nextNode.distanceFromStart = newDistance;
                    heap.add(nextNode);
                    nextNode.previousNode = node;
                }                
            }
        }

        // Iteration finished, collect results and return
        PathfindingResult result = collectResults(node, startTime, evaluatedNodes, MapUtils.ALGORITHM_ASTAR, goalFound);
        return result;
    }  
}
