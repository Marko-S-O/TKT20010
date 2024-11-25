package com.orasaari;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Implementation of the JPS algorith.  
 * 
 * For the decription of the algorigthm, see the original paper:
 * @see https://users.cecs.anu.edu.au/~dharabor/data/papers/harabor-grastien-aaai11.pdf
 */
class JPSPathfinder implements Pathfinder {

    private int width, height;
    private boolean[][] grid;
    boolean[][] closed;
    boolean[][][] traversability;
    PriorityQueue<Node> openList;

     /** 
     * Internal class to decide the order of two nodes in the priority queue.  
     * The priority is the distance of the node from the starting point + heuristic function (octile distance to the goal)
    */
    private static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return Double.compare(n1.priority, n2.priority);
        }
    }

    /* 
     * Check if the node is blocked or outside the map. Required by the forced neighbour evaluation.
    */
    private boolean isBlocked(int x, int y) {
        return x<0 || x>=width || y<0 || y>=height || !grid[x][y];
    }

    /* 
     * Check if the node is inside the map and traversable. Required by the forced neighbour evaluation and neighbour pruning.
    */
    private boolean isTraversable(int x, int y, int directionX, int directionY) {        
        int direction = MapUtil.getDirection(directionX, directionY); // Map (dx, dy) movement directions to the integer code representing one of the 8 directions.
        boolean traversable = x>=0 && x<width && y>=0 && y<height && traversability[x][y][direction];
        return traversable;
    }


    /**
     * Identify successors of a node by pruning the neighbours. To minimize overhead, we only identify 
     * directions here and create actual node objects later as needed.
     * 
     * Note: the vertical movement code is split to more cases than would be mandatory to keep the 
     * code more readable and understandable.
     * 
     * @param   arrivalDirection    Direction from which the node was arrvived from, value mapping to MapUtil.MOVES.
     * @param   currentNode         The node whose neighbours are evaluated.
     * 
     * @return  successors          List of prunded neighbours that needs to be evaluated to extend the path.
     */
    private List<Integer> pruneNeighbours(Node node) {

        List<Integer> neighbours = new ArrayList<Integer>(8);

        if(node.arrivalDirection < 0) { // No direction, add all neighbours       
            for(int i=0; i<8; i++) {
                if(traversability[node.x][node.y][i]) {
                    neighbours.add(Integer.valueOf(i));
                }
            }
        } else { 
            // we have a direction from which the node was arrived to -> prune neighbours based on the direction
            int direction = node.arrivalDirection;
            int directionX = MapUtil.MOVE_DIRECTIONS[direction].directionX;
            int directionY = MapUtil.MOVE_DIRECTIONS[direction].directionY;

            // Add the arrival direction if can continue straight.
            if(traversability[node.x][node.y][direction]) { 
                neighbours.add(Integer.valueOf(direction));
            }

            // Handle forced neighbours. Vertical paths are checked first.
            if(directionX == 1 && directionY == 1) { // moving down-right
                //System.out.println("prune diagonal");
                if(traversability[node.x][node.y][MapUtil.RIGHT]) // right open, need to add it as well
                    neighbours.add(Integer.valueOf(MapUtil.RIGHT));
                if(traversability[node.x][node.y][MapUtil.DOWN]) // down open, need to add it as well
                    neighbours.add(Integer.valueOf(MapUtil.DOWN));
                if(!traversability[node.x][node.y][MapUtil.RIGHT] && traversability[node.x][node.y][MapUtil.RIGHT_UP]) // right blocked, check up-right
                    neighbours.add(Integer.valueOf(MapUtil.RIGHT_UP));
                if(!traversability[node.x][node.y][MapUtil.DOWN] && traversability[node.x][node.y][MapUtil.LEFT_DOWN]) // down blocked, check down-left
                    neighbours.add(Integer.valueOf(MapUtil.LEFT_DOWN));

            } else if(directionX == 1 && directionY == -1) { // moving up-right
                if(traversability[node.x][node.y][MapUtil.RIGHT]) 
                    neighbours.add(Integer.valueOf(MapUtil.RIGHT));
                if(traversability[node.x][node.y][MapUtil.UP]) // 
                    neighbours.add(Integer.valueOf(MapUtil.UP));
                if(!traversability[node.x][node.y][MapUtil.RIGHT] && traversability[node.x][node.y][MapUtil.RIGHT_UP]) // 
                    neighbours.add(Integer.valueOf(MapUtil.RIGHT_UP));
                if(!traversability[node.x][node.y][MapUtil.DOWN] && traversability[node.x][node.y][MapUtil.LEFT_DOWN]) // 
                    neighbours.add(Integer.valueOf(MapUtil.LEFT_DOWN));

            } else if(directionX == -1 && directionY == 1) { // moving down-left
                if(traversability[node.x][node.y][MapUtil.DOWN]) 
                    neighbours.add(Integer.valueOf(MapUtil.DOWN));
                if(traversability[node.x][node.y][MapUtil.LEFT]) 
                    neighbours.add(Integer.valueOf(MapUtil.LEFT));
                if(!traversability[node.x][node.y][MapUtil.RIGHT] && traversability[node.x][node.y][MapUtil.RIGHT_DOWN]) // 
                    neighbours.add(Integer.valueOf(MapUtil.RIGHT_DOWN));
                if(!traversability[node.x][node.y][MapUtil.LEFT] && traversability[node.x][node.y][MapUtil.LEFT_UP]) // 
                    neighbours.add(Integer.valueOf(MapUtil.LEFT_UP));                

            } else if(directionX == -1 && directionY == -1) { // moving up-left
                if(traversability[node.x][node.y][MapUtil.UP])  
                    neighbours.add(Integer.valueOf(MapUtil.UP));
                if(traversability[node.x][node.y][MapUtil.LEFT])  
                    neighbours.add(Integer.valueOf(MapUtil.LEFT));
                if(!traversability[node.x][node.y][MapUtil.RIGHT] && traversability[node.x][node.y][MapUtil.RIGHT_DOWN]) // 
                    neighbours.add(Integer.valueOf(MapUtil.RIGHT_DOWN));
                if(!traversability[node.x][node.y][MapUtil.LEFT] && traversability[node.x][node.y][MapUtil.LEFT_UP]) // 
                    neighbours.add(Integer.valueOf(MapUtil.LEFT_UP));                      

            } else if(directionX != 0) { // moving horizontally

                // with stricter cornering rules, we also need to: 1) allow 90 degree turns 2) instead of the current node, check one node backwards for forced neighbours
                boolean previousUpBlocked = !traversability[node.x-directionX][node.y][MapUtil.UP];
                if(previousUpBlocked && isTraversable(node.x, node.y, directionX, -1)) // previous node up blocked, check up + arrival direction
                    neighbours.add(Integer.valueOf(MapUtil.getDirection(directionX, -1)));
                if(previousUpBlocked && traversability[node.x][node.y][MapUtil.UP]) // previous node up blocked, check 90 degree turn up
                    neighbours.add(Integer.valueOf(MapUtil.UP));

                boolean previousDownBlocked = !traversability[node.x-directionX][node.y][MapUtil.DOWN];
                if(previousDownBlocked && isTraversable(node.x, node.y, directionX, 1)) // previous node down blocked, check down + moving direction
                    neighbours.add(Integer.valueOf(MapUtil.getDirection(directionX, 1)));
                if(previousDownBlocked && traversability[node.x][node.y][MapUtil.DOWN]) //  previous node up blocked, check 90 degree turn down
                    neighbours.add(Integer.valueOf(MapUtil.DOWN));                    
                    
            } else { // moving vertically
            
                boolean previousRightBlocked = !traversability[node.x][node.y-directionY][MapUtil.RIGHT];
                if(previousRightBlocked && isTraversable(node.x, node.y, 1, directionY)) // previous node right blocked, check right + moving direction
                    neighbours.add(Integer.valueOf(MapUtil.getDirection(1, directionY)));
                if(previousRightBlocked && traversability[node.x][node.y][MapUtil.RIGHT]) // previous node right blocked, check 90 degrees right
                    neighbours.add(Integer.valueOf(MapUtil.RIGHT));

                boolean previousLeftBlocked = !traversability[node.x][node.y-directionY][MapUtil.LEFT];
                if(previousLeftBlocked && isTraversable(node.x, node.y, -1, directionY)) // previous node left blocked, check left + moving direction
                    neighbours.add(Integer.valueOf(MapUtil.getDirection(-1, directionY)));
                if(previousLeftBlocked && traversability[node.x][node.y][MapUtil.LEFT]) // previous node left blocked, check 90 degrees left
                    neighbours.add(Integer.valueOf(MapUtil.LEFT));
    
            }
        }
        return neighbours;
    }

    /**  
     * Algorithm 1 Identify Successors
     * Require: x: current node, s: start, g: goal
     * 
     * Implement the part of the algorithm that is labelled as "Identify Successors" in the original paper. 
    */
    private void identifySuccessors(Node currentNode, Point start, Point goal) {

        // successors(x) ← ∅        
        // neighbours(x) ← prune(x, neighbours(x))
        List<Integer> neighbourgs = pruneNeighbours(currentNode);
        
        // for all n ∈ neighbours(x) do
        for(int i=0; i<neighbourgs.size(); i++) {            
            int direction = neighbourgs.get(i);

            // n ← jump(x, direction(x, n), s, g)
            Node jumpNode = jump(currentNode, direction, start, goal);

            if(jumpNode != null && !closed[jumpNode.x][jumpNode.y]) {
                jumpNode.arrivalDirection = direction; // keep the track of the moving direction for neighbour pruning
                jumpNode.previous = currentNode; // maintain the links to be able to backtrace & collect the path after the goal is found
                jumpNode.distance = currentNode.distance + MapUtil.octileDistance(currentNode.x, currentNode.y, jumpNode.x, jumpNode.y);
                jumpNode.heuristic = MapUtil.octileDistance(jumpNode.x, jumpNode.y, goal.x, goal.y);
                jumpNode.priority = jumpNode.distance + jumpNode.heuristic;
                openList.add(jumpNode);    
            }
        }
    }

    /**
     * Algorithm 2 Function jump
     * Require: x: initial node, ~d: direction, s: start, g: goal
     * 
     * Implement the most critical part of JPS: the jump function. The recursive function searches the next
     * jump point according to the rules described in the original paper.
    */
    private Node jump(Node currentNode, int arrivalDirection, Point start, Point goal) {

        Move move = MapUtil.MOVE_DIRECTIONS[arrivalDirection];

        // n ← step(x, ~d)
        // if n is an obstacle or is outside the grid then return null
        if(currentNode==null || isBlocked(currentNode.x, currentNode.y) || !isTraversable(currentNode.x, currentNode.y, move.directionX, move.directionY)) {
            return null;
        } 

        int jumpX = currentNode.x + move.directionX;
        int jumpY = currentNode.y + move.directionY;
        int directionX = move.directionX;
        int directionY = move.directionY;

        Node newNode = new Node(jumpX, jumpY);
        newNode.arrivalDirection = arrivalDirection;

        // if n = g then return n
        if(jumpX == goal.x && jumpY == goal.y) {
            return newNode;
        }

        // if ∃ n′ ∈ neighbours(n) s.t. n′ is forced then return n
        // Check for forced neighbours to see if we have found a jump point and neet to stop the search for now.
        if (move.directionX != 0 && move.directionY != 0) { // Diagonal move
            if ((isBlocked(jumpX + move.directionX, jumpY) && isTraversable(jumpX, jumpY, 0, move.directionY)) || 
                (isBlocked(jumpX, jumpY + move.directionY) && isTraversable(jumpX, jumpY, move.directionX, 0)) || 
                (isBlocked(jumpX - move.directionX, jumpY) && isTraversable(jumpX, jumpY, 0, - move.directionY)) || 
                (isBlocked(jumpX, jumpY - move.directionY) && isTraversable(jumpX, jumpY, move.directionX, 0))) {
                return newNode;
            }
        } else if (move.directionX != 0) { // Horizontal move
            // We are applying strict cornering rules here: both adjacent nodes must be free to allow diagonal move.            
            // This implies that we need to allow 90 degree turns after a blocking node as well when moving horizontally or vertically.
            if((isBlocked(currentNode.x, jumpY + 1) && (isTraversable(jumpX, jumpY, 0, 1 )) ||                 
                isBlocked(currentNode.x, jumpY - 1) && isTraversable(jumpX, jumpY, 0, -1))) {            
                return newNode;                
            }
        } else { // Vertical move
            if((isBlocked(currentNode.x+1, currentNode.y) && (isTraversable(jumpX, jumpY, 1, 0 )) ||                 
                isBlocked(currentNode.x-1, currentNode.y) && isTraversable(jumpX, jumpY, -1, 0))) {            
                return newNode;                
            }
        }   

        // if ~d is diagonal then for all i ∈ {1, 2} do if jump(n, ~di, s, g) is not null then return n
        // When moving diagonally, we need to check if we have horizaontal or vertical paths available. If yes, we need to stop jumping and evaluate them as well.
        if(directionX != 0 && directionY != 0) {
            int verticalOnlyDirection = MapUtil.VERTICAL_ONLY_PATHS[arrivalDirection]; // map diagonal path to its vertical component only direction
            int horizontalOnlyDirection = MapUtil.HORIZONTAL_ONLY_PATHS[arrivalDirection]; // map diagonal path to its horizontal component only direction
            if(jump(newNode, verticalOnlyDirection, start, goal) != null || jump(newNode, horizontalOnlyDirection, start, goal) != null) {
                return newNode;
            }
        }

        // return jump(n, ~d, s, g)
        // If there was no blocker or forced neighbour, continue the jump recursively.
        return jump(newNode, arrivalDirection, start, goal);
    }

    /* 
     * @see com.orasaari.JPSPathfinder#navigate(com.orasaari.GridMap, java.awt.Point, java.awt.Point, boolean)
    */
    public Result navigate(GridMap map, Point start, Point g) {
        return navigate(map, start, g, false);
    }

    /**
     * Implement the JPS pathfinding.
     * 
     * @param map           The 2D grid map where the path is searched.
     * @param start         The starting point of the path.
     * @param goal          The finishing point of the path.
     * @param cutCorners    If false, diagonal movement is allowed only if both of the adjacent nodes (vertical and horizontal neighbors 
     *                      towards the moving direction) are traversable. Currently, this JPS implementation supporse only using false value.
     *                      The parameter is included to have compatible method signatures with the other pathfinding algorithms.
     * 
     * @return              the Result object wrapping the pathfinding results
    */
    public Result navigate(GridMap map, Point start, Point goal, boolean cutCorners) {

       if(cutCorners) {
            throw new IllegalArgumentException("Current JPS implementation does not support cutting corners.");
        }
            
        // save necessary data to instance variables to be used in jump calcuations
        this.grid = map.getGrid(); 
        this.width = grid.length;
        this.height = grid[0].length;
        this.traversability = map.getTraversability(false); // pre-calculated traversability to the adjacent nodes
        this.closed = new boolean[width][height];
        Node[][] nodes = new Node[width][height];

        long startTime = System.currentTimeMillis();

        // Initialize the priority heap, for JPS usually called open list.
        openList = new PriorityQueue<Node>(new NodeComparator());

        // Initialize the priority heap with the starting node
        Node startNode = new Node(start.x, start.y);
        startNode.distance = 0;
        startNode.heuristic = MapUtil.octileDistance(start.x, start.y, goal.x, goal.y); 
        startNode.priority = startNode.heuristic;
        startNode.arrivalDirection = -1; // -1 = no direction, handle all neighbours
        openList.add(startNode);    
        nodes[startNode.x][startNode.y] = startNode;     
        int numOfEvaluatedNodes = 0;   

        Node node = null;

        // loop the priority queue until the goal is found or we run out of nodes
        while(!openList.isEmpty()) {

            node = openList.poll();
            numOfEvaluatedNodes++;
            if(node.x==goal.x && node.y==goal.y) {
                break; // goal met
            }
            closed[node.x][node.y] = true;
            identifySuccessors(node, start, goal);
        }

        // Iteration finished, collect results and return
        long finishTime = System.currentTimeMillis();
        boolean success = node.x == goal.x && node.y == goal.y;
        Result result = MapUtil.collectResults(node, startTime, finishTime, numOfEvaluatedNodes, MapUtil.ALGORITHM_JPS, success);
        return result;
    }  
}
