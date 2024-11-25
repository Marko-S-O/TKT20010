package com.orasaari;

import java.awt.Point;

/** 
 * A common interface for pathfiding algorithms. Intended to make it easier to handle pathfinders 
 * in the control UI and decouple the algorithm from other components.
*/
interface Pathfinder {

    /**
     * The service method for pathfinding.
     * 
     * @param map           The 2D grid map where the path is searched.
     * @param start         The starting point of the path.
     * @param goal          The finishing point of the path.
     * @return              the Result object wrapping the pathfinding results
     */
    abstract Result navigate(GridMap map, Point start , Point finish);
}
