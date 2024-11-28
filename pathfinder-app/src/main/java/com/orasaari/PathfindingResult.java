package com.orasaari;

import java.util.List;

/** 
 * Pathdinding result data.
*/
public class PathfindingResult {

    /** True if the goal node was found */
    boolean goalFound; 

    /** List of linked nodes in the path */
    List<Node> path;

    /** System clock timestamp when the pathdinding started */
    long startTime;

    /** System clock timestamp when the goal was met */
    long goalTime;

    /** Duration of pathfinding, goalTime - startTime */
    long seachDuration;

    /** Number */
    int numberOfPathNodes; 

    /** Number of nodes opened in pathfinding */
    int nodesEvaluated;

    /** Numeric code of the altorithm. */
    int algorithmCode;

    /** Distance between start and and goal (or last evaluated node if goal was not found) */
    double distance;

}
