package com.orasaari;

/**
 * Implementation of the A* pathfinding algorithm.
 */
class AStarPathfinder extends Pathfinder {

    /**
     * Implement the A* pathfinding.
     * 
     * @see Pathfinder.findpath(GridMap map, int startX, int startY, int goalX, int goalY)
    */
    public PathfindingResult findPath(GridMap map, int startX , int startY, int goalX, int goalY) {
            
        // Initialize the data structures
        long startTime = System.currentTimeMillis();
        PriorityHeap heap = new PriorityHeap();
        Node[][] nodeList = new Node[map.getWidth()][map.getHeight()];

        // Initialize the priority heap with the starting node
        Node startNode = new Node(startX, startY);
        startNode.distanceFromStart = 0;
        double octileDistance = octileDistance(startX, startY, goalX, goalY);
        nodeList[startX][startY] = startNode;        
        startNode.priority = octileDistance;
        heap.add(startNode);           

        // Run the pathfinding
        int evaluatedNodes = 0;        
        boolean goalFound = false;
        Node node = null;
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

                int nextX = node.x + MapUtils.MOVES[i][0];
                int nextY = node.y + MapUtils.MOVES[i][1];

                // Only create each node once to save memory.
                boolean nodeExists = true;
                Node nextNode = nodeList[nextX][nextY];
                if(nextNode == null) {
                    nodeExists = false;
                    nextNode = new Node(nextX, nextY);
                    nodeList[nextX][nextY] = nextNode;
                }

                double weight = MapUtils.WEIGHTS[i]; 
                double distance = node.distanceFromStart + weight;

                // Check if we have found a shorter path. If yes, update heap.
                if(distance < nextNode.distanceFromStart) {                    
                    nextNode.distanceFromStart = distance;
                    octileDistance = octileDistance(nextX, nextY,goalX, goalY);                    
                    nextNode.priority = distance + octileDistance;
                    if(nodeExists) {
                        heap.remove(nextNode);
                    }              
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
