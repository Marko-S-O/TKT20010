package com.orasaari;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

/** A class representing a graphical view to a grid map */
class GridMapView extends JPanel {

    private static final int INITIAL_PIXEL_SIZE = 2; // number of physical pixels on the screen used to represent a pixel in a map

    private static final Color COLOR_BLOCKED = Color.black;
    private static final Color COLOR_FREE = Color.white;
    private static final Color COLOR_JUMP = Color.red;
    private static final Color[] PATH_COLORS = {Color.blue, Color.green, Color.pink};

    private boolean[][] grid; 
    private int x0, y0, x1, y1; // Limits of the rectangle to be re-drawn
    private List<List<Node>> paths = null;
    private int pixelSize = INITIAL_PIXEL_SIZE;

    GridMapView(GridMap map) {
        setMap(map);
    }

    /** 
     * Set the map and paint it 
     */
    void setMap(GridMap map) {
        this.grid = map.getGrid().clone(); 
        Dimension size = new Dimension(grid.length * pixelSize, grid[0].length * pixelSize);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);   
        paintGrid();
    }

    /** 
     * Set the pixel size to allow scaling the map in real time.
    */
    void setPixelSize() {
        this.pixelSize = INITIAL_PIXEL_SIZE;
        paintGrid();
    }

    /** 
     * Paint full grid. The initial idea was that we could paint only those parts of the grid that have changed 
     * (like the paths), but it did not work. Because the UI is not in the focus of this course, we just leave this it as-is.
     * 
    */
    void paintGrid() {
        this.paths = null;
        this.x0 = 0;
        this.y0 = 0;
        this.x1 = grid.length;
        this.y1 = grid[0].length;        
        repaint();
    }

    /** 
     * Paint paths on the map and refresh the view.
     * 
     * @param paths  Lists of Nodes to be painted
     */
    void paintPaths(List<List<Node>> paths) {
        this.x0 = 0;
        this.y0 = 0;
        this.x1 = grid.length;
        this.y1 = grid[0].length;  
        this.paths = paths;
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
                g2d.fillRect(i * pixelSize, j * pixelSize, pixelSize, pixelSize);
            }
        }

        if(paths != null) {
            int i = 0;
            for(List<Node> path : paths) {
                for(Node node : path) {   
                    g2d.setColor(node.jumpPassthrough ? COLOR_JUMP : PATH_COLORS[i]);                 
                    g2d.fillRect(node.x * pixelSize, node.y * pixelSize, pixelSize, pixelSize);
                }
                i++;
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(grid.length * pixelSize, grid[0].length * pixelSize);
    }

}
