package com.luoExpress._04_stack;


import com.luoExpress._04_stack.list.ArrayList;
import com.luoExpress._04_stack.list.List;

public class Stack<E> {
	private List<E> list = new ArrayList<>();
	
	public void clear() {
		list.clear();
	}
	
	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public void push(E element) {
		list.add(element);
	}


	public E pop() {
		return list.remove(list.size() - 1);
	}


	public E peek() {
		return list.get(list.size() - 1);
	}
}
