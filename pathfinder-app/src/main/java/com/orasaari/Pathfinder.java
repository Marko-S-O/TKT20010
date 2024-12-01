package com.orasaari;

import java.util.ArrayList;
import java.util.List;

/** 
 * A common abstract parent class for pathfinder. Common code is collected to this class.
*/
abstract class Pathfinder {

    /**
     * the Pathfinder interface method.
     * 
     * @param map           The used for pathfinding
     * @param startX        X coordinate of the start point
     * @param startY        Y coodrinate of the start point
     * @param goalX         X coordinate of the goal
     * @param goalY         Y coordinate of the goal
     * 
     * @return              the Result object wrapping the pathfinding results
     */
    abstract PathfindingResult findPath(GridMap map, int startX , int startY, int goalX, int goalY);

    /**
     * Calculate octile distance between points (x0, y0) and (x1, y1).
     */
    double octileDistance(int x0, int y0, int x1, int y1) {
        int deltaX = Math.abs(x0 - x1);
        int deltaY = Math.abs(y0 - y1);
        double octileDistance = Math.min(deltaX, deltaY) * MapUtils.SQRT2 + Math.abs(deltaX - deltaY);
        return octileDistance;
    }

    /**
     * Collect the result of pathfinding. For JPS, fill the jumped parts of the grid are filled with jump passthrough nodes for visualization purposes.
     * 
     * @param   goalNode                The found end node, or the last evaluated node if the goal was not found.
     * @param   startTime               Start time of pathfinding (ms).
     * @param   finishTime              End time of pathfinding for performance evaluation (ms)
     * @param   evaluatedNodes          Number of nodes that the algorithm handled during pathfinding (dirrerent from the number of nodes in the path).
     * @param   algorithm               Numeric code of the algorithm, codes defined in class MapUtil.
     * @param   success                 True if the pathfinder reached the goal node, false otherwise.
     * 
     * @return  Collected results to be used by UI and performance evaluation.
     */
    static PathfindingResult collectResults(Node goalNode, long startTime, int evaluatedNodes, int algorithm, boolean success) {

        long finishTime = System.currentTimeMillis();
        PathfindingResult result = new PathfindingResult();

        List<Node> path = new ArrayList<Node>();
        path.add(goalNode);
        int numOfPathNodes = 1;
        double distance = goalNode.distanceFromStart;
        Node nextNode = goalNode;
        Node previousNode = goalNode.previousNode;

        while(previousNode != null) {
            numOfPathNodes += 1;

            // Check if there is a jump created by JPS on the path
            int deltaX = nextNode.x - previousNode.x;
            int deltaY = nextNode.y - previousNode.y;
            if(Math.abs(deltaX) > 1 || Math.abs(deltaY) > 1) {

                // fill the nodes between the jump end and start points with jump passthrough nodes
                int directionX = deltaX > 0 ? -1: (deltaX == 0 ? 0 : 1);
                int directiony = deltaY > 0 ? -1 : (deltaY == 0 ? 0 : 1);
                int middleNodeX = nextNode.x + directionX;
                int middleNodeY  = nextNode.y + directiony;
                while(! (middleNodeX==previousNode.x && middleNodeY==previousNode.y) ) {
                    Node middleNode = new Node(middleNodeX, middleNodeY);
                    middleNode.jumpPassthrough = true;
                    middleNodeX = middleNodeX + directionX;
                    middleNodeY  = middleNodeY + directiony;
                    path.add(middleNode);
                }
            }            
            path.add(previousNode);
            nextNode = previousNode;
            previousNode = previousNode.previousNode;
        }
        path = path.reversed(); // path was collected from goal to start -> reverse

        result.path = path;
        result.numberOfPathNodes = numOfPathNodes;
        result.startTime = startTime;
        result.goalTime = finishTime;
        result.searchDuration = finishTime - startTime;
        result.nodesEvaluated = evaluatedNodes;
        result.algorithmCode = algorithm;
        result.distance = distance;
        result.goalFound = success;
        
        return result;
    }

}
