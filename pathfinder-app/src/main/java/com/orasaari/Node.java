package com.orasaari;
public class Node {

    int x;
    int y;
    double distance = Double.MAX_VALUE;        
    boolean handled = false;
    Node previous = null;

    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
