/**
* An abstract representation class for a 2D grid map.
*/
package com.orasaari;

import java.util.ArrayList;

public class GridMap {

    static final byte PIXEL_STATUS_BLOCKED = 0;
    static final byte PIXEL_STATUS_FREE = 1;
    static final byte PIXEL_STATUS_TRIED = 2;
    static final byte PIXEL_STATUS_ROUTE = 3;
    static final byte PIXEL_STATUS_ENDPOINT = 4;

    private byte[][] grid;
    private int width;
    private int height;
    private Edge[][][] adjacencyList = null;

    GridMap(int width, int height) {
        grid = new byte[width][height];
        this.width = width;
        this.height = height;
    }

    GridMap(byte[][] grid) {
        this.grid = grid;
        this.width = grid.length;
        this.height = grid[0].length;
    }

    /** Utility method to create a map to test visualization */
    void randomize(double blockProb) {
        for(int i=0; i<width; i++) 
            for(int j=0; j<height; j++) {
                grid[i][j] = (byte) (Math.random() < blockProb ? 0 : 1);
            }
    }

    byte[][] getGrid() {
        return grid;
    }

    int getWidth() {
        return this.width;
    }

    int getHeight() {
        return this.height;
    }

    /* 
     * Create a pre-calculated, sorted adjancency list to be used by the pathfinding algorithms.
     * In the returned table, the first index is the own x-coordinate of the node, second y-coordinate the third the index in the edge list of node.
    */
    Edge[][][] getAdjancencyList() {

        // Ensure that this is calculated only once
        if(adjacencyList != null) {
            return adjacencyList;
        }

        long startTime = System.currentTimeMillis();

        Edge[][][] edges = new Edge[width][height][];


        for(int i=0; i<this.width; i++) {
            for(int j=0; j<this.height; j++) {
                ArrayList<Edge> list = new ArrayList<Edge>(8);

                // edge straight to the left
                if(i>0 && grid[i-1][j] != 0) {
                    list.add(new Edge(i-1, j, 1));
                }

                // edge to the right
                if(i<width-1 && grid[i+1][j] != 0) {
                    list.add(new Edge(i+1, j, 1));
                }

                // upwards
                if(j>0 && grid[i][j-1] != 0) {
                    list.add(new Edge(i, j-1, 1));
                }

                // downwards
                if(j<height-1 && grid[i][j+1] != 0) {
                    list.add(new Edge(i, j+1, 1));
                }

                // left-up
                if(i>0 && j>0 && grid[i-1][j-1] != 0) {
                    list.add(new Edge(i-1, j-1, MapUtil.SQRT2));
                }

                // right-up
                if(i<width-1 && j>0 && grid[i+1][j-1] != 0) {
                    list.add(new Edge(i+1, j-1, MapUtil.SQRT2));
                }

                // left-down
                if(i>0 && j<height-1 && grid[i-1][j+1] != 0) {
                    list.add(new Edge(i-1, j+1, MapUtil.SQRT2));
                }

                // right-down
                if(i<width-1 && j<height-1 && grid[i+1][j+1] != 0) {
                    list.add(new Edge(i+1, j+1, MapUtil.SQRT2));
                }

                edges[i][j] = list.toArray(new Edge[0]);
            }
        }

        System.out.println("calculating edges took " + (System.currentTimeMillis() - startTime) + " ms" );

        this.adjacencyList = edges;
        return edges;

    }
}
