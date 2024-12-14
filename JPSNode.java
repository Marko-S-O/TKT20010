package com.orasaari;

/** 
 * A data wrapper class without functionality to collect necessary information 
 * of an individual node when it is handled by the pathfinders.
*/
public class JPSNode extends Node {

    /** Distance from the start node on the evaluated route. */
    double distanceFromStart = Double.MAX_VALUE;        

    /* 
     * Init.
     * 
     * @param   x   x coordinate in the map.
     * @param   y   y coordinate in the map.
    */
    JPSNode(int x, int y) {
        super(x, y);
    }

    public int comparaTo(Node other) {
        return Double.compare(this.priority, other.priority);
    }
}