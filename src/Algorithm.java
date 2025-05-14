// Required imports for algorithm program
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Class containing implementations of pathfinding algorithms: DFS, BFS, and A*.
 * It also visualizes search progress by changing node colors with delay.
 */
public class Algorithm {

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
     * Uses stack to explore nodes
     */
    public void dfs(Node start, Node end, int graphWidth, int graphHeight) {
        Stack<Node> nodes = new Stack<>();
        Node[][] prev = new Node[graphWidth][graphHeight];
        boolean[][] visited = new boolean[graphWidth][graphHeight]; // Track visited nodes

        nodes.push(start);
        long startTime = System.currentTimeMillis();

        while (!nodes.empty()) {
            Node curNode = nodes.pop();

            int x = curNode.getX();
            int y = curNode.getY();

            if (visited[x][y]) continue;
            visited[x][y] = true;

            if (!curNode.isEnd()) {
                curNode.setColor(Color.ORANGE);
                try {
                    Thread.sleep(searchtime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                curNode.setColor(Color.BLUE);

                for (Node adjacent : curNode.getNeighbours()) {
                    int ax = adjacent.getX();
                    int ay = adjacent.getY();
                    if (!visited[ax][ay]) {
                        nodes.push(adjacent);
                        prev[ax][ay] = curNode;
                    }
                }
            } else {
                curNode.setColor(Color.MAGENTA);
                long endTime = System.currentTimeMillis();
                System.out.println("DFS Runtime: " + (endTime - startTime) + " ms");
                break;
            }
        }

        shortpath(prev, end); // Highlight shortest path
    }


    /**
     * Performs Breadth-First Search (BFS) from start node to end node.
     * Nodes are visited in a queue-based manner (FIFO).
     * Visualization: Orange = visited, Blue = Explored, Magenta = Target found
     * Uses queue to explore nodes
     */
    public void bfs(Node start, Node end, int graphWidth, int graphHeight) {
        Queue<Node> queue = new LinkedList<>();
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
            	// Visualize visiting the node
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
     * Uses a priority queue to explore nodes based on heuristic cost.
     * Visualization: Orange = visited, Blue = Explored, Magenta = Target found
     * Uses ArrayList for open list implementation
     */
    public void Astar(Node start, Node targetNode, int graphWidth, int graphHeight) {
        List<Node> openList = new ArrayList<>();
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
