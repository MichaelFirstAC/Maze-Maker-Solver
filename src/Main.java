// Required import libraries for GUI, graphics, and file handling.
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Main Class extending Canvas (for drawing) and 
 * 	implementing Runnable (for threading) and
 * MouseListener (for user input)
 */
public class Main extends Canvas implements Runnable, MouseListener {

	private static Node start = null;	// Reference to the starting point of the 
	private static Node target = null;	// Reference to the ending point of the maze
	private static JFrame frame;		// Main application window

	private Node[][] nodeList;			// 2D grid of nodes representing the maze
	private static Main runTimeMain;	// Static reference to this instance for use in menu callbacks
	private static Algorithm algorithm;	// Object to perform pathfinding algorithms

	// Dimensions for the application window
	private final static int WIDTH = 1690;
	private final static int HEIGHT = 980;
	
	// Dimensions for the maze grid (48x27 nodes)
	private final static int NODES_WIDTH = 47;
	private final static int NODES_HEIGHT = 25;
	
	// Main entry point for the program
	public static void main(String[] args) {
		frame = new JFrame("Maze Solver");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setLayout(null); // Use absolute layout
		
		Main m = new Main();
		algorithm =  new Algorithm(); // Instantiate pathfinding logic
		
		m.setBounds(0, 25, WIDTH, HEIGHT);	// Position canvas below menu
		SetupMenu(frame);					// Create menu UI
		runTimeMain = m;
		
		frame.add(m);
		frame.setVisible(true);
		m.start(); // Start the render thread

	}
	
