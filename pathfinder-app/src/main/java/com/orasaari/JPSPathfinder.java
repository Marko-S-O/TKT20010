package com.orasaari;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Implementation of the JPS algorith.  
 * 
 * For the research paper, 
 * @see https://users.cecs.anu.edu.au/~dharabor/data/papers/harabor-grastien-aaai11.pdf
 */
class JPSPathfinder implements Pathfinder {

    private int width, height;
    private boolean[][] grid;
    boolean[][] closed;
    boolean[][][] traversability;
    PriorityQueue<Node> openList;

    /** 
     * Internal class to compare place of two nodes in the priority queue. Same as A*.
    */
    private static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return Double.compare(n1.priority, n2.priority);
        }
    }

    private boolean isBlocked(int x, int y) {
        return x<0 || x>=width || y<0 || y>=height || !grid[x][y];
    }

    private boolean isTraversable(int x, int y, int directionX, int directionY) {        
        int direction = MapUtil.getDirection(directionX, directionY);
        boolean traversable = x>=0 && x<width && y>=0 && y<height && traversability[x][y][direction];
        return traversable;
    }


    /**
     * Identify successors of a node by pruning the neighbours. To minimize overhead, 
     * we don't create new nodes here, just add directions to neighbours that need to be evaluated.
     * 
     * This splits the vertical part to more pieces than necessary but, at least for now, 
     * this is a clear way to understand the logic by writing the directions open one by one.
     * 
     * @param   arrivalDirection    direction from which the node was arrvived from, value mapping to MapUtil.MOVES
     * @param   currentNode         node currently under evaluation
     * 
     * @return  successors          list of prunded neighbours that needs to be evaluated
     */
    private List<Integer> pruneNeighbours(Node node) {

        if(node.arrivalDirection >= 0) {
            System.out.println("pruneNeighbours: " + node.x + ", " + node.y + ": " + MapUtil.MOVE_DIRECTIONS[node.arrivalDirection].directionX + ": " +MapUtil.MOVE_DIRECTIONS[node.arrivalDirection].directionY);
        }

        List<Integer> neighbours = new ArrayList<Integer>(8);

        if(node.arrivalDirection < 0) { // No direction, handle all neighbours       
            for(int i=0; i<8; i++) {
                if(traversability[node.x][node.y][i]) {
                    neighbours.add(Integer.valueOf(i));
                }
            }
        } else { 
            // we have a direction from which the node was arrived to, need to prune unnecessary neighbours
            int direction = node.arrivalDirection;
            int directionX = MapUtil.MOVE_DIRECTIONS[direction].directionX;
            int directionY = MapUtil.MOVE_DIRECTIONS[direction].directionY;

            // add the direction if can continue straight to the direction we came from
            if(traversability[node.x][node.y][direction]) { 
                neighbours.add(Integer.valueOf(direction));
            }

            // Handle forced neighbours cases to see if we also need to make it possible to fork the path.
            // For sure there is a shorter way to write this, but, at least for now, this is a clear way to understand the logic by writing directions open one by one.
            if(directionX == 1 && directionY == 1) { // moving down-right
                if(traversability[node.x][node.y][MapUtil.RIGHT]) // right open, need to add it as well
                    neighbours.add(Integer.valueOf(MapUtil.RIGHT));
                if(traversability[node.x][node.y][MapUtil.DOWN]) // down open, need to add it as well
                    neighbours.add(Integer.valueOf(MapUtil.DOWN));
                if(!traversability[node.x][node.y][MapUtil.RIGHT] && traversability[node.x][node.y][MapUtil.RIGHT_UP]) // right blocked, check up-right
                    neighbours.add(Integer.valueOf(MapUtil.RIGHT_UP));
                if(!traversability[node.x][node.y][MapUtil.DOWN] && traversability[node.x][node.y][MapUtil.LEFT_DOWN]) // down blocked, check down-left
                    neighbours.add(Integer.valueOf(MapUtil.LEFT_DOWN));

            } else if(directionX == 1 && directionY == -1) { // moving up-right
                if(traversability[node.x][node.y][MapUtil.RIGHT]) // right open, need to add it as well
                    neighbours.add(Integer.valueOf(MapUtil.RIGHT));
                if(traversability[node.x][node.y][MapUtil.UP]) // up open, need to add it as well
                    neighbours.add(Integer.valueOf(MapUtil.UP));
                if(!traversability[node.x][node.y][MapUtil.RIGHT] && traversability[node.x][node.y][MapUtil.RIGHT_UP]) // 
                    neighbours.add(Integer.valueOf(MapUtil.RIGHT_UP));
                if(!traversability[node.x][node.y][MapUtil.DOWN] && traversability[node.x][node.y][MapUtil.LEFT_DOWN]) // 
                    neighbours.add(Integer.valueOf(MapUtil.LEFT_DOWN));

            } else if(directionX == -1 && directionY == 1) { // moving down-left
                if(traversability[node.x][node.y][MapUtil.DOWN]) // right open, need to ad it as well
                    neighbours.add(Integer.valueOf(MapUtil.DOWN));
                if(traversability[node.x][node.y][MapUtil.LEFT]) // right open, need to ad it as well
                    neighbours.add(Integer.valueOf(MapUtil.LEFT));
                if(!traversability[node.x][node.y][MapUtil.RIGHT] && traversability[node.x][node.y][MapUtil.RIGHT_DOWN]) // 
                    neighbours.add(Integer.valueOf(MapUtil.RIGHT_DOWN));
                if(!traversability[node.x][node.y][MapUtil.LEFT] && traversability[node.x][node.y][MapUtil.LEFT_UP]) // 
                    neighbours.add(Integer.valueOf(MapUtil.LEFT_UP));                

            } else if(directionX == -1 && directionY == -1) { // moving up-left
                if(traversability[node.x][node.y][MapUtil.UP]) // 
                    neighbours.add(Integer.valueOf(MapUtil.UP));
                if(traversability[node.x][node.y][MapUtil.LEFT]) // 
                    neighbours.add(Integer.valueOf(MapUtil.LEFT));
                if(!traversability[node.x][node.y][MapUtil.RIGHT] && traversability[node.x][node.y][MapUtil.RIGHT_DOWN]) // 
                    neighbours.add(Integer.valueOf(MapUtil.RIGHT_DOWN));
                if(!traversability[node.x][node.y][MapUtil.LEFT] && traversability[node.x][node.y][MapUtil.LEFT_UP]) // 
                    neighbours.add(Integer.valueOf(MapUtil.LEFT_UP));                      

            } else if(directionX > 0) { // moving horizontally
                // with stricter cornering rules, we also need to: 1) allow 90 degree turns 2) check one node backwards for forced neighbours

                boolean previousUpBlocked = !traversability[node.x-directionX][node.y][MapUtil.UP];
                if(previousUpBlocked && isTraversable(node.x, node.y, directionX, -1)) // previous node up blocked, check up+direction
                    neighbours.add(Integer.valueOf(MapUtil.getDirection(directionX, -1)));
                if(previousUpBlocked && traversability[node.x][node.y][MapUtil.UP]) // previous node up blocked, check 90 degree turn up
                    neighbours.add(Integer.valueOf(MapUtil.UP)); // allow 90 degree turn up as well

                boolean previousDownBlocked = !traversability[node.x-directionX][node.y][MapUtil.DOWN];
                if(previousDownBlocked && isTraversable(node.x, node.y, directionX, 1)) // previous node down blocked, check down+direction
                    neighbours.add(Integer.valueOf(MapUtil.getDirection(directionX, 1)));
                if(previousDownBlocked && traversability[node.x][node.y][MapUtil.DOWN]) //  previous node up blocked, check 90 degree turn down
                    neighbours.add(Integer.valueOf(MapUtil.DOWN));                    
                    
            } else { // moving vertically
                boolean previousRightBlocked = !traversability[node.x][node.y-directionY][MapUtil.RIGHT];
                if(previousRightBlocked && isTraversable(node.x, node.y, 1, directionY)) // previous node right blocked, check right+direction
                    neighbours.add(Integer.valueOf(MapUtil.getDirection(1, directionY)));
                if(previousRightBlocked && traversability[node.x][node.y][MapUtil.RIGHT]) // previous node right blocked, check 90 degrees right
                    neighbours.add(Integer.valueOf(MapUtil.RIGHT));

                boolean previousLeftBlocked = !traversability[node.x][node.y-directionY][MapUtil.LEFT];
                if(previousLeftBlocked && isTraversable(node.x, node.y, -1, directionY)) // previous node left blocked, check left+direction
                    neighbours.add(Integer.valueOf(MapUtil.getDirection(-1, directionY)));
                if(previousLeftBlocked && traversability[node.x][node.y][MapUtil.LEFT]) // previous node left blocked, check 90 degrees left
                    neighbours.add(Integer.valueOf(MapUtil.LEFT));
    
            }
        }

        return neighbours;
    }

    // Algorithm 1 Identify Successors
    // Require: x: current node, s: start, g: goal
    private void identifySuccessors(Node currentNode, Point start, Point goal) {

        // successors(x) ← ∅        
        //List<Node> successors = new ArrayList<Node>();        
        // To minimize overheads, successors straight to the open list

        // neighbours(x) ← prune(x, neighbours(x))
        List<Integer> neighbourgs = pruneNeighbours(currentNode);
        
        System.out.println("identifySuccessors: " + currentNode);
        System.out.println("neighbours: " + neighbourgs.size());
        for(int i=0; i<neighbourgs.size(); i++) {
            System.out.println("neighbour: " + MapUtil.MOVE_DIRECTIONS[neighbourgs.get(i)]);
        }

        // for all n ∈ neighbours(x) do
        for(int i=0; i<neighbourgs.size(); i++) {
            
            int direction = neighbourgs.get(i);
            System.out.println("---------------------------------------------------------");            
            System.out.println("handle neighbour: " + MapUtil.MOVE_DIRECTIONS[direction]);

            // n ← jump(x, direction(x, n), s, g)
            Node jumpNode = jump(currentNode, direction, start, goal);

            if(jumpNode != null && !closed[jumpNode.x][jumpNode.y]) {
                // calculate travelled distance, heuristic, priority and add to the open list
                jumpNode.arrivalDirection = direction;
                jumpNode.previous = currentNode;
                jumpNode.distance = currentNode.distance + MapUtil.octileDistance(currentNode.x, currentNode.y, jumpNode.x, jumpNode.y);
                jumpNode.heuristic = MapUtil.octileDistance(jumpNode.x, jumpNode.y, goal.x, goal.y);
                jumpNode.priority = jumpNode.distance + jumpNode.heuristic;
                System.out.println("identifySuccessors, found jumpNode: " + jumpNode);
                openList.add(jumpNode);    
            }
        }
    }

    /**
     * Algorithm 2 Function jump
     * Require: x: initial node, ~d: direction, s: start, g: goal
    */
    private Node jump(Node currentNode, int arrivalDirection, Point start, Point goal) {

        System.out.println("jump: " + currentNode);
        System.out.println("arrivalDirection: " + MapUtil.MOVE_DIRECTIONS[arrivalDirection]);

        // n ← step(x, ~d)
        // if n is an obstacle or is outside the grid then return null
        if(currentNode==null || isBlocked(currentNode.x, currentNode.y)) {
            System.out.println("blocked, return null");
            return null;
        } else {
            System.out.println("not blocked, moving one");
        }

        Move move = MapUtil.MOVE_DIRECTIONS[arrivalDirection];
        int jumpX = currentNode.x + move.directionX;
        int jumpY = currentNode.y + move.directionY;
        int directionX = move.directionX;
        int directionY = move.directionY;

        Node newNode = new Node(jumpX, jumpY);
        System.out.println("created new node: " + newNode);  
        newNode.arrivalDirection = arrivalDirection;

        // if n = g then return n
        if(jumpX == goal.x && jumpY == goal.y) {
            System.out.println("goal met");
            return newNode;
        }

        // if ∃ n′ ∈ neighbours(n) s.t. n′ is forced then return n
        if (move.directionX != 0 && move.directionY != 0) { // Diagonal move
            System.out.println("Diagonal move " + jumpX + ", " + jumpY + ": " + move.directionX + ", " + move.directionY);
            System.out.println("isBlocked(jumpX - move.directionX, jumpY): " + isBlocked(jumpX - move.directionX, jumpY));
            System.out.println("isTraversable(jumpX, jumpY + move.directionY): " + isTraversable(jumpX, jumpY, 0, move.directionY));
            System.out.println("isBlocked(jumpX, jumpY - move.directionY): " + isBlocked(jumpX, jumpY - move.directionY));
            System.out.println("isTraversable(jumpX + move.directionX, jumpY): " + isTraversable(jumpX, jumpY, move.directionX, 0));
            
            if ((isBlocked(jumpX + move.directionX, jumpY) && isTraversable(jumpX, jumpY, 0, move.directionY)) || 
                (isBlocked(jumpX, jumpY + move.directionY) && isTraversable(jumpX, jumpY, move.directionX, 0)) || 
                (isBlocked(jumpX - move.directionX, jumpY) && isTraversable(jumpX, jumpY, 0, - move.directionY)) || 
                (isBlocked(jumpX, jumpY - move.directionY) && isTraversable(jumpX, jumpY, move.directionX, 0))) {
                System.out.println("Forced diagonal neighbor detected: " + newNode);
                return newNode;
            }
        } else if (move.directionX != 0) { // Horizontal move
            // We are applying strict cornering rules here: both adjacent nodes must be free to allow diagonal move.            
            // It would be nice to have have this parametrized but, at least for now, we are following the AI Moving Lab 
            // convention only to keep the results comparable and minimze complexity.

            // evaluate if we are in a forced stop by a corner
            if((isBlocked(currentNode.x, jumpY + 1) && (isTraversable(jumpX, jumpY, 0, 1 )) ||                 
                isBlocked(currentNode.x, jumpY - 1) && isTraversable(jumpX, jumpY, 0, 1))) {            
                    System.out.println("Forced horizontal neighbor detected: " + newNode);
                return newNode;                
            }
        } else { // Vertical move
            if((isBlocked(currentNode.x+1, currentNode.y) && (isTraversable(jumpX, jumpY, 1, 0 )) ||                 
                isBlocked(currentNode.x-1, currentNode.y) && isTraversable(jumpX, jumpY, -1, 0))) {            
                    System.out.println("Forced horizontal neighbor detected: " + newNode);
                return newNode;                
            }
        }   

        // if ~d is diagonal then for all i ∈ {1, 2} do if jump(n, ~di, s, g) is not null then return n
        if(directionX != 0 && directionY != 0) {
            int verticalOnlyDirection = MapUtil.VERTICAL_ONLY_PATHS[arrivalDirection]; // map diagonal path to its vertical component only direction
            int horizontalOnlyDirection = MapUtil.HORIZONTAL_ONLY_PATHS[arrivalDirection]; // map diagonal path to its vertical component only direction
            if(jump(newNode, verticalOnlyDirection, start, goal) != null || jump(newNode, horizontalOnlyDirection, start, goal) != null) {
                System.out.println("straight path found: " + newNode);
            
                return newNode;
            }
        }

        // return jump(n, ~d, s, g)
        System.out.println("continue jump: " + newNode + ": " + MapUtil.MOVE_DIRECTIONS[arrivalDirection]);
        return jump(newNode, arrivalDirection, start, goal);
    
    }

    public Result navigate(GridMap map, Point start, Point g) {
        return navigate(map, start, g, false);
    }

    /**
     * Implement the JPS algoritm. 
    */
    public Result navigate(GridMap map, Point start, Point goal, boolean cutCorners) {

        System.out.println("navigate JPS " + start + " -> " + goal);
            
        // save necessary data to instance variables to be used in jump calcuations
        this.grid = map.getGrid(); 
        this.width = grid.length;
        this.height = grid[0].length;
        this.traversability = map.getTraversability(cutCorners);
        this.closed = new boolean[width][height];
        Node[][] nodes = new Node[width][height];

        long startTime = System.currentTimeMillis();

        openList = new PriorityQueue<Node>(new NodeComparator());

        Node startNode = new Node(start.x, start.y);
        startNode.distance = 0;
        startNode.heuristic = MapUtil.octileDistance(start.x, start.y, goal.x, goal.y); 
        startNode.priority = startNode.heuristic;
        startNode.arrivalDirection = -1; // -1 = no direction, handle all neighbours
        openList.add(startNode);    

        nodes[startNode.x][startNode.y] = startNode;     
  
        Node node = null;
        int numOfEvaluatedNodes = 0;        

        // loop the priority queue until the goal is found or we run out of nodes
        while(!openList.isEmpty()) {

            node = openList.poll();

            System.out.println("************************************************************************");          
            System.out.println("naw node to handle: " + node);
            numOfEvaluatedNodes++;

            if(node.x==goal.x && node.y==goal.y) {
                break; // goal met
            }

            closed[node.x][node.y] = true;

            identifySuccessors(node, start, goal);
        }

        long finishTime = System.currentTimeMillis();

        boolean success = node.x == goal.x && node.y == goal.y;
        Result result = MapUtil.collectResults(node, startTime, finishTime, numOfEvaluatedNodes, MapUtil.ALGORITHM_JPS, success);
        return result;
    }  
}
