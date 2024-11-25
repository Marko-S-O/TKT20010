package com.orasaari;

/** 
 * A data wrapper class to collect necessary information of an individual node
 * when it is handled by the pathfinders.
*/
public class Node {

    /** x coordinate in the 2D grid. X coordinates run from left to right. */
    int x;  

    /** y coordinate in the 2D grid. Y coordinates run FROM TOP to bottom. */
    int y;  

    /** A flag indicating if the pathfinder has already evaluated this node. */
    boolean handled = false;

    /** A link to the previous node to allow collecting the route once the goal has been found. */
    Node previous = null;

    /** Distance from the start point. */
    double distance = Double.MAX_VALUE;        

    /** 
     * Value of the heuristic function for the node. Heuristic = octile distance from the node to the goal. 
     */
    double heuristic;

    /** Priority in the open list (priority heap). For A and JPS, priority = distance + heuristic */
    double priority;

    /** If true, a node is a true path node but a node on the grid passed by a JPS jump. Needed for visualization purposes. */
    boolean jumpPassthrough = false;

    /** Arrival direction to the node. Needed by JPS neighbour pruning. Value is mapping to MapUtils.MOVE_DIRECTIONS.*/
    int arrivalDirection;

    /* 
     * Init.
     * 
     * @param   x   x coordinate in the 2D grid. X coordinates run from left to right.
     * @param   y   y coordinate in the 2D grid. Y coordinates run from top to bottom.
    */
    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** 
     * String representation for debugging purposes 
    */
    public String toString() {
        String s = "Node:: x: " + x + ", y: " + y + ", d: " + distance + ", h: " + heuristic + ", p: " + priority;
        return s;
    }
}
