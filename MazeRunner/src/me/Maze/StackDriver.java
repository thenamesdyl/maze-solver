package me.Maze;

public class StackDriver {
	public static void main(String[] args){
		Coordinate coord = new Coordinate(5,6);
		Coordinate coord2 = new Coordinate(7,2);
		Coordinate coord3 = new Coordinate(9,3);

		ArrayStack stack = new ArrayStack();
		System.out.println(stack.size());
		stack.push(coord);
		System.out.println(stack.top().toString());
		stack.push(coord2);
		System.out.println(stack.pop().toString());
		//removed top so now it should be 5,6
		System.out.println(stack.top().toString());
		//should be 0
		System.out.println(stack.size());
		for(int i = 0; i<1000; i++){
			stack.push(coord);
		}
	    
		stack.push(coord3);
		System.out.println(stack.top().toString());
		//should be 1001
		System.out.println(stack.size());

		
		
		
	}


}
