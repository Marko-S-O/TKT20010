package com.orasaari;

/** 
 * A data wrapper class to collect necessary information of an individual node (pixel)
 * when it is in the handling of the pathfinder algorithm.
*/
public class Node {

    int x;
    int y;

    /** A flag indicating if the pathfinder has already evaluated this nedo */
    boolean handled = false;

    /** A link to the previous node to be able to collect the route once the goal has been found */
    Node previous = null;

    /** Distance from the starting */
    double distance = Double.MAX_VALUE;        

    /** Value of the heuristic function for the node. In practice, this is the octile distance from the node to the end point. 
     * However, in some kind of maps some other heuristic functions like euclidian or manhattan distance may be useful 
     */
    double heuristic;

    /** Priority in the open list (priority heap). For A and JPS, priority = distance + heuristic */
    double priority;

    /** For JPS, this field is used to collect the nodes on the path that the algorithm passes by a jump. Needed only for visualization of the route. */
    boolean jumpPassthrough = false;

    /** Arrival direction to the node. Needed by JPS. Value is mapping to MapUtils.MOVE_DIRECTIONS.
     */
    int arrivalDirection;

    /* 
     * Init.
     * 
     * @param   x   x coordinate in the 2D grid. X coordinates run from left to right.
     * @param   y   y coordinate in the 2D grid. Y coordinates run FROM TOP to bottom
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

    /** 
     * Equals can be used to check if the node is already in PriorityQueue (open list/priority heap).
     * However, using equals for this purpose was found out to be far too time-consuming and was replaced 
     * by maintaining a boolean array for the purpose in the algorithm.
    */
    @Override
    public boolean equals(Object o) {
        Node n = (Node)o; 
        return n.x == this.x && n.y == this.y;
    }
}
