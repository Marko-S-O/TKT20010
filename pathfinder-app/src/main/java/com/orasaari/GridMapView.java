package com.orasaari;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

/** 
 * A grapical representation of a 2D grid map 
*/
class GridMapView extends JPanel {

    private static final int INITIAL_PIXEL_SIZE = 2; // number of physical pixels on the screen used to represent a pixel in a map

    private static final Color COLOR_BLOCKED = Color.black;
    private static final Color COLOR_FREE = Color.white;
    private static final Color COLOR_JUMP = Color.red;
    private static final Color[] PATH_COLORS = {Color.blue, Color.green, Color.pink};

    private boolean[][] grid; 
    private List<List<Node>> paths = null;
    private int pixelSize = INITIAL_PIXEL_SIZE;

    GridMapView(GridMap map) {
        setMap(map);
    }

    /** 
     * Set the map and paint it.
     * 
     * @param map  The map to be visualized
     */
    void setMap(GridMap map) {
        this.grid = map.getGrid().clone(); 
        Dimension size = new Dimension(grid.length * pixelSize, grid[0].length * pixelSize);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);   
        this.pixelSize = INITIAL_PIXEL_SIZE;
        paintGrid();

    }

    /** 
     * Set the pixel size used to visualize one map node.
     * 
     * @pixelSize  The number of physical pixels on the screen used to represent one map node.
    */
    void setPixelSize(int pixelSize) {
        this.pixelSize = pixelSize;
        paintGrid();
    }

    /** 
     * Paint full grid. 
    */
    void paintGrid() {     
        repaint();
    }

    /** 
     * Paint paths on the map and refresh the view.
     * 
     * @param paths  Lists of Nodes to be painted
     */
    void paintPaths(List<List<Node>> paths) {
        this.paths = paths;
        repaint();
    }

    /** 
     * Override the JPanel paint method to draw the grid and paths. 
     * Tried painting only changed pixels but it did not work out -> refreshing the whole grid.
    */
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for(int i=0; i<grid.length; i++) {
            for(int j=0; j<grid[0].length; j++) {
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

    /** 
     * Define the component size based on the map size and pixel size. This is critical for the main UI to get scaled correctly.
    */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(grid.length * pixelSize, grid[0].length * pixelSize);
    }

}
