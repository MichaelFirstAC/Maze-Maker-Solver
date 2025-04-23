// Required imports for node class
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;


/**
 * The Node class represents a single cell in a grid-based pathfinding algorithm.
 * Each node stores its position, visual properties, neighbor references, and pathfinding-related states.
 */
public class Node {

	private int Xpos; // X Coordinate (in pixels)
	private int Ypos; // Y Coordinate (in pixels)
	
	private Color nodeColor = Color.LIGHT_GRAY; // Default color of an unvisited node
	
	private final int WIDTH = 35;	// Width of the node in pixels
	private final int HEIGHT = 35;	// Height of the node in pixels
	
	// Neighbor references for cardinal directions
	private Node left, right, up, down;

	private double fcost; // Total estimated cost used in A* (g + h)

	// Constructor that initializes a nodes position
	public Node(int x, int y) {
		Xpos = x;
		Ypos = y;
	}
	
	// Default constructor
	public Node() {
	}

	/**
	 * Calculates Euclidean distance between two nodes.
	 * Used as the heuristic function in A*.
	 */
	public static double distance(Node a, Node b) {
		double x = Math.pow(a.Xpos - b.Xpos, 2);
		double y = Math.pow(a.Ypos - b.Ypos, 2);

		return Math.sqrt(x + y);
	}

	/**
	 * Renders the node using Graphics2D.
	 * Draws the border in black and fills the node with its current color.
	 */
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK); // Draw border
		g.drawRect(Xpos, Ypos, WIDTH, HEIGHT);
		g.setColor(nodeColor); // Fill color
		g.fillRect(Xpos + 1, Ypos + 1, WIDTH - 1, HEIGHT - 1);
	}

	/**
	 * Handles user interaction with this node via mouse clicks.
	 * buttonCode determines the action:
	 * 1 = wall, 2 = start, 3 = end, 4 = clear
	 */
	public void Clicked(int buttonCode) {
		System.out.println("called");
		if (buttonCode == 1) {
			nodeColor = Color.BLACK;	// Wall (impassable)

		}
		if (buttonCode == 2) {
			nodeColor = Color.GREEN;	// Start node

		}
		if (buttonCode == 3) {
			nodeColor = Color.RED;		// End node

		}
		if (buttonCode == 4) {
			clearNode();				// Reset to default state

		}
	}	
	
	// Getter and setter for f-cost used in A* algorithm
	public double getFCost() {
		return this.fcost;
	}

	public void setFCost(double fcost) {
		this.fcost = fcost;
	}

	// Change the visual color of the node
	public void setColor(Color c) {
		nodeColor = c;
	}

	public Color getColor() {
		return nodeColor;
	}

	/**
	 * Returns a list of neighbor nodes that are walkable (not walls).
	 */
	public List<Node> getNeighbours() {
		List<Node> neighbours = new ArrayList<>();
		if (left != null && left.isPath())
			neighbours.add(left);
		if (down != null && down.isPath())
			neighbours.add(down);
		if (right != null && right.isPath())
			neighbours.add(right);
		if (up != null && up.isPath())
			neighbours.add(up);

		return neighbours;
	}

	/**
	 * Assigns neighbor nodes (left, right, up, down) to this node.
	 * This helps pathfinding algorithms traverse the graph.
	 */
	public void setDirections(Node l, Node r, Node u, Node d) {
		left = l;
		right = r;
		up = u;
		down = d;
	}

	// Reset the node to its default unvisited state
	public void clearNode() {
		nodeColor = Color.LIGHT_GRAY;
	}

	/**
	 * Converts the node's X pixel position to grid column index.
	 * Assumes a margin of 15 pixels was added in the GUI.
	 */
	public int getX() {
		return (Xpos - 15) / WIDTH;
	}

	/**
	 * Converts the node's Y pixel position to grid row index.
	 * Assumes a margin of 15 pixels was added in the GUI.
	 */
	public int getY() {
		return (Ypos - 15) / HEIGHT;
	}

	// Setters for position (in pixels) – fluent interface style
	public Node setX(int x) {
		Xpos = x;
		return this;
	}

	public Node setY(int y) {
		Ypos = y;
		return this;
	}

	// Status checkers based on node color
	public boolean isWall() {
		return (nodeColor == Color.BLACK);
	}

	public boolean isStart() {
		return (nodeColor == Color.GREEN);
	}

	public boolean isEnd() {
		return (nodeColor == Color.RED);
	}

	public boolean isPath() {
		return (nodeColor == Color.LIGHT_GRAY || nodeColor == Color.RED);
	}

	public boolean isSearched() {
		return (nodeColor == Color.BLUE || nodeColor == Color.ORANGE);
	}

}
