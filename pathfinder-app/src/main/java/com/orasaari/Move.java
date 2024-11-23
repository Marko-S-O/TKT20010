package com.orasaari;

    /* 
     * Instead of using two-dimensional integer array to represent the moving directions like in Dijkstra, A*,
     * wrap move directions to a class to make code a bit more compact and readable in JPS.
    */
    class Move {
        int directionX;
        int directionY;
        Move(int directionX, int directionY) {
            this.directionX = directionX;
            this.directionY = directionY;
        }
        public String toString() {
            return "Move(" + directionX + "," + directionY + ")";
        }
    }
