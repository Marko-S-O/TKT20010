package com.orasaari;

import java.awt.Point;

/** 
 * A unified interface for pathfiding algorithms. Intended to make it easier to handle pathfinders 
 * in the control UI (class GridMapUI) and decouple the UI and algorithm.
*/
interface Pathfinder {
    abstract Result navigate(GridMap map, Point start , Point finish);
}
