package com.orasaari;

/** 
 * A data class wrapping fields needed for path finding algorithms.
*/
public class Node {

    int x;
    int y;
    double distance = Double.MAX_VALUE;        
    boolean handled = false;
    Node previous = null;
    double priority;
    double heuristic;
    boolean jumpPassthrough = false;
    int[] arrivalDirection;

    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        String s = "Node:: x: " + x + ", y: " + y + ", d: " + distance + ", h: " + heuristic + ", p: " + priority;
        return s;
    }

    @Override
    public boolean equals(Object o) {
        Node n = (Node)o; 
        return n.x == this.x && n.y == this.y;
    }
}
