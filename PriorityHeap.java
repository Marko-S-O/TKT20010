package com.orasaari;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityHeap extends PriorityQueue<JPSNode> {

    /** 
     * Comparator for queue elements.
    */
    private static class NodeComparator implements Comparator<JPSNode> {        
        public int compare(JPSNode n1, JPSNode n2) {
            return Double.compare(n1.priority, n2.priority);
        }
    }

    public PriorityHeap() {
        super(new NodeComparator());
    }

}