package com.orasaari;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Implementation of the JPS algorith.  
 * 
 * For the decription of the algorigthm, see the original paper:
 * @see https://users.cecs.anu.edu.au/~dharabor/data/papers/harabor-grastien-aaai11.pdf
 */
class JPSPathfinder extends Pathfinder {

    // some data structures kept in instance level for convienience
    private GridMap map;
    boolean[][] handled;
    PriorityQueue<JPSNode> openList;

    /**
     * "neighbours(x) ← prune(x, neighbours(x))"
     * 
     * Identify the neighbours of the node that need to be handled by pruning. To minimize overhead, we only identify 
     * directions to evaluate further and create actual node objects later as needed.
     * 
     * Enforcing stric cornering rules makes diagonal movement simpler: we don't have to check the nodes 90 degrees towards 
     * our movement directions since they are not possible successors anymore.
     * 
     * The vertical movement code is split to more cases than would be technically necessary to keep the 
     * code more readable and understandable.
     * 
     * @param   movingDirection    Direction from which the node was arrvived from, value mapping to MapUtil.MOVES.
     * @param   currentNode         The node whose neighbours are evaluated.
     * 
     * @return  successors          List of prunded neighbours that needs to be evaluated to extend the path.
     */
    private List<Integer> pruneNeighbours(JPSNode node) {

        List<Integer> neighbours = new ArrayList<Integer>(8);

        if(node.movingDirection < 0) { // No direction, add all neighbours       
            for(int i=0; i<8; i++) {
                if(map.isTraversable(node.x, node.y, i)) {
                    neighbours.add(Integer.valueOf(i));
                }
            }
        } else { 
            // we have a direction from which the node was arrived to -> prune neighbours based on the direction
            int direction = node.movingDirection;
            int directionX = MapUtils.MOVE_DIRECTIONS[direction].directionX;
            int directionY = MapUtils.MOVE_DIRECTIONS[direction].directionY;

            // Add the arrival direction if can continue straight.
            if(map.isTraversable(node.x, node.y, direction)) { 
                neighbours.add(Integer.valueOf(direction));
            }

            // Handle forced neighbours. Diagonal paths are checked first.
            if(directionX == 1 && directionY == 1) { // moving down-right
                if(map.isTraversable(node.x, node.y, MapUtils.RIGHT)) // right open, need to add it as well
                    neighbours.add(Integer.valueOf(MapUtils.RIGHT));
                if(map.isTraversable(node.x, node.y, MapUtils.DOWN)) // down open, need to add it as well
                    neighbours.add(Integer.valueOf(MapUtils.DOWN));

            } else if(directionX == 1 && directionY == -1) { // moving up-right
                if(map.isTraversable(node.x, node.y, MapUtils.RIGHT)) 
                    neighbours.add(Integer.valueOf(MapUtils.RIGHT));
                if(map.isTraversable(node.x, node.y, MapUtils.UP)) // 
                    neighbours.add(Integer.valueOf(MapUtils.UP));

            } else if(directionX == -1 && directionY == 1) { // moving down-left
                if(map.isTraversable(node.x, node.y, MapUtils.DOWN)) 
                    neighbours.add(Integer.valueOf(MapUtils.DOWN));
                if(map.isTraversable(node.x, node.y, MapUtils.LEFT)) 
                    neighbours.add(Integer.valueOf(MapUtils.LEFT));

            } else if(directionX == -1 && directionY == -1) { // moving up-left
                if(map.isTraversable(node.x, node.y, MapUtils.UP))  
                    neighbours.add(Integer.valueOf(MapUtils.UP));
                if(map.isTraversable(node.x, node.y, MapUtils.LEFT))  
                    neighbours.add(Integer.valueOf(MapUtils.LEFT));

            } else if(directionX != 0) { // moving horizontally

                // with stricter cornering rules, we also need to: 
                // 1) allow 90 degree turns 2) instead of the current node, check one node backwards for forced neighbours
                boolean previousUpBlocked = !map.isTraversable(node.x-directionX, node.y, MapUtils.UP);
                if(previousUpBlocked && map.isTraversable(node.x, node.y, directionX, -1)) // previous node up blocked, check up + arrival direction
                    neighbours.add(Integer.valueOf(map.getDirection(directionX, -1)));
                if(previousUpBlocked && map.isTraversable(node.x, node.y, MapUtils.UP)) // previous node up blocked, check 90 degree turn up
                    neighbours.add(Integer.valueOf(MapUtils.UP));

                boolean previousDownBlocked = !map.isTraversable(node.x-directionX, node.y, MapUtils.DOWN);
                if(previousDownBlocked && map.isTraversable(node.x, node.y, directionX, 1)) // previous node down blocked, check down + moving direction
                    neighbours.add(Integer.valueOf(map.getDirection(directionX, 1)));
                if(previousDownBlocked && map.isTraversable(node.x, node.y, MapUtils.DOWN)) //  previous node up blocked, check 90 degree turn down
                    neighbours.add(Integer.valueOf(MapUtils.DOWN));                    
                    
            } else { // moving vertically
            
                boolean previousRightBlocked = !map.isTraversable(node.x, node.y-directionY, MapUtils.RIGHT);
                if(previousRightBlocked && map.isTraversable(node.x, node.y, 1, directionY)) // previous node right blocked, check right + moving direction
                    neighbours.add(Integer.valueOf(map.getDirection(1, directionY)));
                if(previousRightBlocked && map.isTraversable(node.x, node.y, MapUtils.RIGHT)) // previous node right blocked, check 90 degrees right
                    neighbours.add(Integer.valueOf(MapUtils.RIGHT));

                boolean previousLeftBlocked = !map.isTraversable(node.x, node.y-directionY, MapUtils.LEFT);
                if(previousLeftBlocked && map.isTraversable(node.x, node.y, -1, directionY)) // previous node left blocked, check left + moving direction
                    neighbours.add(Integer.valueOf(map.getDirection(-1, directionY)));
                if(previousLeftBlocked && map.isTraversable(node.x, node.y, MapUtils.LEFT)) // previous node left blocked, check 90 degrees left
                    neighbours.add(Integer.valueOf(MapUtils.LEFT));
            }
        }
        return neighbours;
    }

