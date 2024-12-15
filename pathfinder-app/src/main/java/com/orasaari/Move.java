package com.orasaari;

    /* 
     * A simple wrapper class for x and y coordinates of moving direction. 
     * This class only exists to make JPS code more readable.
    */
    class Move {

        static final int LEFT = 0;
        static final int RIGHT = 1;
        static final int UP = 2;    
        static final int DOWN = 3;
        static final int LEFT_UP = 4;    
        static final int RIGHT_DOWN = 5;
        static final int LEFT_DOWN = 6;
        static final int RIGHT_UP = 7;

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

        /** Moving directions are running around the clock from north-east to north. Thes map to the GridMap traversabilty table indices. */
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

        /** Distances of corresponding MOVES. Intended to simplify code and make algorithms faster. Used by A* and Dijkstra */
        static final double[] EDGE_WEIGHTS = {1, 1, 1, 1, MapUtils.SQRT2, MapUtils.SQRT2, MapUtils.SQRT2, MapUtils.SQRT2};

        int directionX; // x coordinate of the moving direction (1, 0, -1)
        int directionY; // y coordinate of the moving direction (1, 0, -1)

        Move(int directionX, int directionY) {
            this.directionX = directionX;
            this.directionY = directionY;
        }
    }
