package com.orasaari;

/** 
 * A data wrapper class without functionality to collect necessary information 
 * of an individual node when it is handled by the pathfinders.
*/
public class Node implements Comparable<Node> {

    /** X and coordinate of the node. */
    int x, y;  

    /** Priority in the queue for the node (distanceFromStart + heuristic) */
    double priority;

    /** A link to the previous node to allow collecting the route once the goal has been found. */
    Node previousNode = null;

    /** Needed for visualization purposes only. Not used by algorithms. If true, a node is not a true path node but a pixel on the grid passed by a JPS jump. */
    boolean jumpPassthrough = false;

    /** Moving direction to the node in pathfinding (JPS). */
    int movingDirection;

    /* 
     * Init.
     * 
     * @param   x   x coordinate in the map.
     * @param   y   y coordinate in the map.
    */
    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /* 
     * Create a new node in Dijkstra/A* iteration.
     * 
     * @param   x   x coordinate in the map.
     * @param   y   y coordinate in the map.
    */
    Node(int x, int y, double priority, Node previousNode) {
        this.x = x;
        this.y = y;
        this.priority = priority;
        this.previousNode = previousNode;
    }

    /*
     * Comparator interface method for PriorityQueue.
     */
    public int compareTo(Node other) {
        if (this.priority < other.priority)
            return -1;
        else 
            return 1;  
    }
}
