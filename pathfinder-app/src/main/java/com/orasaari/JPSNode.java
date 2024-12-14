package com.orasaari;

/** 
 * A data wrapper class without functionality to collect necessary information 
 * of an individual node when it is handled by the pathfinders.
*/
public class JPSNode extends Node {


    /** Information if the node has been handled. */
    boolean handled = false;

    /** Distance from the start node on the evaluated route. */
    double distanceFromStart = Double.MAX_VALUE;        

    /** Priority in the queue for the node (distanceFromStart + heuristic) */
    double priority;

    /** If true, a node is not a true path node but a pixel on the grid passed by a JPS jump. Needed for visualization purposes. */
    boolean jumpPassthrough = false;

    /** Moving direction to the node in pathfinding (JPS). */
    int movingDirection;

    /* 
     * Init.
     * 
     * @param   x   x coordinate in the map.
     * @param   y   y coordinate in the map.
    */
    JPSNode(int x, int y) {
        super(x, y);
    }
}