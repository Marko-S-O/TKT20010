package com.orasaari;

/** 
 * A static utility class to implement util methods and store global constants.
 */
class MapUtils {

    static final double SQRT2 = Math.sqrt(2.0);
    static final int ALGORITHM_DIJKSTRA = 0;
    static final int ALGORITHM_ASTAR = 1;
    static final int ALGORITHM_JPS = 2;
    static final String[] ALGORITHM_NAMES = {"Dijkstra", "A-Star", "JPS"};
    static final int GRID_BLOCKED = 0;
    static final int LEFT = 0;
    static final int RIGHT = 1;
    static final int UP = 2;    
    static final int DOWN = 3;
    static final int LEFT_UP = 4;    
    static final int RIGHT_DOWN = 5;
    static final int LEFT_DOWN = 6;
    static final int RIGHT_UP = 7;

    // Directories of the used files. If deploying the code to another environment, these need to be adjusted accordingly.
    static final String MAP_DIRECTORY = "c:/users/ext/TKT20010/pathfinder-app/data/";
    static final String SCENARIO_DIRECTORY = "c:/users/ext/TKT20010/pathfinder-app/data/test/";    
    static final String STREET_MAP_DIRECTORY = "c:/users/ext/TKT20010/pathfinder-app/data/street-map/"; 

    /** Possible moving directions in the 2D grid. Intended to simplify code and make algorithms faster. Used by A* and Dijkstra */
    static final int[][] MOVES = {{-1,0}, {1,0}, {0,-1}, {0,1}, {-1,-1}, {1,1}, {-1,1}, {1,-1}};

    /** Distances of corresponding MOVES. Intended to simplify code and make algorithms faster. Used by A* and Dijkstra */
    static final double[] WEIGHTS = {1, 1, 1, 1, MapUtils.SQRT2, MapUtils.SQRT2, MapUtils.SQRT2, MapUtils.SQRT2};

    /** Moving directions are running around the clock from north-east to north. These are used by JPS but the order is the same as A* and mapping to the GridMap traversabilty table indices. */
    static final Move[] MOVE_DIRECTIONS = {
        new Move(-1,0),  // left
        new Move(1,0),   // right
        new Move(0,-1),  // up   
        new Move(0,1),   // right
        new Move(-1,-1), // left-up
        new Move(1,1),  // right-down
        new Move(-1,1), // left-down
        new Move(1,-1)   // right-up
    };

    /** Get the vertical components of the diagonal paths. For example, for right-down return down. */
    static final int[] VERTICAL_ONLY_COMPONENT = {
        -1,
        -1,
        -1,
        -1,
        UP,
        DOWN,
        DOWN,
        UP
    };

    /** Get the horizontal components of the diagonal paths. For example, for right-down return right. */
    static final int[] HORIZONTAL_ONLY_COMPONENT = {
        -1,
        -1,
        -1,
        -1,
        LEFT,
        RIGHT,
        LEFT,
        RIGHT,
    };
    
}
