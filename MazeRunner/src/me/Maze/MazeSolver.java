package me.Maze;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MazeSolver {
    public static MazeDisplay myWindow;


    public static void findPath(Maze theMaze) {
        // solver algorithm goes here.
        // any time a MazeSquare's state changes
        // your code must call myWindow.update(Coordinate)
        // with the coordinates of the changed square.Random rnd = new Random();
    	Random rnd = new Random();
    	ArrayStack<Coordinate> stack = new ArrayStack<Coordinate>();
        stack.push(theMaze.getStart());
    	theMaze.visitPos(theMaze.getStart());
    	myWindow.update(theMaze.getStart());
    	
    	while(stack.size() != -1){
    		if(theMaze.squareAt(stack.top()).equals(theMaze.getFinish())){
    			stack.push(theMaze.getFinish());
    			theMaze.visitPos(theMaze.getFinish());
    			myWindow.update(stack.top());
    			System.out.println("finished");
    			break;
    		}
    		
    		if(theMaze.movePossible(stack.top(), theMaze.northOf(stack.top()))){
    			System.out.println("here1");
    			stack.push(theMaze.squareAt(theMaze.northOf(stack.top())));
    		    theMaze.visitPos(stack.top());
    		    myWindow.update(stack.top());
    		}else if(theMaze.movePossible(stack.top(), theMaze.eastOf(stack.top()))){
    			System.out.println("here2");
    			stack.push(theMaze.squareAt(theMaze.eastOf(stack.top())));
    			theMaze.visitPos(stack.top());
    			myWindow.update(stack.top());
    		}else if(theMaze.movePossible(stack.top(), theMaze.southOf(stack.top()))){
    			System.out.println("here3");
    			stack.push(theMaze.squareAt(theMaze.southOf(stack.top())));
    			theMaze.visitPos(stack.top());
    			myWindow.update(stack.top());
    		}else if(theMaze.movePossible(stack.top(), theMaze.westOf(stack.top()))){
    			System.out.println("here4");
    			stack.push(theMaze.squareAt(theMaze.westOf(stack.top())));
    			theMaze.visitPos(stack.top());
    			myWindow.update(stack.top());
    		}else{
    			if(stack.size() == 0){
    				System.out.println(stack.top().getRow());
    				break;
    			}
    			
                theMaze.abandonPos(stack.top());
                myWindow.update(stack.top());
    			stack.pop();
    			
    		}
    		
    		
    	}
    }

    public static void main(String[] args) {
        Scanner fromUser = new Scanner(System.in);
        System.out.print("Number of rows? ");
        int ROWS = fromUser.nextInt();
        System.out.print("Number of cols? ");
        int COLS = fromUser.nextInt();

        for (int i = 0; i < 5; i++) {
            Maze aMaze = new Maze(ROWS, COLS);
            myWindow = new MazeDisplay(aMaze, ROWS, COLS);
            myWindow.showMaze();
            findPath(aMaze);
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
            }
            myWindow.destroyMaze();
        }
    }
}