    /**  
     * "Algorithm 1 Identify Successors
     * Require: x: current node, s: start, g: goal"
     * 
     * Implement the part of the algorithm that is labelled as "Identify Successors" in the original paper. 
    */
    private void identifySuccessors(JPSNode currentNode, int goalX, int goalY) {

        // successors(x) ← ∅        
        // neighbours(x) ← prune(x, neighbours(x))
        List<Integer> neighbourgs = pruneNeighbours(currentNode);
        
        // for all n ∈ neighbours(x) do
        for(int i=0; i<neighbourgs.size(); i++) {            
            int direction = neighbourgs.get(i);

            // n ← jump(x, direction(x, n), s, g)
            JPSNode jumpNode = jump(currentNode.x, currentNode.y, direction, goalX, goalY);

            if(jumpNode != null && !handled[jumpNode.x][jumpNode.y]) {
                jumpNode.movingDirection = direction; // keep the track of the moving direction for neighbour pruning
                jumpNode.previousNode = currentNode;       
                jumpNode.distanceFromStart = currentNode.distanceFromStart + octileDistance(currentNode.x, currentNode.y, jumpNode.x, jumpNode.y);
                jumpNode.priority = jumpNode.distanceFromStart + octileDistance(jumpNode.x, jumpNode.y, goalX, goalY);
                openList.add(jumpNode);    
            }
        }
    }

