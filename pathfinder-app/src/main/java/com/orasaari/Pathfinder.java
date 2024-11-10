package com.orasaari;

import java.awt.Point;

interface Pathfinder {

    abstract Result navigate(GridMap map, Point start , Point finish);

}
