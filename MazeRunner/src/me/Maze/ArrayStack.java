package me.Maze;

import java.util.EmptyStackException;


public class ArrayStack<E> implements Stack {
	private E[] data;
	private int top;
	private int size;
	public ArrayStack(){
		top = -1;
		size = 100;
		data = (E[]) new Coordinate[100];
	}
	public ArrayStack(int n){
		top = -1;
		size = n;
		data = (E[]) new Coordinate[n];
	}

	public E pop() {


		E result = data[top];
		top--;	 
		return result;
	}

	private boolean isEmpty() {
		return (top == 0);
	}
	public E top() {
		return data[top];
	}

	public int size() {
		return top;
	}

	public void push(Object elt) {
		if (size()+1 == data.length){
	         expandStack();
		}
		
		top++;	      
		data[top] = (E) elt;

		
	}
	private void expandStack(){
		E[] larger = (E[])(new Coordinate[data.length*2]);

		for (int index=0; index < data.length; index++){
			larger[index] = data[index];
		}

		data = larger;
	   }



}