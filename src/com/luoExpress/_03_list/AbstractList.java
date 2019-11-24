package com.luoExpress._03_list;

public abstract class AbstractList<E> implements List<E>  {
	//static final int ELEMENT_NOT_FOUND = -1;
	/**
	 * Number of elements
	 */
	protected int size;//protected --->subclass

	/**
	 * Number of elements
	 * @return
	 */
	public int size() {
		return size;
	}

	/**
	 * isEmpty
	 * @return
	 */
	public boolean isEmpty() {
		 return size == 0;
	}

	/**
	 * contains elements
	 * @param element
	 * @return
	 */
	public boolean contains(E element) {
		return indexOf(element) != ELEMENT_NOT_FOUND;
	}

	/**
	 * Add elements to the end
	 * @param element
	 */
	public void add(E element) {
		add(size, element);
	}

	/**
	 * outOfBounds check
	 * @param index
	 */
	protected void outOfBounds(int index) {
		throw new IndexOutOfBoundsException("Index:" + index + ", Size:" + size);
	}

	/**
	 * range Check
	 * @param index
	 */
	protected void rangeCheck(int index) {
		if (index < 0 || index >= size) {
			outOfBounds(index);
		}
	}

	/**
	 * range Check ForAdd
	 * @param index
	 */
	protected void rangeCheckForAdd(int index) {
		if (index < 0 || index > size) {
			outOfBounds(index);
		}
	}
}
