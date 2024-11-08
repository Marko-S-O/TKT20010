package com.orasaari;

/** 
 * A simple wrapper class describing an edge in an adjacency list.
*/
class Edge {
    int x;  // x cordinate of a connecting node
    int y;  // y coordinate of a connecting node
    double weight; // distance to a connecting node

    Edge(int x, int y, double weight) {
        this.x = x;
        this.y = y;
        this.weight = weight;
    }
}