    /**
     * "Algorithm 2 Function jump
     * Require: x: initial node, ~d: direction, s: start, g: goal"
     * 
     * Implement the most critical part of JPS: the jump function. The recursive function searches the next
     * jump point according to the rules described in the original paper.
    */
    private JPSNode jump(int currentX, int currentY, int arrivalDirection, int goalX, int goalY) {

        Move move = MapUtils.MOVE_DIRECTIONS[arrivalDirection];

        // "n ← step(x, ~d)
        // if n is an obstacle or is outside the grid then return null"
        if(currentX<0 || currentY<0 || map.isBlocked(currentX, currentY) || !map.isTraversable(currentX, currentY, move.directionX, move.directionY)) {
            return null;
        } 

        int jumpX = currentX + move.directionX;
        int jumpY = currentY + move.directionY;
        int directionX = move.directionX;
        int directionY = move.directionY;

        //JPSNode newNode = new JPSNode(jumpX, jumpY);
        //newNode.movingDirection = arrivalDirection;

        // "if n = g then return n"
        if(jumpX == goalX && jumpY == goalY) {
            return new JPSNode(jumpX, jumpY);
        }

        // if ∃ n′ ∈ neighbours(n) s.t. n′ is forced then return n
        // Check for forced neighbours to see if we have found a jump point and neet to stop the search for now.
        if (directionX != 0 && directionY != 0) { // Diagonal move
            // Like in neigbour pruning, diagonal jump gets simpler with strict cornering rules: 
            // we only need to check a vertical or horizontal adjacent node towards the moving direction
            if ((map.isBlocked(jumpX + directionX, jumpY) && map.isTraversable(jumpX, jumpY, 0, directionY))  || 
                (map.isBlocked(jumpX, jumpY + directionY) && map.isTraversable(jumpX, jumpY, directionX, 0)))
                return new JPSNode(jumpX, jumpY);
        } else if (directionX != 0) { // Horizontal move
            // We are applying strict cornering rules here: both adjacent nodes must be free to allow diagonal move.            
            // This implies that we need to allow 90 degree turns after a blocking node as well when moving horizontally or vertically.
            if((map.isBlocked(currentX, jumpY + 1) && map.isTraversable(jumpX, jumpY, 0, 1 )) ||                 
               (map.isBlocked(currentX, jumpY - 1) && map.isTraversable(jumpX, jumpY, 0, -1)))            
                return new JPSNode(jumpX, jumpY);                
        } else { // Vertical move
            if((map.isBlocked(currentX + 1, currentY) && map.isTraversable(jumpX, jumpY, 1, 0 )) ||                 
               (map.isBlocked(currentX - 1, currentY) && map.isTraversable(jumpX, jumpY, -1, 0)))             
                return new JPSNode(jumpX, jumpY);                
        }   

        // "if ~d is diagonal then for all i ∈ {1, 2} do if jump(n, ~di, s, g) is not null then return n"
        // When moving diagonally, we need to check if we have horizontal or vertical paths available. If yes, we need to stop jumping and evaluate them as well.
        if(directionX != 0 && directionY != 0) {
            int verticalOnlyDirection = MapUtils.VERTICAL_ONLY_COMPONENT[arrivalDirection]; // map diagonal path to its vertical component only direction
            int horizontalOnlyDirection = MapUtils.HORIZONTAL_ONLY_COMPONENT[arrivalDirection]; // map diagonal path to its horizontal component only direction
            if(jump(jumpX, jumpY, verticalOnlyDirection, goalX, goalY) != null || jump(jumpX, jumpY, horizontalOnlyDirection, goalX, goalY) != null) {
                return new JPSNode(jumpX, jumpY);
            }
        }

        // "return jump(n, ~d, s, g)""
        // If there was no blocker or forced neighbour, continue the jump recursively.
        return jump(jumpX, jumpY, arrivalDirection, goalX, goalY);
    }

    /**
     * Implement the JPS pathfinding.
     * 
     * @see Pathfinder.findpath(GridMap map, int startX, int startY, int goalX, int goalY)
    */
    public PathfindingResult findPath(GridMap map, int startX, int startY, int goalX, int goalY) {

        // Initialize the data structures
        long startTime = System.currentTimeMillis();
        this.map = map;
        this.handled = new boolean[map.getWidth()][map.getHeight()];
        openList = new PriorityQueue<JPSNode>();

        // Initialize the priority heap (open list) with the starting node
        JPSNode startNode = new JPSNode(startX, startY);
        startNode.distanceFromStart = 0;
        startNode.priority = octileDistance(startX, startY, goalX, goalY); 
        startNode.movingDirection = -1; // -1 = no direction, handle all neighbours
        openList.add(startNode);    

        // Run the pathfinding
        int evaluatedNodes = 0;   
        JPSNode node = null;
        boolean goalFound = false;
        while(!openList.isEmpty()) {
            
            node = openList.poll();
            if(handled[node.x][node.y]) {
                continue;
            } else {
                handled[node.x][node.y] = true;
                evaluatedNodes++;
            }

            if(node.x==goalX && node.y==goalY) {
                goalFound = true;
                break; 
            }

            identifySuccessors(node, goalX, goalY);
        }

        // Iteration finished, collect results and return
        PathfindingResult result = collectResults(node, startTime, evaluatedNodes, MapUtils.ALGORITHM_JPS, goalFound, node.distanceFromStart);
        return result;
    }  
}