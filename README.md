# Overview

Maze-solver-java is an application with an interactive GUI that lets you design your mazes. You can choose from the available algorithms to search and find the shortest path from your desired start-to-end point.

* `Left Click -> Wall Node`
* `Rigth Click -> End Node`
* `Middle Click -> Start Node`

The algorithms included are:

* Breadth First Search
* Depth First Search
* A* star Search 

# Project Structure
Source Files (src folder):

- Algorithm.java: Contains the implementation of various maze-solving algorithms (DFS, BFS, A*).
- Main.java: The main class that sets up the GUI, handles user interactions, and integrates the algorithms.
- Node.java: Represents a node in the maze, with properties and methods to manage its state and neighbors.
- Algorithm2.java: Contains the implementation of the same maze-solving algorithms with different Data structures.

### To use Algorithm2 for comparisons, swap Algorithm.java and Algorithm2.java in the src folder and rename them. Make sure the algorithm you wish to use is titled 'Algorithm.java' and placed inside the src folder. 

## Compiled Class Files (bin folder):

Contains the compiled .class files for the source files.

## Sample Maze Files (sample folder):

sample1.maze, sample2.maze, sample3.maze: Text files representing different maze configurations.

# Key Components

## Algorithm.java
This class implements three maze-solving algorithms:

- DFS (Depth-First Search): Uses a stack to explore nodes.
- BFS (Breadth-First Search): Uses a queue to explore nodes and keeps track of the previous nodes to reconstruct
the shortest path.
- A* Star Search: Uses a heuristic to prioritize nodes that are closer to the target.

## Node.java
This class represents a node in the maze with properties like position, color, and neighbors. It includes methods to render the node, handle clicks, and determine its type (wall, start, end, path).

## Main.java
This class sets up the GUI using Swing components and handles user interactions. It includes methods to:

- Initialize and render the maze.
- Handle mouse events to set start, end, and wall nodes.
- Save and load mazes from files.
- Execute the selected algorithm and display the runtime.

### Usage

- Running the Application: The main method in Main.java sets up the JFrame and starts the application.
- Interacting with the Maze: Users can click to set walls, start, and end nodes. The menu allows saving/loading mazes and selecting algorithms to solve the maze.

### Example Maze File (sample1.maze)

The maze files use a simple format where:

- 1 represents a wall.
- 0 represents a path.
- 2 represents the start node.
- 3 represents the end node.
