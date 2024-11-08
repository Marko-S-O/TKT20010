package com.orasaari;

import java.awt.Point;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

class DijkstraPathfinder implements Pathfinder {

    private class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return Double.compare(n1.distance, n2.distance);
        }
    }

    public List<Node> navigate(GridMap map, Point start, Point finish) {
            
            // Init heap
            PriorityQueue<Node> heap = new PriorityQueue<Node>(new NodeComparator());

            Edge[][][] edges = map.getAdjancencyList();            
            byte[][] grid = map.getGrid();
            boolean[][] handledList = new boolean[grid.length][grid[0].length];
            Node[][] nodeList = new Node[grid.length][grid[0].length];

            // Init iteration
            Node currentNode = new Node(start.x, start.y);
            currentNode.distance = 0;
            heap.add(currentNode);           

            // Execute iteration
            while(!heap.isEmpty()) {

                currentNode = heap.poll();

                // We are only calculating the the route so we can finish when in the end node
                if(currentNode.x == finish.x && currentNode.y == finish.y) {
                    break;
                }

                // check, if the current node is in the handled list
                if(handledList[currentNode.x][currentNode.y]) {
                    continue;
                }
                handledList[currentNode.x][currentNode.y] = true;

                // Iterate over edges of the current node
                Edge[] currentNodeEdges = edges[currentNode.x][currentNode.y];
                for(int i=0; i<currentNodeEdges.length; i++) {
                    Edge edge = currentNodeEdges[i];

                    Node nextNode = nodeList[edge.x][edge.y];
                    if(nextNode == null) {
                        nextNode = new Node(edge.x, edge.y);
                    }
                    double newDistance = currentNode.distance + edge.weight;
                    if(newDistance < nextNode.distance) {
                        nextNode.distance = newDistance;
                        heap.add(nextNode);
                        nextNode.previous = currentNode;
                    }
                }
            }

            // Finished with the search. See if we succeedede finding the route.
            // If yes, reconstruct the route to be viewed in the UI.
            if(currentNode.x == finish.x && currentNode.y == finish.y) {
                System.out.println("Found the route: " + currentNode.distance);
                List<Node> route = MapUtil.collectRoute(currentNode);
                return route;
            } else {
                System.out.println("Did not find the route. Last searched node: (" + currentNode.x + ", " + currentNode.y + ")");
            }
            return null;
        }    
}
