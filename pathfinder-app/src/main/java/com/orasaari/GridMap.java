package com.orasaari;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
* The 2D grid map representation class.
*/
public class GridMap {

    private boolean[][] grid;
    private int width = -1;
    private int height = -1;
    boolean[][][] traversability;

    /*
     * Initialize a randomized map for testing purposes.
     * 
     * @param   width   width of the map 
     * @param   height  height of the map
     */
    GridMap(int width, int height, double blockProb) {
        grid = new boolean[width][height];
        this.width = width;
        this.height = height;
        if(blockProb < 100.0) 
            for(int i=0; i<width; i++) 
                for(int j=0; j<height; j++) 
                    grid[i][j] = (Math.random() < blockProb ? false : true);
        calculateTraversability();
    }

    /**
     * Intialize a map from a file.
     * 
     * @param filename  name of the .map file
     */
    GridMap(String filename) {
        this.grid = loadMapFile(filename);
        this.width = grid.length;
        this.height = grid[0].length;
        calculateTraversability();
    }
 
    
    /** 
     * Get the array representation of the map.
    */
    boolean[][] getGrid() {
        return this.grid;
    }

    /**
     * Calculate traversability to the neighbourg nodes. Only thight cornering rules are allowed anymore. 
    */
    private void calculateTraversability() {
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
                            } else {
                                // Cutting corners is not allowed -> either of the two adjacent nodes towards the moving direction can block the movement
                                traversability[i][j][k] = grid[i+directionX][j] && grid[i][j+directionY]; 
                            }
                        }
                    }
                }  
            }            
            this.traversability = traversability; 
        }
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
     * An utility method to create a random map. Used for testing. 
     * 
     * @param blockProb     The probability of a node being blocked in randomization.
     */
    void randomize(double blockProb) {
        for(int i=0; i<width; i++) 
            for(int j=0; j<height; j++) 
                grid[i][j] = (Math.random() < blockProb ? false : true);
    
        calculateTraversability();
    }

    /**
     * Evaluate if the map is traversable to the diection (directionX, directionY). 
     * Only applicable only for neighbourg nodes.
     * 
     * @param x             x coordinate of the node
     * @param y             y coordinate of the node
     * @param directionX    x component of the movement (-1, 0, 1)
     * @param directionYy   component of the movement (-1, 0, 1)
     * @return
     */
    boolean isTraversable(int x, int y, int directionX, int directionY) {        
        int directionCode = getDirection(directionX, directionY); 
        boolean inGrid = x>=0 && x<width && y>=0 && y<height;
        if(inGrid) {
            return traversability[x][y][directionCode];
        } else {
            return false;
        }        
    }

     /**
     * Evaluate if the map is traversable to the direction code that is mapping to the traversability matrix. 
     * This method is applicable only for the adjacent nodes.
     * 
     * @param x             x coordinate of the node
     * @param y             y coordinate of the node
     * @param directionCode direction code in traversability matrix
     * @return
     */
    boolean isTraversable(int x, int y, int directionCode) {        
        boolean inGrid = x>=0 && x<width && y>=0 && y<height;
        if(inGrid) {
            return traversability[x][y][directionCode];
        } else {
            return false;
        }        
    }

    /**
     * Return information if the grid in the map is blocked or outside grid
     * 
     * @param x the x coordinate in the grid    
     * @param y the y coordinate in the grid
     * 
     * @return  true, if (x,y) is outside the grid or blocked
     */
    boolean isBlocked(int x, int y) {
        return x<0 || x>=width || y<0 || y>=height || !grid[x][y];
    }

    /**
     * Map direction given in coordinates to the numeric direction code mapping to the traversability array indices. 
     * 
     * @param directionX    x component of the movement (-1, 0, 1)
     * @param directionY    y component of the movement (-1, 0, 1)
     * 
     * @return  The numeric direction code mapping to the traversability matrix.
     */
    int getDirection(int directionX, int directionY) {

        if(directionX == -1) {
            if(directionY == -1) {
                return MapUtils.LEFT_UP;
            } else if(directionY == 0) {
                return MapUtils.LEFT;
            } else {
                return MapUtils.LEFT_DOWN;
            }
        } else if(directionX == 0) {
            if(directionY == -1) {
                return MapUtils.UP;
            } else {
                return MapUtils.DOWN;
            }
        } else if(directionX == 1) {
            if(directionY == -1) {
                return MapUtils.RIGHT_UP;
            } else if(directionY == 0) {
                return MapUtils.RIGHT;
            } else {
                return MapUtils.RIGHT_DOWN;
            }
        } else {
            return -1;
        }
    }

    /** 
     * Load a a file from a disc. Only Moving AI lab .map files are supported. 
     * 
     * @param   file    The file in the local file system.
     * 
     * @return  The GridMap object representing the map, or null in case of any error.
    */
    private boolean[][] loadMapFile(String filename) {

        File file = new File(filename);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // Skip the type ("octile")
            String line2 = reader.readLine();
            int height = Integer.parseInt(line2.substring(line2.indexOf(' ')+1));
            String line3 = reader.readLine();
            int width = Integer.parseInt(line3.substring(line3.indexOf(' ')+1));
            reader.readLine(); // skip the map header ("map")

            boolean[][] grid = new boolean[width][height];
            String line;
            int j = 0;
            while((line = reader.readLine()) != null) {
                for(int i=0; i<line.length(); i++) {
                    grid[i][j] = line.charAt(i) == '.';
                }
                j++;
            }
                        
            return grid;

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