	// Sets up the menu bar and its actions
	public static void SetupMenu(JFrame frame) {
		JMenuBar bar = new JMenuBar();
		bar.setBounds(0, 0, WIDTH, 25);
		frame.add(bar);
		
		// Create top-level menus
		JMenu fileMenu = new JMenu("File");
		bar.add(fileMenu);
		
		JMenu boardMenu = new JMenu("Board");
		bar.add(boardMenu);
		
		JMenu algorithmsMenu = new JMenu("Algorithms");
		bar.add(algorithmsMenu);
		
		// Create menu items for each menu
		// File menu
		JMenuItem saveMaze = new JMenuItem("Save Maze");
		JMenuItem openMaze = new JMenuItem("Open Maze");
		JMenuItem exit = new JMenuItem("Exit");

		// Board menu
		JMenuItem newGrid = new JMenuItem("New Board");
		JMenuItem clearSearch = new JMenuItem("Clear Search Results");

		// Algorithm menu
		JMenuItem bfsItem = new JMenuItem("Breadth-First Search");
		JMenuItem dfsItem = new JMenuItem("Depth-First Search");
		JMenuItem astarItem = new JMenuItem("A-star Search");
		JMenuItem searchTime = new JMenuItem("Exploring time per Node");

		// Link menu items to their functions using lambda expressions
		openMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					runTimeMain.openMaze(); // Load maze from file
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		
		saveMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runTimeMain.clearSearchResults(); // Do not save search paths
				try {
					runTimeMain.saveMaze(); // Save maze layout
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		
		// Close application
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		// Reset board
		newGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runTimeMain.createNodes(true);
			}
		});
		
		// Clear paths only
		clearSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runTimeMain.clearSearchResults();
			}
		});

		// Run pathfinding algorithms only if start and end points are valid
		// BFS algorithm
		bfsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (runTimeMain.isMazeValid()) {
					algorithm.bfs(runTimeMain.start, runTimeMain.target, runTimeMain.NODES_WIDTH,
							runTimeMain.NODES_HEIGHT);
				} else {
					System.out.println("DIDNT LAUNCH");
				}
			}
		});
		
		// DFS algorithm
		dfsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (runTimeMain.isMazeValid()) {
					algorithm.dfs(runTimeMain.start, runTimeMain.target, runTimeMain.NODES_WIDTH,
							runTimeMain.NODES_HEIGHT);
				} else {
					System.out.println("DIDNT LAUNCH");
				}
			}
		});
		
		// Astar algorithm 
		astarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (runTimeMain.isMazeValid()) {
					algorithm.Astar(runTimeMain.start, runTimeMain.target, runTimeMain.NODES_WIDTH,
							runTimeMain.NODES_HEIGHT);
				} else {
					System.out.println("DIDNT LAUNCH");
				}
			}
		});
		
		// Set delay for search animations (Just used for visuals)
		searchTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String input = JOptionPane.showInputDialog(null, "Enter a time it takes to search each node in miliseconds(default = 100ms) ", "Search Time", JOptionPane.QUESTION_MESSAGE);
				algorithm.setSearchTime(Integer.parseInt(input));
			}
		});

		// Add items to appropriate menus
		fileMenu.add(exit);
		fileMenu.add(saveMaze);
		fileMenu.add(openMaze);
		boardMenu.add(newGrid);
		boardMenu.add(clearSearch);
		algorithmsMenu.add(dfsItem);
		algorithmsMenu.add(bfsItem);
		algorithmsMenu.add(astarItem);
		algorithmsMenu.add(searchTime);
	}

	// Thread loop that handles rendering
	public void run() {
		init(); // Setup grid
		while (true) {
			BufferStrategy bs = getBufferStrategy();
			if (bs == null) {
				createBufferStrategy(2); // Double buffering
				continue;
			}
			Graphics2D grap = (Graphics2D) bs.getDrawGraphics(); // check
			render(grap); // Draw all nodes
			bs.show();
			try {
				Thread.sleep(1); // Frame delay
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// Initialization of grid and listeners
	public void init() {
		requestFocus(); // Request keyboard mouse focus
		addMouseListener(this); // Register mouse input
		nodeList = new Node[NODES_WIDTH][NODES_HEIGHT];
		createNodes(false); // Initialize node positions
		setMazeDirections(); // Define neighbors for each node
	}
	
	// Setup neighbor links for all nodes (Used in pathfinding)
	public void setMazeDirections() {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				Node up = null,down = null,left = null,right = null;
				int u = j - 1;
				int d = j + 1;
				int l = i - 1;
				int r = i + 1;
				
				if(u >= 0) up = nodeList[i][u];
				if(d < NODES_HEIGHT) down =  nodeList[i][d];
				if(l >= 0) left = nodeList[l][j];
				if(r < NODES_WIDTH) right =  nodeList[r][j];
				
				nodeList[i][j].setDirections(left, right, up, down);
			}	
		}
	}
	
	// Create or reset the grid
	public void createNodes(boolean ref) {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				if(!ref) nodeList[i][j] = new Node(i, j).setX(15 + i * 35).setY(15 + j * 35);
				nodeList[i][j].clearNode(); // Remove wall or path state
			}
		}
	}

	// Save current maze layout to a file ('.maze' format)
	public void saveMaze() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showSaveDialog(frame);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			String ext = file.getAbsolutePath().endsWith(".maze") ? "" : ".maze";
			BufferedWriter outputWriter = new BufferedWriter(new FileWriter(file.getAbsolutePath() + ext));
			for (int i = 0; i < nodeList.length; i++) {
				for (int j = 0; j < nodeList[i].length; j++) {
					
					// O = normal, 1 = wall, 2 = start, 3 = end
					if (nodeList[i][j].isWall()) {
						outputWriter.write("1");
					} else if (nodeList[i][j].isStart()) {
						outputWriter.write("2");
					} else if (nodeList[i][j].isEnd()) {
						outputWriter.write("3");
					} else {
						outputWriter.write("0");
					}
				}
				outputWriter.newLine();
			}
			outputWriter.flush();
			outputWriter.close();
		}
	}

	// Load maze layout from file
	public void openMaze() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(frame);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String line = null;
			for (int i = 0; i < NODES_WIDTH; i++) {
				line = reader.readLine();
				for (int j = 0; j < NODES_HEIGHT; j++) {
					
					//nodeList[i][j].setColor(Color.BLACK);
					int nodeType = Character.getNumericValue(line.charAt(j));
					System.out.println("node is " + nodeType);
					switch (nodeType) {
					case 0:
						nodeList[i][j].setColor(Color.LIGHT_GRAY);
						break;
					case 1:
						nodeList[i][j].setColor(Color.BLACK);
						break;

					case 2:
						nodeList[i][j].setColor(Color.GREEN);
						start = nodeList[i][j];
						break;
					case 3:
						nodeList[i][j].setColor(Color.RED);
						target = nodeList[i][j];
						break;
					}
				}
			}
			reader.close();
			// System.out.println(stringMaze);
		}
	}

	// Clear only the search paths, keeping walls, start, and endpoints
	public void clearSearchResults() {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				if (nodeList[i][j].isSearched()) {
					nodeList[i][j].clearNode();
				}
			}
		}
		if (isMazeValid()) {
			target.setColor(Color.RED);
			start.setColor(Color.GREEN);
		}
	}

	// Draws the maze grid and background
	public void render(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				nodeList[i][j].render(g);
			}
		}
	}

	// Start the render thread
	public void start() {
		new Thread(this).start();
	}

	// Handle mouse input: toggle wall, set start or end point
	public void mousePressed(MouseEvent e) {
		Node clickedNode = getNodeAt(e.getX(), e.getY());
		if (clickedNode == null)
			return;

		// Clear/delete wall if clicked
		if (clickedNode.isWall()) {
			clickedNode.clearNode();
			return;
		}

		clickedNode.Clicked(e.getButton()); // Leftclick: wall, middle: start, right: end

		if (clickedNode.isEnd()) {
			if (target != null) {
				target.clearNode();
			}
			target = clickedNode;
		} else if (clickedNode.isStart()) {

			if (start != null) {
				start.clearNode();
			}
			start = clickedNode;
		}

	}

	// Checks if both start and target nodes are set
	public boolean isMazeValid() {
		return target == null ? false : true && start == null ? false : true;
	}

	// Helper to retrieve the current start node (used in DFS)
	private Node getStart() {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				if (nodeList[i][j].isStart()) {
					return nodeList[i][j];
				}
			}
		}
		return null;
	}

	// Given screen coordinates, find the corresponding node
	public Node getNodeAt(int x, int y) {
		x -= 15;
		x /= 35;
		y -= 15;
		y /= 35;

		System.out.println(x + ":" + y);
		if (x >= 0 && y >= 0 && x < nodeList.length && y < nodeList[x].length) {
			return nodeList[x][y];
		}
		return null;
	}

	// Requirements for Mouselistener
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
