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
    private static final Color COLOR_TRIED = Color.gray;
    private static final Color COLOR_PATH = Color.red;
    private static final Color COLOR_ENDPOINT = Color.green;
    private static final Color[] COLOR_CODES = {COLOR_BLOCKED, COLOR_FREE, COLOR_TRIED, COLOR_PATH, COLOR_ENDPOINT};

    //private GridMap map; // grid map (abstract map, not graphical presentation canvas)
    private byte[][] grid; 
    private int x0, y0, x1, y1; // Limits of the rectangle to be re-drawn
    
    GridMapView(GridMap map) {
        setMap(map);
    }

    void setMap(GridMap map) {
        //this.map = map;
        this.grid = map.getGrid().clone(); // map is used for calculation as well, can't manipulate here without cloning
        Dimension size = new Dimension(grid.length * PIXEL_SIZE, grid[0].length * PIXEL_SIZE);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);   
        paintGrid();
    }

    /** Paint full grid */
    void paintGrid() {
        this.x0 = 0;
        this.y0 = 0;
        this.x1 = grid.length;
        this.y1 = grid[0].length;        
        repaint();
    }


    /** 
     * Paint an individual pixel 
     * At the moment, this is not working. All the pixels need to be repainted to preserve the image.
     * Need to evaluate if other components but JPanel would be suitable as a drawing canvas.
    */
    void paintPixel(int x, int y, byte status) {
        System.out.println("PaintPixel: " + x + ", " + y + ", " + status);
        x0 = x;
        x1 = x0 + 1;
        y0 = y;
        y1 = y0 + 1;
        grid[x][y] = status;
        
        repaint();
    }

    void paintPath(List<Node> path) {
        Iterator<Node> iter = path.iterator();
        while(iter.hasNext()) {
            Node node = iter.next();
            grid[node.x][node.y] = GridMap.PIXEL_STATUS_ROUTE;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        x0 = 0; x1 = grid.length; y0 = 0; y1 = grid[0].length;
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
        return new Dimension(grid.length * PIXEL_SIZE, grid[0].length * PIXEL_SIZE);
    }

}
