package com.orasaari;

import java.awt.Point;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Implementation of the A* algorith. 
 * 
 * This is a copy of Dijkstra with only minor modifications in the the navigate function: 
 * heap is prioritized according to distance + heuristic function instead of just distance from the start node.
 * Heuristic function = octile distance from a node in the end of edge to the goal node.
 */
class AStarPathfinder implements Pathfinder {

     /** 
     * Internal class to decide the order of two nodes in the priority queue.  
     * The priority is the distance of the node from the starting point + heuristic function (octile distance to the goal)
    */
    private class ANodeComparator implements Comparator<Node> {        
        public int compare(Node n1, Node n2) {
            return Double.compare(n1.priority, n2.priority);
        }
    }

    /* 
     * @see com.orasaari.AStarPathfinder#navigate(com.orasaari.GridMap, java.awt.Point, java.awt.Point, boolean)
    */
    public Result navigate(GridMap map, Point start , Point finish) {
        return navigate(map, start, finish, false);
    }   

    /**
     * Implement the A* pathfinding.
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
    public Result navigate(GridMap map, Point start , Point goal, boolean cutCorners) {
            
        boolean[][] grid = map.getGrid(); 
        boolean[][][] travellability = map.getTraversability(cutCorners); // pre-calculated traversability to the adjacent nodes

        // Initialize the timer. Calculating travallebility of nodes is not included in performance evaluation.
        long startTime = System.currentTimeMillis();

        // Initialize the priority heap
        PriorityQueue<Node> heap = new PriorityQueue<Node>(new ANodeComparator());

        // Nodes are created only as needed and maintained in an array and re-used to minimize overhead.
        Node[][] nodeList = new Node[grid.length][grid[0].length];

        // Initialize the priority heap with the starting node
        Node currentNode = new Node(start.x, start.y);
        nodeList[start.x][start.y] = currentNode;
        currentNode.distance = 0;
        double octileDistance = MapUtils.octileDistance(start.x, start.y, goal.x, goal.y);
        currentNode.priority = octileDistance;
        heap.add(currentNode);           
        int numeOfEvaluatedNodes = 0;

        // Execute the pathfinding iteration
        while(!heap.isEmpty()) {
            currentNode = heap.poll();
            
            if(currentNode.x == goal.x && currentNode.y == goal.y) {
                break; // goal met, finish and collect results
            }
            
            if(currentNode.handled) {
                continue; // skip already handled nodes
            }

            currentNode.handled = true; // handle node & mark it as handled
            numeOfEvaluatedNodes++;     // number of evaluated nodes is additional information for performance evaluation

            // Iterate over the potential edges of the current node. 
            for(int i=0; i<8; i++) {

                if(!travellability[currentNode.x][currentNode.y][i]) {
                    continue; // blocked edger or ouside grid, skip
                }

                int nextNodeX = currentNode.x + MapUtils.MOVES[i][0];
                int nextNodeY = currentNode.y + MapUtils.MOVES[i][1];

                // Check if we have already created a node object for the particular location. If yes, re-use it.
                boolean existingNode = true;
                Node nextNode = nodeList[nextNodeX][nextNodeY];
                if(nextNode == null) {
                    existingNode = false;
                    nextNode = new Node(nextNodeX, nextNodeY);
                    nodeList[nextNodeX][nextNodeY] = nextNode;
                }

                // Calculate priority = distance from the start + heuritsic function (octile distance to the goal)
                double edgeWeight = MapUtils.WEIGHTS[i]; // 1 for straight, sqrt(2) for diagonal
                double distance = currentNode.distance + edgeWeight;
                if(distance < nextNode.distance) {                    
                    nextNode.distance = distance;
                    octileDistance = MapUtils.octileDistance(nextNodeX, nextNodeY, goal.x, goal.y);                    
                    nextNode.priority = distance + octileDistance;

                    // PriorityQueue does seem to automatically re-ordering the elements when the priority changes.
                    // This came up just once in hundreds of runs but needs to be handled by removing and adding back the node.
                    // This carries some overhead but there is no easy way around it.      
                    if(existingNode) {
                        heap.remove(nextNode);
                    }              
                    heap.add(nextNode);
                    nextNode.previous = currentNode;
                }                
            }
        }

        // Iteration finished, collect results and return
        long finishTime = System.currentTimeMillis();
        boolean success = currentNode.x == goal.x && currentNode.y == goal.y;
        Result result = MapUtils.collectResults(currentNode, startTime, finishTime, numeOfEvaluatedNodes, MapUtils.ALGORITHM_ASTAR, success);
        return result;
    }  
}
