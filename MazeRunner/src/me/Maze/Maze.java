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
	private ArrayStack<Coordinate> stack = new ArrayStack<Coordinate>();
    private boolean[] wallState = {false,true,true,false};
    

    public Maze(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        square = new MazeSquare[numRows][numCols];
        for(int i = 0; i < numRows; i++){
        	for(int j = 0; j< numCols; j++){
        		square[i][j] = new MazeSquare(new Coordinate(i,j), wallState);
        		
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
    	return(new Coordinate(p.getRow(), p.getCol()+1));
    }

    public Coordinate southOf(Coordinate p) { 
    	return(new Coordinate(p.getRow()+1, p.getCol()));
    }

    public Coordinate westOf(Coordinate p) { 
    	return(new Coordinate(p.getRow(), p.getCol()-1));
    }

    public boolean movePossible(Coordinate from, Coordinate to) { 
//getRow >= 0 so its within border and getRow < numRows because of west edge
    	if(from.getRow() >= 0 && from.getRow() < numRows && from.getCol() >= 0 && from.getCol() < numCols && to.getRow() >= 0 && to.getRow() < numRows && to.getCol() >= 0 && to.getCol() < numCols ){
//isVisited will be false then changed to true to move past if. Same with isAbandoned
    		if(!this.squareAt(to).isVisited() && !this.squareAt(to).isAbandoned()){

    			if(this.northOf(from).equals(to)){
    				return !this.squareAt(to).getWall(Direction.SOUTH);
    			}else if(this.eastOf(from).equals(to)){
    				return !this.squareAt(from).getWall(Direction.EAST);
    			}else if(this.southOf(from).equals(to)){
    				return !this.squareAt(from).getWall(Direction.SOUTH);
    			}else if(this.westOf(from).equals(to)){
    				return !this.squareAt(to).getWall(Direction.EAST);
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
        finishPos = new Coordinate(rnd.nextInt(numRows), numCols-1);
        
        for(int i = 0; i< numCols; i++){
        	this.squareAt(new Coordinate(0, i)).toggleWall(Direction.NORTH);
        }
        for(int i = 0; i< numRows; i++){
        	this.squareAt(new Coordinate(i,0)).toggleWall(Direction.WEST);
        }
        
        
        this.squareAt(startPos).toggleWall(Direction.WEST);
        this.squareAt(finishPos).toggleWall(Direction.EAST);
    	square[startPos.getRow()][startPos.getCol()].visit();
    	stack.push(startPos);
    	
    	
    	while(stack.size() != -1){
    		if(this.unvisitedNeighbors(stack.top()).size() != 0){
    			ArrayList<Coordinate> list = new ArrayList<Coordinate>();
    			list = this.unvisitedNeighbors(stack.top());
    			int randTemp = rnd.nextInt(list.size());
    			Coordinate myOldPos = stack.top();
    			stack.push(list.get(randTemp));
    			Coordinate myNewPos = stack.top();
    			
    			if(this.northOf(myOldPos).equals(myNewPos) && this.squareAt(myNewPos).getWall(Direction.SOUTH)){
    				this.squareAt(myNewPos).toggleWall(Direction.SOUTH);
    			}else if(this.southOf(myOldPos).equals(myNewPos) && this.squareAt(myOldPos).getWall(Direction.SOUTH)){
    				this.squareAt(myOldPos).toggleWall(Direction.SOUTH);
    			}else if(this.eastOf(myOldPos).equals(myNewPos) &&  this.squareAt(myOldPos).getWall(Direction.EAST)){
    				this.squareAt(myOldPos).toggleWall(Direction.EAST);
    			}else if(this.westOf(myOldPos).equals(myNewPos) && this.squareAt(myNewPos).getWall(Direction.EAST)){
    				this.squareAt(myNewPos).toggleWall(Direction.EAST);
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
