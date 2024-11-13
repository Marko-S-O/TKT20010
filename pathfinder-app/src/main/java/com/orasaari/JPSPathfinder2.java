package com.orasaari;

import java.awt.Point;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Implementation of JPS algorith.  
 * This uses the implementation of A* as a basis with necessary modifications in the navigate function.
 */
class JPSPathfinder2 implements Pathfinder {

    private static final int[][] MOVES = {{-1,0}, {1,0}, {0,-1}, {0,1}, {-1,-1}, {1,1}, {-1,1}, {1,-1}};
    //private static final double[] WEIGHTS = {1, 1, 1, 1, MapUtil.SQRT2, MapUtil.SQRT2, MapUtil.SQRT2, MapUtil.SQRT2};

    private int width, height;
    private boolean[][] grid;
    boolean[][] closed;

    private long jumpTime = 0;
    private long loopTime = 0;
    private long listTime = 0;

    /** 
     * Internal class to compare place of two nodes in the priority queue. The ch
    */
    private class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return Double.compare(n1.priority, n2.priority);
        }
    }

    private boolean isBlocked(int x, int y) {
        return x<0 || x>=width || y<=0 || y>=height || !grid[x][y];
    }

    private boolean isTravallable(int x, int y) {
        return x>=0 && x<width && y>=0 && y<height && grid[x][y];
    }

    /**
     * Move to a jump point: either we meet limits of the grid, arrive in the finishing point or face "forced neighbors"
    */
    private Node jump(Node currentNode, int directionX, int directionY, Point goal) {

        //System.out.println("jump: " + currentNode.x + ", " + currentNode.y + ", " + directionX + ", " + directionY);

        int jumpX = currentNode.x + directionX;
        int jumpY = currentNode.y + directionY;

        Node newNode = new Node(jumpX, jumpY);

        // if n is an obstacle or is outside the grid then return null
        if(isBlocked(jumpX, jumpY)) {
            return null;
        }

        // if n = g then return n
        if(jumpX == goal.x && jumpY == goal.y) {
            return newNode;
        }

        if(directionX !=0 && directionY !=0) { // moving diagonal

            // if ∃ n′ ∈ neighbours(n) s.t. n′ is forced then return n            
            if( (isTravallable(jumpX-directionX, jumpY+directionY) && !isBlocked(jumpX-directionX, jumpY)) ||
                (isTravallable(jumpX+directionX, directionY-directionY) && isBlocked(jumpX, jumpY-directionY)))  {
                return newNode;
            }

            // if ~d is diagonal then / for all i ∈ {1, 2} do / if jump(n, ~di, s, g) is not null then / return n            
            if(jump(newNode, directionX, 0, goal) != null || jump(newNode, 0, directionY, goal) != null) {
                return newNode;
            }            

        } else if(directionX != 0) { // moving horizontal
            if( (isTravallable(jumpX+directionX, jumpY+1) && isBlocked(jumpX, jumpY+1)) ||
                (isTravallable(jumpX+directionX, jumpY-1) && isBlocked(jumpX, jumpY-1))) {
                return newNode;
            }

        } else { // moving vertical
            if( (isTravallable(jumpX+1, jumpY+directionY) && isBlocked(jumpX+1, jumpY)) ||
                (isTravallable(jumpX-1, jumpY+directionY) && isBlocked(jumpX-1, jumpY))) {
                return newNode;        
            }
        }
        return jump(newNode, directionX, directionY, goal);
    }

    /**
     * Implement the JPS algoritm. 
    */
    public Result navigate(GridMap map, Point start, Point g) {
            
        long startTime = System.currentTimeMillis();

        PriorityQueue<Node> openList = new PriorityQueue<Node>(new NodeComparator());

        // save necessary data to instance variables to be used in jump point calcuations
        this.grid = map.getGrid(); 
        this.width = grid.length;
        this.height = grid[0].length;
        Node[][] nodes = new Node[width][height];
                
        closed = new boolean[width][height];

        // Require: x: current node, s: start, g: goal
        // successors(x) ← ∅
        Node startNode = new Node(start.x, start.y);
        startNode.distance = 0;
        startNode.heuristic = MapUtil.octileDistance(start.x, start.y, g.x, g.y); 
        startNode.priority = startNode.heuristic;
        openList.add(startNode);    
        nodes[startNode.x][startNode.y] = startNode;       

        Node node = null;
        int numOfEvaluatedNodes = 0;        

        while(!openList.isEmpty()) {
        
        long l0 = System.currentTimeMillis();

            //long l1 = System.currentTimeMillis();
            node = openList.poll();
            //listTime = listTime + System.currentTimeMillis() - l1;

            numOfEvaluatedNodes++;

            if(node.x==g.x && node.y==g.y) {
                break; // goal met
            }
            closed[node.x][node.y] = true;

            // for all n ∈ neighbours(x) do
            for(int i=0; i<8; i++) {          
                
                int directionX = MOVES[i][0];
                int directionY = MOVES[i][1];
              
                if(isBlocked(node.x + directionX, node.y + directionY) || closed[node.x+directionX][node.y+directionY]) {
                    continue;
                }

                // for all n ∈ neighbours(x) do / n ← jump(x, direction(x, n), s, g) / add n to successors(x)
                long t0 = System.currentTimeMillis();
                Node jumpNode = jump(node, directionX, directionY, g);
                jumpTime += (System.currentTimeMillis()-t0);
                if(jumpNode == null) {
                    continue;
                }
                
                jumpNode.previous = node;
                jumpNode.distance = node.distance + MapUtil.octileDistance(node.x, node.y, jumpNode.x, jumpNode.y);
                jumpNode.heuristic = MapUtil.octileDistance(jumpNode.x, jumpNode.y, g.x, g.y);
                jumpNode.priority = jumpNode.distance + jumpNode.heuristic;

                //System.out.println(jumpNode);

                long l2 = System.currentTimeMillis();
                if(nodes[jumpNode.x][jumpNode.y]==null || jumpNode.priority < nodes[jumpNode.x][jumpNode.y].priority) {                  
                    openList.add(jumpNode);
                    listTime = listTime + (System.currentTimeMillis()-l2);
                    nodes[jumpNode.x][jumpNode.y] = jumpNode;
                }
            }
            loopTime = loopTime + (System.currentTimeMillis()-l0);
        }

        long finishTime = System.currentTimeMillis();

        System.out.println("Jump time: " + jumpTime);
        System.out.println("Loop time: " + (loopTime-jumpTime));
        System.out.println("List time: " + listTime);

        boolean success = node.x == g.x && node.y == g.y;
        Result result = MapUtil.collectResults(node, startTime, finishTime, numOfEvaluatedNodes, MapUtil.ALGORITHM_DIJKSTRA, success);
        return result;
    }  
}
