/**
* A  representation class for a 2D grid map.
* This is just a data container without functionality.
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
 
    boolean[][] getGrid() {
        return this.grid;
    }

    /**
     * To be able to efficiently utilize the available pre-calculated scenarios for 2D city maps, we need to adjust travellaibility rule:
     * If we are not allowed to cut corners, we need to check that the diagonal move is not blocked in the both sides even if the next node is free.
     * @see https://www.movingai.com/benchmarks/street/index.html
     * 
     * @param   cutCorners  if true, the diagonal move is allowed only if the vertical and horizontal nodes towards the moving direction are free.
     * 
     * @return  travellability  a 3D array of boolean values indicating travellability to a direction from a node. Directions are defined im MapUtil.MOVES.
    */
    boolean[][][] getTraversability(boolean cutCorners) {
        boolean[][][] traversability = new boolean[width][height][8];
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                if(grid[i][j]) {
                    for(int k=0; k<8; k++) {
                        int directionX = MapUtil.MOVES[k][0];
                        int directionY = MapUtil.MOVES[k][1];
                        int nextNodeX = i + directionX;
                        int nextNodeY = j + directionY;
                        if(nextNodeX < 0 || nextNodeY < 0 || nextNodeX >= width || nextNodeY >= height) {
                            // The node is outside the map -> false
                            traversability[i][j][k] = false;
                        } else if(!grid[nextNodeX][nextNodeY]) {
                            // The node is blocked -> always false
                            traversability[i][j][k] = false;
                        } else if(directionX == 0 || directionY == 0 || cutCorners) {
                            // Verical or horizontal move or cutting corners is allowed -> always true
                            traversability[i][j][k] = true;
                        } else {
                            // Cutting corners is not allowed -> either of the two adjacent nodes towards the moving direction can block the movement
                            traversability[i][j][k] = grid[i+directionX][j] && grid[i][j+directionY]; 
                        }
                    }
                }
            }  
        }
        return traversability;
    }


    int getWidth() {
        return this.width;
    }

    int getHeight() {
        return this.height;
    }

    /** 
     * Utility method to create a random map. Used only to test visualization. 
     */
    void randomize(double blockProb) {
        for(int i=0; i<width; i++) 
            for(int j=0; j<height; j++) {
                grid[i][j] = (Math.random() < blockProb ? false : true);
            }
    }

}
