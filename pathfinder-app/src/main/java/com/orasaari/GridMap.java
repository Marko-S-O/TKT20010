/**
* An abstract representation class for a 2D grid map.
*/
package com.orasaari;


public class GridMap {

    static final byte PIXEL_STATUS_BLOCKED = 0;
    static final byte PIXEL_STATUS_FREE = 1;
    static final byte PIXEL_STATUS_TRIED = 2;
    static final byte PIXEL_STATUS_ROUTE = 3;
    static final byte PIXEL_STATUS_ENDPOINT = 4;

    private boolean[][] grid;
    private int width;
    private int height;

    GridMap(int width, int height) {
        grid = new boolean[width][height];
        this.width = width;
        this.height = height;
    }

    GridMap(boolean[][] grid) {
        this.grid = grid;
        this.width = grid.length;
        this.height = grid[0].length;
    }

    /** Utility method to create a map to test visualization */
    void randomize(double blockProb) {
        for(int i=0; i<width; i++) 
            for(int j=0; j<height; j++) {
                grid[i][j] = (Math.random() < blockProb ? false : true);
            }
    }

    boolean[][] getGrid() {
        return grid;
    }

    int getWidth() {
        return this.width;
    }

    int getHeight() {
        return this.height;
    }

}
