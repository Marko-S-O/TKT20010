package com.orasaari;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/** A class representing a graphical view to a grid map */
class GridMapView extends JPanel {

    private static final int PIXEL_BLOCKED = 0;
    private static final int PIXEL_FREE = 1;
    private static final int PIXEL_PASSED = 2;
    private static final int PIXEL_SIZE = 3;
    private static final Color COLOR_BLOCKED = Color.black;
    private static final Color COLOR_FREE = Color.white;
    private static final Color COLOR_TRIED = Color.gray;
    private static final Color COLOR_PATH = Color.darkGray;
    private static final Color[] COLOR_CODES = {COLOR_BLOCKED, COLOR_FREE, COLOR_TRIED, COLOR_PATH};

    private GridMap map; // grid map (abstract map, not graphical presentation canvas)
    private byte[][] grid; 
    private int x0, y0, x1, y1; // Limits of the rectangle to be re-drawn
    
    GridMapView(GridMap map) {
        setMap(map);
    }

    void setMap(GridMap map) {
        this.map = map;
        this.grid = map.getGrid();
        Dimension size = new Dimension(grid.length * PIXEL_SIZE, grid[0].length * PIXEL_SIZE);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);   
        paintGrid();
    }

    /** Paint full grid */
    void paintGrid() {
        x0 = 0;
        y0 = 0;
        x1 = grid.length;
        y1 = grid[0].length;        
        repaint();
    }

    void paintRoute() {
        
    }

    /** Paint an individual pixel */
    void paintPixel(int x, int y, byte status) {
        x0 = x;
        x1 = x0 + 1;
        y0 = y;
        y1 = y0 + 1;
        grid[x][y] = status;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for(int i=x0; i<x1; i++) {
            for(int j=y0; j<y1; j++) {
                g2d.setColor(COLOR_CODES[grid[i][j]]);
                g2d.fillRect(i * PIXEL_SIZE, j * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(grid[0].length * PIXEL_SIZE, grid.length * PIXEL_SIZE);
    }

}
