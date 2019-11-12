package com.luoExpress._09_Set.set;

import com.luoExpress._09_Set.list.LinkedList;
import com.luoExpress._09_Set.list.List;

public class ListSet<E> implements Set<E> {
	private List<E> list = new LinkedList<>();
	
	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public boolean contains(E element) {
		return list.contains(element);
	}

	@Override
	public void add(E element) {
//		if(list.contains(element)){
//			int index = list.indexOf(element);
//			list.set(index, element);
//		}
//		list.add(element);
		int index = list.indexOf(element);
		if (index != List.ELEMENT_NOT_FOUND) { // 存在就覆盖
			list.set(index, element);
		} else { // 不存在就添加
			list.add(element);
		}
	}

	@Override
	public void remove(E element) {
//		if (list.contains(element)) {
//			int index = list.indexOf(element);
//			list.remove(index);
//		}


		int index = list.indexOf(element);
		if (index != List.ELEMENT_NOT_FOUND) {
			list.remove(index);
		}
	}

	@Override
	public void traversal(Visitor<E> visitor) {
		if (visitor == null) return;
		
		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (visitor.visit(list.get(i))) return;
		}
	}

}
