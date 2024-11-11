package com.orasaari;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;

/** A class representing a graphical view to a grid map */
class GridMapView extends JPanel {

    private static final int PIXEL_SIZE = 2; // number of physical pixels on the screen used to represent a pixel in a map

    private static final Color COLOR_BLOCKED = Color.black;
    private static final Color COLOR_FREE = Color.white;
    private static final Color COLOR_PATH = Color.red;

    //private GridMap map; // grid map (abstract map, not graphical presentation canvas)
    private boolean[][] grid; 
    private int x0, y0, x1, y1; // Limits of the rectangle to be re-drawn
    private List<Node> path = null;
    
    GridMapView(GridMap map) {
        setMap(map);
    }

    void setMap(GridMap map) {
        this.grid = map.getGrid().clone(); 
        Dimension size = new Dimension(grid.length * PIXEL_SIZE, grid[0].length * PIXEL_SIZE);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);   
        paintGrid();
    }

    /** Paint full grid */
    void paintGrid() {
        this.path = null;
        this.x0 = 0;
        this.y0 = 0;
        this.x1 = grid.length;
        this.y1 = grid[0].length;        
        repaint();
    }

    void paintPath(List<Node> path) {
        this.x0 = 0;
        this.y0 = 0;
        this.x1 = grid.length;
        this.y1 = grid[0].length;  
        this.path = path;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        x0 = 0; x1 = grid.length; y0 = 0; y1 = grid[0].length;
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for(int i=x0; i<x1; i++) {
            for(int j=y0; j<y1; j++) {
                g2d.setColor(grid[i][j] ? COLOR_FREE : COLOR_BLOCKED);
                g2d.fillRect(i * PIXEL_SIZE, j * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
            }
        }

        if(path != null) {
            g2d.setColor(COLOR_PATH);
            Iterator<Node> iter = path.iterator();
            while(iter.hasNext()) {
                Node node = iter.next();
                g2d.fillRect(node.x * PIXEL_SIZE, node.y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);

            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(grid.length * PIXEL_SIZE, grid[0].length * PIXEL_SIZE);
    }

}
