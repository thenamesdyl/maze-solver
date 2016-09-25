package me.Maze;

public class MazeSquare {

    // set the default wall state to no walls.

    private boolean[] wall = {false, false, false, false};
    private boolean visited;
    private boolean abandoned;
    private Coordinate myPosition;

    // new squares are built without walls
    public MazeSquare(Coordinate p) {
        myPosition = p;
        this.clear();
    }

    // this might be nice to have....
    public MazeSquare(Coordinate p, boolean[] wallSet) {
        this(p);
        for (int i = 0; i < wallSet.length && i < wall.length; i++) {
            wall[i] = wallSet[i];
        }
    }

    public void toggleWall(Direction dir) { 
    	wall[dir.value()] = !wall[dir.value()];
    }

    public boolean getWall(Direction dir) { 
    	return wall[dir.value()];
    }

    public boolean isVisited() { 
    	return visited;
    }

    public void visit() {
    	visited = true;
    }

    public boolean isAbandoned() { 
    	return abandoned;
    }

    public void abandon() { 
    	abandoned = true;
    	
    }

    public void clear() { 
    	visited = false;
    	abandoned = false;
    	
    }

    public Coordinate getPosition() { 
    	return myPosition;
    }

    public boolean equals(MazeSquare other) { 
    	
    	return other.getPosition().equals(this.getPosition());
    }
}