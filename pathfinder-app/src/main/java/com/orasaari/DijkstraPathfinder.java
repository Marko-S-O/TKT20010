package com.orasaari;

import java.awt.Point;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Implementation of Dijkstra algorith. There are two versions: the first one re-calculates the edges before iterating the route, 
 * the second one evaluates neighboring pixels on the fly. The second one seems to be performing significantly better with any tried map.
 */
class DijkstraPathfinder implements Pathfinder {

    private static final int[][] MOVES = {{-1,0}, {1,0}, {0,-1}, {0,1}, {-1,-1}, {1,1}, {-1,1}, {1,-1}};
    private static final double[] WEIGHTS = {1, 1, 1, 1, MapUtil.SQRT2, MapUtil.SQRT2, MapUtil.SQRT2, MapUtil.SQRT2};

    private class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return Double.compare(n1.distance, n2.distance);
        }
    }

    /**
     * Implement the interface method to find the shortest path from the point start to point end.
     */
    public Result navigate(GridMap map, Point start, Point finish, boolean usePrecalculculatedEdges) {
        return usePrecalculculatedEdges ? navigatePrecalculatedEdges(map, start, finish) : navigateWithoutPrecalculatedEdges(map, start, finish);
    }

    /**
     * Implement the pathfinder so that all the edges are calculated firts before iterating the route. This is slow and 
     * not supposed to be used except for testing and benchmarking purposes.
    */

    private Result navigatePrecalculatedEdges(GridMap map, Point start, Point finish) {
            
        System.out.println("DijstraPathfinder.navigate, start: " + start + ", finish: " + finish);

        long startTime = System.currentTimeMillis();

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
        int numeOfEvaluatedNodes = 0;

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
            numeOfEvaluatedNodes++;

            // Iterate over edges of the current node
            Edge[] currentNodeEdges = edges[currentNode.x][currentNode.y];
            for(int i=0; i<currentNodeEdges.length; i++) {
                Edge edge = currentNodeEdges[i];

                Node nextNode = nodeList[edge.x][edge.y];
                if(nextNode == null) {
                    nextNode = new Node(edge.x, edge.y);
                    nodeList[edge.x][edge.y] = nextNode;
                }
                double newDistance = currentNode.distance + edge.weight;
                if(newDistance < nextNode.distance) {
                    nextNode.distance = newDistance;
                    heap.add(nextNode);
                    nextNode.previous = currentNode;
                }
            }
        }

        long finishTime = System.currentTimeMillis();

        // Finished with the search. See if we succeedede finding the route.
        // If yes, reconstruct the route to be viewed in the UI.
        if(currentNode.x == finish.x && currentNode.y == finish.y) {
            System.out.println("Found the route: " + currentNode.distance);
            Result result = MapUtil.collectResults(currentNode, startTime, finishTime, numeOfEvaluatedNodes, MapUtil.ALGORITHM_DIJKSTRA, true);
            return result;
        } else {
            System.out.println("Did not find the route. Last searched node: (" + currentNode.x + ", " + currentNode.y + ")");
            Result result = MapUtil.collectResults(currentNode, startTime, finishTime, numeOfEvaluatedNodes, MapUtil.ALGORITHM_DIJKSTRA, false);
            return result;
        }
    }    

    /**
     * Implement the pathfinding alforitm without using pre-calculated edges. Edges to neighboring eight pixels
     * are evaluated on the fly.
    */
    public Result navigateWithoutPrecalculatedEdges(GridMap map, Point start, Point finish) {
            
        System.out.println("DijstraPathfinder.navigate, start: " + start + ", finish: " + finish);

        long startTime = System.currentTimeMillis();

        // Init heap
        PriorityQueue<Node> heap = new PriorityQueue<Node>(new NodeComparator());

        // Initialize iteration
        byte[][] grid = map.getGrid(); 
        boolean[][] handledList = new boolean[grid.length][grid[0].length];
        Node[][] nodeList = new Node[grid.length][grid[0].length];
        Node currentNode = new Node(start.x, start.y);
        currentNode.distance = 0;
        heap.add(currentNode);           
        int numeOfEvaluatedNodes = 0;

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
            numeOfEvaluatedNodes++;

            // Iterate over the potential edges of the current node
            for(int i=0; i<8; i++) {
                int nextNodeX = currentNode.x + MOVES[i][0];
                int nextNodeY = currentNode.y + MOVES[i][1];

                // check that the adjacent node (pixel) is inside the map limits
                if(nextNodeX < 0 || nextNodeY <0 || nextNodeX >= grid.length || nextNodeY >= grid[0].length) {
                    continue;
                }

                // check that the adjacent node is free to travel
                if(grid[nextNodeX][nextNodeY] == MapUtil.GRID_BLOCKED) {
                    continue;
                }

                // Check if we have already created a node object for the particular location. If yes, re-use it.
                Node nextNode = nodeList[nextNodeX][nextNodeY];
                if(nextNode == null) {
                    nextNode = new Node(nextNodeX, nextNodeY);
                    nodeList[nextNodeX][nextNodeY] = nextNode;
                }
                double edgeWeight = WEIGHTS[i];

                // check if our distance from the current node to the next node is smaller that the current shortest route to the next node
                double newDistance = currentNode.distance + edgeWeight;
                if(newDistance < nextNode.distance) {
                    nextNode.distance = newDistance;
                    heap.add(nextNode);
                    nextNode.previous = currentNode;
                }                
            }
        }

        long finishTime = System.currentTimeMillis();

        // Finished with the search. See if we succeedede finding the route.
        // If yes, reconstruct the route to be viewed in the UI.
        if(currentNode.x == finish.x && currentNode.y == finish.y) {
            System.out.println("Found the route: " + currentNode.distance);
            Result result = MapUtil.collectResults(currentNode, startTime, finishTime, numeOfEvaluatedNodes, MapUtil.ALGORITHM_DIJKSTRA, true);
            return result;
        } else {
            System.out.println("Did not find the route. Last searched node: (" + currentNode.x + ", " + currentNode.y + ")");
            Result result = MapUtil.collectResults(currentNode, startTime, finishTime, numeOfEvaluatedNodes, MapUtil.ALGORITHM_DIJKSTRA, false);
            return result;
        }
    }  
}
