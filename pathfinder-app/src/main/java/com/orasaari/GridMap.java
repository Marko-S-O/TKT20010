package com.orasaari;

/**
* A  representation class for a 2D grid map. To maximize peformance of the pathfinding,
* boolean array is used for representation.
*/
public class GridMap {

    private boolean[][] grid;
    private int width = -1;
    private int height = -1;
    boolean[][][] traversability;

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
 
    /** 
     * Get the array representation of the map 
    */
    boolean[][] getGrid() {
        return this.grid;
    }

    /**
     * To be able to efficiently utilize Moving AI Lab 2D grid maps, we need to adjust travellaibility rule: If we are not allowed to 
     * cut corners, we need to check that the diagonal move is free in the both sides even if the next diagonal node is free.
     * 
     * @see https://www.movingai.com/benchmarks/street/index.html
     * 
     * @param   cutCorners  If true, the diagonal move is allowed only if the vertical and horizontal nodes towards the moving direction are free.
     * 
     * @return  TEraversability: a 3D array of boolean values indicating travellability to a direction from a node. Directions are defined im MapUtil.MOVES.
    */
    boolean[][][] getTraversability(boolean cutCorners) {
        if(traversability == null) {
            boolean[][][] traversability = new boolean[width][height][8];
            for(int i=0; i<width; i++) {
                for(int j=0; j<height; j++) {
                    if(grid[i][j]) {
                        for(int k=0; k<8; k++) {
                            int directionX = MapUtils.MOVES[k][0];
                            int directionY = MapUtils.MOVES[k][1];
                            int nextNodeX = i + directionX;
                            int nextNodeY = j + directionY;
                            if(nextNodeX < 0 || nextNodeY < 0 || nextNodeX >= width || nextNodeY >= height) {                               
                                traversability[i][j][k] = false;  // The node is outside the map -> false
                            } else if(!grid[nextNodeX][nextNodeY]) {                                
                                traversability[i][j][k] = false; // The node is blocked -> always false
                            } else if(directionX == 0 || directionY == 0 || cutCorners) {
                                traversability[i][j][k] = true; // cutting corners
                            } else {
                                // Cutting corners is not allowed -> either of the two adjacent nodes towards the moving direction can block the movement
                                traversability[i][j][k] = grid[i+directionX][j] && grid[i][j+directionY]; 
                            }
                        }
                    }
                }  
            }
            // save the traversability so that it does not have to be calculated again in perforamnce evaluation
            this.traversability = traversability; 
        }
        return traversability;
    }


    /**
     * @return  The width of the map, or -1 if the map is not initialized.
     */
    int getWidth() {
        return this.width;
    }

    /**
     * @return  The height of the map, or -1 if the map is not initialized.
     */
    int getHeight() {
        return this.height;
    }

    /** 
     * An utility method to create a random map. Used only to test visualization. 
     * 
     * @param blockProb     The probability of a node being blocked in randomization.
     */
    void randomize(double blockProb) {
        for(int i=0; i<width; i++) 
            for(int j=0; j<height; j++) {
                grid[i][j] = (Math.random() < blockProb ? false : true);
            }
    }

}
