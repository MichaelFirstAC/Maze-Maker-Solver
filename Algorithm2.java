/** This file is the comparison algorithm if we were to use different data structures
 * It uses recursion for DFS, Arraydeque for BFS, and a Linkedlist for A*.
 */


// Required imports for algorithm program
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Class containing implementations of pathfinding algorithms: DFS, BFS, and A*.
 * It also visualizes search progress by changing node colors with delay.
 */
public class Algorithm2 {

	// Delay time in milliseconds used to visualize search progress
    private int searchtime = 100;

    public int getSearchTime() {
        return searchtime;
    }

    public void setSearchTime(int searchtime) {
        this.searchtime = searchtime;
    }

    /**
     * Performs Depth-first search (DFS) from the starting node
     * Nodes are visited in a stack-based manner (LIFO)
     * Visualization: Orange = visited, Blue = Explored, Magenta = Target found
     */   
    public void dfs(Node start, Node end, int graphWidth, int graphHeight) {
        Stack<Node> nodes = new Stack<>();
        Node[][] prev = new Node[graphWidth][graphHeight]; // Matrix to store previous nodes
        nodes.push(start);
        long startTime = System.currentTimeMillis();

        while (!nodes.empty()) {
            Node curNode = nodes.pop();
            
            // Check if the current node is the end node
            if (curNode.isEnd()) {
                // Mark the end node in magenta
                curNode.setColor(Color.MAGENTA);
                long endTime = System.currentTimeMillis();
                System.out.println("DFS Runtime: " + (endTime - startTime) + " ms");
                break; // Exit the loop when the end node is found
            }

            // Visualize visiting the node
            curNode.setColor(Color.ORANGE); // Mark as visited
            try {
                Thread.sleep(searchtime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            curNode.setColor(Color.BLUE); // Mark as explored
            
            // Push all neighbors onto the stack
            for (Node adjacent : curNode.getNeighbours()) {
                if (prev[adjacent.getX()][adjacent.getY()] == null) { // Check if not visited
                    nodes.push(adjacent);
                    prev[adjacent.getX()][adjacent.getY()] = curNode; // Set previous node
                }
            }
        }

        // Retrace the path from the end node to the start node
        Node pathConstructor = end; // Start from the end node
        while (pathConstructor != null) {
            if (pathConstructor.isEnd()) {
                // The end node is already magenta, no need to change it again
            } else {
                pathConstructor.setColor(Color.ORANGE);
                end.setColor(Color.MAGENTA);
                // Highlight the path node
            }
            
            try {
                Thread.sleep(searchtime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Move to the previous node
            pathConstructor = prev[pathConstructor.getX()][pathConstructor.getY()]; 
        }
    }

    /**
     * Performs Breadth-First Search (BFS) from start node to end node.
     * Uses an Arraydeque to explore nodes in a queue-based manner (FIFO).
     * Also stores path using a prev[][] matrix.
     */
    public void bfs(Node start, Node end, int graphWidth, int graphHeight) {
        Queue<Node> queue = new ArrayDeque<>();
        Node[][] prev = new Node[graphWidth][graphHeight];
        long startTime = System.currentTimeMillis(); // Start timing the search
    
        queue.add(start);
        while (!queue.isEmpty()) {
            Node curNode = queue.poll();
    
            if (curNode.isEnd()) {
                curNode.setColor(Color.MAGENTA);
                long endTime = System.currentTimeMillis();
                System.out.println("BFS Runtime: " + (endTime - startTime) + " ms");
                break;
            }
    
            if (!curNode.isSearched()) {
                curNode.setColor(Color.ORANGE);
                try {
                    Thread.sleep(searchtime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                curNode.setColor(Color.BLUE); // Mark as explored
    
                for (Node adjacent : curNode.getNeighbours()) {
                    queue.add(adjacent);
                    prev[adjacent.getX()][adjacent.getY()] = curNode; // Store path
                }
            }
        }
    
        // Highlight the shortest path
        shortpath(prev, end);
    }

    /**
     * Performs A* (A star) pathfinding algorithm from start to end node.
     * Combines actual cost (g) and heuristic estimate (h) to choose optimal path.
     * Uses a LinkedList to store open nodes for A* search
     */
    public void Astar(Node start, Node targetNode, int graphWidth, int graphHeight) {
        List<Node> openList = new LinkedList<>();
        Node[][] prev = new Node[graphWidth][graphHeight];
        long startTime = System.currentTimeMillis(); // Start timing
        
        openList.add(start);

        while (!openList.isEmpty()) {
        	// Pick the node with the lowest estimated cost to goal
            Node curNode = getLeastHeuristic(openList, targetNode, start);
            openList.remove(curNode);

            if (curNode.isEnd()) {
                curNode.setColor(Color.MAGENTA);
                long endTime = System.currentTimeMillis();
                System.out.println("A* Runtime: " + (endTime - startTime) + " ms");
                break;
            }
            
            // Visual feedback
            curNode.setColor(Color.ORANGE);
            try {
                Thread.sleep(searchtime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            curNode.setColor(Color.BLUE);
            
            for (Node adjacent : curNode.getNeighbours()) {
                if (adjacent.isSearched()) {
                    continue; // Skip already explored nodes
                }
                
                // Heuristic path if new path is better or node has not been added yet
                double f1 = Node.distance(adjacent, targetNode);
                double h1 = Node.distance(curNode, start);

                double f2 = Node.distance(adjacent, targetNode);
                double h2 = Node.distance(curNode, start);

                // Update path if new path is better or node has not been added yet
                if (!openList.contains(adjacent) || (f1 + h1 < f2 + h2)) {
                    prev[adjacent.getX()][adjacent.getY()] = curNode;
                    
                    if (!openList.contains(adjacent)) {
                        openList.add(adjacent);
                    }
                }
            }
        }
        shortpath(prev, targetNode); // Highlight the optimal path found
    }

    /**
     * Helper method to find the node with the lowest f(n) = g(n) = h(n)
     * from the list of candidates (open list).
     */
    private Node getLeastHeuristic(List<Node> nodes, Node end, Node start) {
        if (!nodes.isEmpty()) {
            Node leastH = nodes.get(0);
            
            for (int i = 1; i < nodes.size(); i++) {
                double f1 = Node.distance(nodes.get(i), end);
                double h1 = Node.distance(nodes.get(i), start);

                double f2 = Node.distance(leastH, end);
                double h2 = Node.distance(leastH, start);
                
                if (f1 + h1 < f2 + h2) {
                    leastH = nodes.get(i);
                }
            }
            return leastH;
        }
        return null;
    }

    /**
     * Reconstructs and visualizes the path from the end node back to the start.
     * using the prev[][] matrix generated during BFS or A*.
     */
    private void shortpath(Node[][] prev, Node end) {
        Node pathConstructor = end;
        while (pathConstructor != null) {
            pathConstructor = prev[pathConstructor.getX()][pathConstructor.getY()];

            // Walk backwards from the end node to the start node
            if (pathConstructor != null) {
                pathConstructor.setColor(Color.ORANGE); // Highlight path node
            }
            
            try {
                Thread.sleep(searchtime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
