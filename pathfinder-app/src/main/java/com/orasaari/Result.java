package com.orasaari;

import java.util.List;

/** 
 * A wrapper class to cover all the necessary result data for a single pathfinding run.
*/
public class Result {

    boolean success; 
    List<Node> path;
    long startTime;
    long finishTime;
    long duration;

    /** Number of nodes in the found shortest paths. For A, Dijkstra, this is equal to pixels on the grid. For JPS, the number may differ because of the jumps. */ 
    int numOfPathNodes; 

    /** Number of nodes that were taken from the open list (priority heap) by the pathfinding algorithm */
    int numeOfEvaluatedNodes;

    /** Int code of the algorithm. Codes are defined in class MapUtil. */
    int algorithm;

    /** Distance between start and finish nodes (pixels) in the grid. */
    double distance;

    public String toString() {
        return "Result:: distance: " + distance + " path lenght: " + numOfPathNodes + ", duration: " + duration + ", eval. nodes: " + numeOfEvaluatedNodes + ", algorithm: " + algorithm + ", success: " + success;
    }
}
