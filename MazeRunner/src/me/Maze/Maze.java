package me.Maze;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class Maze {
    private MazeSquare[][] square;
    private Coordinate startPos;
    private Coordinate finishPos;
    private int numRows;
    private int numCols;
    @SuppressWarnings("rawtypes")
	private ArrayStack stack = new ArrayStack();
    

    public Maze(int numRows, int numCols) {
        this.numRows = numRows-1;
        this.numCols = numCols-1;
        square = new MazeSquare[numRows][numCols];
        for(int i = 0; i < numRows; i++){
        	for(int j = 0; j< numCols; j++){
        		square[i][j] = new MazeSquare(new Coordinate(i,j));
        		
        	}
        	
        }
        this.genMaze();
        this.clear();
        

        
    }
    

    public MazeSquare squareAt(Coordinate p) { 
    	return square[p.getRow()][p.getCol()];
    }

    public void visitPos(Coordinate p) { 
    	square[p.getRow()][p.getCol()].visit();
    }

    public void abandonPos(Coordinate p) { 
    	square[p.getRow()][p.getCol()].abandon();
    }

    public Coordinate getStart() { 

    	return startPos;
    	
    
    }

    public Coordinate getFinish() { 
    	return finishPos;
    	
    }

    public Coordinate northOf(Coordinate p) { 
    	return(new Coordinate(p.getRow()-1, p.getCol()));
    }

    public Coordinate eastOf(Coordinate p) { 
    	return(new Coordinate(p.getRow(), p.getCol()-1));
    }

    public Coordinate southOf(Coordinate p) { 
    	return(new Coordinate(p.getRow()+1, p.getCol()));
    }

    public Coordinate westOf(Coordinate p) { 
    	return(new Coordinate(p.getRow(), p.getCol()-1));
    }

    public boolean movePossible(Coordinate from, Coordinate to) { 
    	if(from.getRow() >= 0 && from.getRow() <= numRows && from.getCol() >= 0 && from.getCol() <=numCols && to.getRow() >= 0 && to.getRow() <= numRows && to.getCol() >= 0 && to.getCol() <= numCols ){
    		if(this.squareAt(to).isVisited() || this.squareAt(to).isAbandoned()){
    			
    			if(this.northOf(to).equals(from)){
    				return this.squareAt(to).getWall(Direction.NORTH);
    			}else if(this.eastOf(to).equals(from)){
    				return this.squareAt(to).getWall(Direction.EAST);
    			}else if(this.southOf(to).equals(from)){
    				return this.squareAt(to).getWall(Direction.SOUTH);
    			}else if(this.westOf(to).equals(from)){
    				return this.squareAt(to).getWall(Direction.WEST);
    			}else{
    				return false;
    			}
    		}
    		
    	}
    	return false;
    }

    /*
     * push the start position onto a stack.
while the stack is not empty:
Make the "current" position the Coordinates of the top element of the stack
visit the current position
if the current position has any unvisited neighbors (using the provided utility method) then:
pick one of the unvisited neighbors at random
remove all walls between the current position and the unvisited neighbor
push the position of the unvisited neighbor onto the stack, and go to 2
otherwise: pop the stack, and go to 2.

*/
    private void genMaze() { 
    	Random rnd = new Random();
    	
        startPos = new Coordinate(rnd.nextInt(numRows), 0);
        finishPos = new Coordinate(rnd.nextInt(numRows), numCols);
        this.squareAt(startPos).toggleWall(Direction.WEST);
        this.squareAt(finishPos).toggleWall(Direction.EAST);
    	stack.push(square[startPos.getRow()][startPos.getCol()].getPosition());
    	square[startPos.getRow()][startPos.getCol()].visit();
    	
    	
    	
    	while(stack.size() != -1){
    		if(this.unvisitedNeighbors((Coordinate) stack.top()).size() != 0){
    			ArrayList<Coordinate> list = new ArrayList<Coordinate>();
    			list = this.unvisitedNeighbors((Coordinate) stack.top());
    			Coordinate tempCoord = (Coordinate) stack.top();
    			int randTemp = rnd.nextInt(list.size());
    			stack.push(list.get(randTemp));
    			//just used casts...
    			Coordinate myNewPos = (Coordinate) stack.top();
    			
    			if(this.northOf(list.get(randTemp)).equals(tempCoord)){
    				this.squareAt(myNewPos).toggleWall(Direction.NORTH);
    			}else if(this.southOf(list.get(randTemp)).equals(tempCoord)){
    				this.squareAt(myNewPos).toggleWall(Direction.SOUTH);
    			}else if(this.eastOf(list.get(randTemp)).equals(tempCoord)){
    				this.squareAt(myNewPos).toggleWall(Direction.EAST);
    			}else if(this.westOf(list.get(randTemp)).equals(tempCoord)){
    				this.squareAt(myNewPos).toggleWall(Direction.WEST);
    			}
    			
    			this.visitPos(myNewPos);
    			
    		}else{
    			if(stack.size() == 0){
    				break;
    			}

    			stack.pop();
    		}
    		
    		
    	}
    	
    	
    	
    	
    	
    	
    }

    private void clear() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                square[i][j].clear();
            }
        }
    }

    // I'm going to leave this in here since we haven't
    // discussed the use of the ArrayList collection from the java library
    private ArrayList<Coordinate> unvisitedNeighbors(Coordinate p) {
        ArrayList<Coordinate> list = new ArrayList<Coordinate>();

        int r = p.getRow();
        int c = p.getCol();

        if (r > 0 && !square[r - 1][c].isVisited())
            list.add(new Coordinate(r - 1, c));
        if (r < numRows - 1 && !square[r + 1][c].isVisited())
            list.add(new Coordinate(r + 1, c));
        if (c > 0 && !square[r][c - 1].isVisited())
            list.add(new Coordinate(r, c - 1));
        if (c < numCols - 1 && !square[r][c + 1].isVisited())
            list.add(new Coordinate(r, c + 1));

        return list;
    }

    // this will be made use of in the maze solver but is not useful here.
    // Just checks if p is within the bounds of the maze.
    private boolean validPos(Coordinate p) {
        return ((p.getRow() < numRows) && (p.getRow() >= 0) && (p.getCol() < numCols) && (p.getCol() >= 0));
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        // output the top
        for (int i = 0; i < numCols; i++)
            buf.append("__");
        buf.append("_\n");

        // output the rows
        for (int i = 0; i < numRows; i++) {
            if (i != startPos.getRow()) {
                buf.append("|");
            } else {
                buf.append(" ");
            }

            for (int j = 0; j < numCols; j++) {
                if (square[i][j].getWall(Direction.SOUTH)) {
                    buf.append("_");
                } else {
                    buf.append(" ");
                }

                if (square[i][j].getWall(Direction.EAST)) {
                    buf.append("|");
                } else {
                    if (j + 1 < numCols) {
                        if (square[i][j + 1].getWall(Direction.SOUTH)) {
                            buf.append("_");
                        } else {
                            buf.append(".");
                        }
                    }
                }
            }
            buf.append("\n");
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        Scanner fromUser = new Scanner(System.in);
        System.out.print("rows? ");
        int r = fromUser.nextInt();
        System.out.print("cols? ");
        int c = fromUser.nextInt();

        Maze aMaze = new Maze(r, c);
        System.out.println(aMaze);
    }
}
