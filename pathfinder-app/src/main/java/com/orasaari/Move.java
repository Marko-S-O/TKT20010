package com.orasaari;

    /* 
     * A simple wrapper class for x and y coordinates of moving direction. 
     * This class only exists to make JPS code more readable.
    */
    class Move {
        int directionX; // x coordinate of the moving direction (1, 0, -1)
        int directionY; // y coordinate of the moving direction (1, 0, -1)

        Move(int directionX, int directionY) {
            this.directionX = directionX;
            this.directionY = directionY;
        }
    }
