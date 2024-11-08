package com.orasaari;

import java.awt.Point;

interface Pathfinder {

    abstract Route navigate(GridMap map, Point start , Point finish );
    
    
}
