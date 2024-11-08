package com.orasaari;

import java.awt.Point;
import java.util.List;

interface Pathfinder {

    abstract List<Node> navigate(GridMap map, Point start , Point finish );
    
    
}
