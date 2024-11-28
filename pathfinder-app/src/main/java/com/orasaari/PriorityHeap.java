package com.orasaari;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityHeap extends PriorityQueue<Node> {

    /** 
     * Comparator for queue elements.
    */
    private static class NodeComparator implements Comparator<Node> {        
        public int compare(Node n1, Node n2) {
            return Double.compare(n1.priority, n2.priority);
        }
    }

    public PriorityHeap() {
        super(new NodeComparator());
    }

}
