package com.luoExpress._11_Graph.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import com.luoExpress._11_Graph.MinHeap;
import com.luoExpress._11_Graph.UnionFind;

@SuppressWarnings("unchecked")
public class ListGraph<V, E> extends Graph<V, E> {

	private Map<V, Vertex<V, E>> vertices = new HashMap<>();
	private Set<Edge<V, E>> edges = new HashSet<>();
	public ListGraph() {}
	public ListGraph(WeightManager<E> weightManager) {
		super(weightManager);
	}

	private static class Vertex<V, E> {
		V value;
		Set<Edge<V, E>> inEdges = new HashSet<>();
		Set<Edge<V, E>> outEdges = new HashSet<>();
		Vertex(V value) {

			this.value = value;
		}
		@Override
		public boolean equals(Object obj) {
			return Objects.equals(value, ((Vertex<V, E>)obj).value);
		}
		@Override
		public int hashCode() {
			return value == null ? 0 : value.hashCode();
		}
		@Override
		public String toString() {
			return value == null ? "null" : value.toString();
		}
	}
	
	private static class Edge<V, E> {
		Vertex<V, E> from;
		Vertex<V, E> to;
		E weight;
		
		Edge(Vertex<V, E> from, Vertex<V, E> to) {
			this.from = from;
			this.to = to;
			//this.weight = null;
		}
		
		EdgeInfo<V, E> info() {
			return new EdgeInfo<>(from.value, to.value, weight);
		}
		
		@Override
		public boolean equals(Object obj) {
			Edge<V, E> edge = (Edge<V, E>) obj;
			return Objects.equals(from, edge.from) && Objects.equals(to, edge.to);
		}
		@Override
		public int hashCode() {
			return from.hashCode() * 31 + to.hashCode();
		}

		@Override
		public String toString() {
			return "Edge [from=" + from + ", to=" + to + ", weight=" + weight + "]";
		}
	}
	

	private Comparator<Edge<V, E>> edgeComparator = (Edge<V, E> e1, Edge<V, E> e2) -> {
		return weightManager.compare(e1.weight, e2.weight);
	};
	
	public void print() {
		System.out.println("[顶点]-------------------");
		vertices.forEach((V v, Vertex<V, E> vertex) -> {
			System.out.println(v);
			System.out.println("out-----------");
			System.out.println(vertex.outEdges);
			System.out.println("in-----------");
			System.out.println(vertex.inEdges);
		});

		System.out.println("[边]-------------------");
		edges.forEach((Edge<V, E> edge) -> {
			System.out.println(edge);
		});
	}

	@Override
	public int edgesSize() {
		return edges.size();
	}

	@Override
	public int verticesSize() {
		return vertices.size();
	}

	@Override
	public void addVertex(V v) {
		if (vertices.containsKey(v)) return;
		vertices.put(v, new Vertex<>(v));
	}

	@Override
	public void addEdge(V from, V to) {
		addEdge(from, to, null);
	}

	@Override
	public void addEdge(V from, V to, E weight) {
		Vertex<V, E> fromVertex = vertices.get(from);
		if (fromVertex == null) {
			fromVertex = new Vertex<>(from);
			vertices.put(from, fromVertex);
		}
		
		Vertex<V, E> toVertex = vertices.get(to);
		if (toVertex == null) {
			toVertex = new Vertex<>(to);
			vertices.put(to, toVertex);
		}

		Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
		edge.weight = weight;
//		if (toVertex.outEdges.contains(edge)) {
//			toVertex.inEdges.remove(edge);
//			edges.remove(edge);
//		}
		if (fromVertex.outEdges.remove(edge)) {
			toVertex.inEdges.remove(edge);
			edges.remove(edge);
		}
		fromVertex.outEdges.add(edge);
		toVertex.inEdges.add(edge);
		edges.add(edge);
	}

	@Override
	public void removeEdge(V from, V to) {
		Vertex<V, E> fromVertex = vertices.get(from);
		if (fromVertex == null) return;
		
		Vertex<V, E> toVertex = vertices.get(to);
		if (toVertex == null) return;
		
		Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
		if (fromVertex.outEdges.remove(edge)) {
			toVertex.inEdges.remove(edge);
			edges.remove(edge);
		}
	}

	@Override
	public void removeVertex(V v) {
		Vertex<V, E> vertex = vertices.remove(v);
		if (vertex == null) return;
		//java中如果想一边遍历一边删除需要使用迭代器
		for (Iterator<Edge<V, E>> iterator = vertex.outEdges.iterator(); iterator.hasNext();) {
			Edge<V, E> edge = iterator.next();
			edge.to.inEdges.remove(edge);
			//vertex.outEdges.remove(edge);error!!!不可以这么写，一边遍历一边删除 是不允许的，多线程危险操作
			// 将当前遍历到的元素edge从集合vertex.outEdges中删掉
			iterator.remove();
			edges.remove(edge);
		}

		for (Iterator<Edge<V, E>> iterator = vertex.inEdges.iterator(); iterator.hasNext();) {
			Edge<V, E> edge = iterator.next();
			edge.from.outEdges.remove(edge);
			// 将当前遍历到的元素edge从集合vertex.inEdges中删掉
			iterator.remove(); 
			edges.remove(edge);
		}
	}

	@Override
	//类似二叉树层序遍历
	public void bfs(V begin, VertexVisitor<V> visitor) {
		if (visitor == null) return;
		Vertex<V, E> beginVertex = vertices.get(begin);
		if (beginVertex == null) return;
		
		Set<Vertex<V, E>> visitedVertices = new HashSet<>();//记录已经被遍历过的点
		Queue<Vertex<V, E>> queue = new LinkedList<>();//LinkedList是一个非常干净的双端连表
		queue.offer(beginVertex);
		visitedVertices.add(beginVertex);

		while (!queue.isEmpty()) {
			Vertex<V, E> vertex = queue.poll();

			//System.out.println(vertex.value);
			if (visitor.visit(vertex.value)) return;
			//根据outedges找后续的顶点
			for (Edge<V, E> edge : vertex.outEdges) {
				if (visitedVertices.contains(edge.to)) continue;
				queue.offer(edge.to);
				visitedVertices.add(edge.to);
			}
		}
	}

	/**
	 * 1.先打印根节点（判断是否已经被访问过），然后从outEdges中选择一条边
	 * 2.将被选择边的from、to按顺序入栈
	 * 3.打印被选择边的to
	 * 4.将to加到已经访问的范围中
	 * 5.break（不去访问outEdges中的其他边，而且访问一条边上的剩余点）
	 * 6.弹出栈顶元素
	 * @param begin
	 * @param visitor
	 */
	@Override
	public void dfs(V begin, VertexVisitor<V> visitor) {
		if (visitor == null) return;
		Vertex<V, E> beginVertex = vertices.get(begin);
		if (beginVertex == null) return;

		Set<Vertex<V, E>> visitedVertices = new HashSet<>();
		Stack<Vertex<V, E>> stack = new Stack<>();
		
		// 先访问起点
		stack.push(beginVertex);
		visitedVertices.add(beginVertex);
		//System.out.println(beginVertex.value);
		if (visitor.visit(begin)) return;
		
		while (!stack.isEmpty()) {
			Vertex<V, E> vertex = stack.pop();
			
			for (Edge<V, E> edge : vertex.outEdges) {
				if (visitedVertices.contains(edge.to)) continue;
				
				stack.push(edge.from);
				stack.push(edge.to);
				visitedVertices.add(edge.to);
				//System.out.println(edge.to.value);
				if (visitor.visit(edge.to.value)) return;
				
				break;
			}
		}
	}

	public void dfs2(V begin) {
		Vertex<V, E> beginVertex = vertices.get(begin);
		if (beginVertex == null) return;
		Set<Vertex<V,E>> visitedVertices = new HashSet<>();
		dfs2(beginVertex, visitedVertices);
	}

	private void dfs2(Vertex<V, E> vertex, Set<Vertex<V, E>> visitedVertices) {
		System.out.println(vertex.value);
		visitedVertices.add(vertex);

		for (Edge<V, E> edge : vertex.outEdges) {
			if (visitedVertices.contains(edge.to)) continue;
			dfs2(edge.to, visitedVertices);
		}
	}

	@Override
	public List<V> topologicalSort() {
		List<V> list = new ArrayList<>();
		Queue<Vertex<V, E>> queue = new LinkedList<>();
		Map<Vertex<V, E>, Integer> ins = new HashMap<>();
		// 初始化（将度为0的节点都放入队列）
		vertices.forEach((V v, Vertex<V, E> vertex) -> {
			int in = vertex.inEdges.size();
			if (in == 0) {
				queue.offer(vertex);
			} else {
				ins.put(vertex, in);
			}
		});
		
		while (!queue.isEmpty()) {
			Vertex<V, E> vertex = queue.poll();
			// 放入返回结果中
			list.add(vertex.value);
			
			for (Edge<V, E> edge : vertex.outEdges) {
				int toIn = ins.get(edge.to) - 1;
				if (toIn == 0) {
					queue.offer(edge.to);
				} else {
					ins.put(edge.to, toIn);
				}
			}
		}
		
		return list;
	}

	@Override
	public Set<EdgeInfo<V, E>> mst() {
		return kruskal();
		//return Math.random() > 0.5 ? prim() : kruskal();
	}
	
	private Set<EdgeInfo<V, E>> prim() {
		
		//记录已经在mst中点
		Set<Vertex<V,E>> addedVertices = new HashSet<>();
		//记录最小生成树的边信息的
		Set<EdgeInfo<V, E>> edgInfos = new HashSet<>();
		Iterator<Vertex<V, E>> iterator = vertices.values().iterator();//java中每个容器java都封装le一个迭代器
		if (!iterator.hasNext()) return null;
		Vertex<V, E> vertex = iterator.next();//A
		addedVertices.add(vertex);//A
		MinHeap<Edge<V,E>> heap = new MinHeap<>(vertex.outEdges,edgeComparator);
		while (!heap.isEmpty()&& addedVertices.size()<vertices.size()){
			Edge<V, E> removed = heap.remove();
			if (addedVertices.contains(removed.to)) continue;
			edgInfos.add(removed.info());
			addedVertices.add(removed.to);//B
			heap.addAll(removed.to.outEdges);
		}

		return edgInfos;
	}
	
	private Set<EdgeInfo<V, E>> kruskal() {
		//记录最小生成树的边信息的
		Set<EdgeInfo<V, E>> edgInfos = new HashSet<>();
		MinHeap<Edge<V,E>> heap = new MinHeap<>(edges,edgeComparator);
		UnionFind<Vertex<V,E>> unionFind = new UnionFind<>();
		//		for (Map.Entry<V, Vertex<V, E>> entry : vertices.entrySet())
//		{
//			//V key = entry.getKey();
//			Vertex<V, E> vertex = entry.getValue();
//
//			unionFind.makeSet(vertex);
//		}
		vertices.forEach((V v,Vertex<V, E> vertex)->{//entry--->value
			unionFind.makeSet(vertex);
		}
	);
		while (!heap.isEmpty()&& edgInfos.size()<verticesSize()-1){
			Edge<V, E> removed = heap.remove();
			if (unionFind.isSame(removed.to,removed.from)) continue;
			edgInfos.add(removed.info());
			unionFind.union(removed.from,removed.to);
		}

		return edgInfos;
	}

//	public Map<V, E> shortestPath(V begin) {
//		Vertex<V, E> beginVertex = vertices.get(begin);
//		if (beginVertex == null) return null;
//		
//		Map<V, E> selectedPaths = new HashMap<>();
//		Map<Vertex<V, E>, E> paths = new HashMap<>();
//		// 初始化paths
//		for (Edge<V, E> edge : beginVertex.outEdges) {
//			paths.put(edge.to, edge.weight);
//		}
//
//		while (!paths.isEmpty()) {
//			Entry<Vertex<V, E>, E> minEntry = getMinPath(paths);
//			// minVertex离开桌面
//			Vertex<V, E> minVertex = minEntry.getKey();
//			selectedPaths.put(minVertex.value, minEntry.getValue());
//			paths.remove(minVertex);
//			// 对它的minVertex的outEdges进行松弛操作
//			for (Edge<V, E> edge : minVertex.outEdges) {
//				// 如果edge.to已经离开桌面，就没必要进行松弛操作
//				if (selectedPaths.containsKey(edge.to.value)) continue;
//				// 新的可选择的最短路径：beginVertex到edge.from的最短路径 + edge.weight
//				E newWeight = weightManager.add(minEntry.getValue(), edge.weight);
//				// 以前的最短路径：beginVertex到edge.to的最短路径
//				E oldWeight = paths.get(edge.to);
//				if (oldWeight == null || weightManager.compare(newWeight, oldWeight) < 0) {
//					paths.put(edge.to, newWeight);
//				}
//			}
//		}
//		
//		selectedPaths.remove(begin);
//		return selectedPaths;
//	}
	
	@Override
	public Map<V, PathInfo<V, E>> shortestPath(V begin) {
		return dijkstra(begin);
	}

	@SuppressWarnings("unused")
	private Map<V, PathInfo<V, E>> bellmanFord(V begin) {
		Vertex<V, E> beginVertex = vertices.get(begin);
		if (beginVertex == null) return null;
		
		Map<V, PathInfo<V, E>> selectedPaths = new HashMap<>();
		selectedPaths.put(begin, new PathInfo<>(weightManager.zero()));
		
		int count = vertices.size() - 1;
		for (int i = 0; i < count; i++) { // v - 1 次
			for (Edge<V, E> edge : edges) {
				PathInfo<V, E> fromPath = selectedPaths.get(edge.from.value);
				if (fromPath == null) continue;
				relax(edge, fromPath, selectedPaths);
			}
		}
		
		for (Edge<V, E> edge : edges) {
			PathInfo<V, E> fromPath = selectedPaths.get(edge.from.value);
			if (fromPath == null) continue;
			if (relax(edge, fromPath, selectedPaths)) {
				System.out.println("有负权环");
				return null;
			}
		}
		
		selectedPaths.remove(begin);
		return selectedPaths;
	}

	
	/**
	 * 松弛
	 * @param edge 需要进行松弛的边
	 * @param fromPath edge的from的最短路径信息
	 * @param paths 存放着其他点（对于dijkstra来说，就是还没有离开桌面的点）的最短路径信息
	 */
	private boolean relax(Edge<V, E> edge, PathInfo<V, E> fromPath, Map<V, PathInfo<V, E>> paths) {
		// 新的可选择的最短路径：beginVertex到edge.from的最短路径 + edge.weight
		E newWeight = weightManager.add(fromPath.weight, edge.weight);
		// 以前的最短路径：beginVertex到edge.to的最短路径
		PathInfo<V, E> oldPath = paths.get(edge.to.value);
		if (oldPath != null && weightManager.compare(newWeight, oldPath.weight) >= 0) return false;
		
		if (oldPath == null) {
			oldPath = new PathInfo<>();
			paths.put(edge.to.value, oldPath);
		} else {
			oldPath.edgeInfos.clear();
		}

		oldPath.weight = newWeight;
		oldPath.edgeInfos.addAll(fromPath.edgeInfos);
		oldPath.edgeInfos.add(edge.info());
		
		return true;
	}
	
	private Map<V, PathInfo<V, E>> dijkstra(V begin) {
		Vertex<V, E> beginVertex = vertices.get(begin);
		if (beginVertex == null) return null;
		
		Map<V, PathInfo<V, E>> selectedPaths = new HashMap<>();
		Map<Vertex<V, E>, PathInfo<V, E>> paths = new HashMap<>();
		paths.put(beginVertex, new PathInfo<>(weightManager.zero()));
		// 初始化paths
//		for (Edge<V, E> edge : beginVertex.outEdges) {
//			PathInfo<V, E> path = new PathInfo<>();
//			path.weight = edge.weight;
//			path.edgeInfos.add(edge.info());
//			paths.put(edge.to, path);
//		}

		while (!paths.isEmpty()) {
			Entry<Vertex<V, E>, PathInfo<V, E>> minEntry = getMinPath(paths);
			// minVertex离开桌面
			Vertex<V, E> minVertex = minEntry.getKey();
			PathInfo<V, E> minPath = minEntry.getValue();
			selectedPaths.put(minVertex.value, minPath);
			paths.remove(minVertex);
			// 对它的minVertex的outEdges进行松弛操作
			for (Edge<V, E> edge : minVertex.outEdges) {
				// 如果edge.to已经离开桌面，就没必要进行松弛操作
				if (selectedPaths.containsKey(edge.to.value)) continue;
				relaxForDijkstra(edge, minPath, paths);
			}
		}
		
		selectedPaths.remove(begin);
		return selectedPaths;
	}
	
	/**
	 * 松弛
	 * @param edge 需要进行松弛的边
	 * @param fromPath edge的from的最短路径信息
	 * @param paths 存放着其他点（对于dijkstra来说，就是还没有离开桌面的点）的最短路径信息
	 */
	private void relaxForDijkstra(Edge<V, E> edge, PathInfo<V, E> fromPath, Map<Vertex<V, E>, PathInfo<V, E>> paths) {
		// 新的可选择的最短路径：beginVertex到edge.from的最短路径 + edge.weight
		E newWeight = weightManager.add(fromPath.weight, edge.weight);
		// 以前的最短路径：beginVertex到edge.to的最短路径
		PathInfo<V, E> oldPath = paths.get(edge.to);
		if (oldPath != null && weightManager.compare(newWeight, oldPath.weight) >= 0) return;
		
		if (oldPath == null) {
			oldPath = new PathInfo<>();
			paths.put(edge.to, oldPath);
		} else {
			oldPath.edgeInfos.clear();
		}

		oldPath.weight = newWeight;
		oldPath.edgeInfos.addAll(fromPath.edgeInfos);
		oldPath.edgeInfos.add(edge.info());
	}
	
	/**
	 * 从paths中挑一个最小的路径出来
	 * @param paths
	 * @return
	 */
	private Entry<Vertex<V, E>, PathInfo<V, E>> getMinPath(Map<Vertex<V, E>, PathInfo<V, E>> paths) {
		Iterator<Entry<Vertex<V, E>, PathInfo<V, E>>> it = paths.entrySet().iterator();
		Entry<Vertex<V, E>, PathInfo<V, E>> minEntry = it.next();
		while (it.hasNext()) {
			Entry<Vertex<V, E>, PathInfo<V, E>> entry = it.next();
			if (weightManager.compare(entry.getValue().weight, minEntry.getValue().weight) < 0) {
				minEntry = entry;
			}
		}
		return minEntry;
	}
	
	@Override
	public Map<V, Map<V, PathInfo<V, E>>> shortestPath() {
		Map<V, Map<V, PathInfo<V, E>>> paths = new HashMap<>();
		// 初始化
		for (Edge<V, E> edge : edges) {
			Map<V, PathInfo<V, E>> map = paths.get(edge.from.value);
			if (map == null) {
				map = new HashMap<>();
				paths.put(edge.from.value, map);
			}
			
			PathInfo<V, E> pathInfo = new PathInfo<>(edge.weight);
			pathInfo.edgeInfos.add(edge.info());
			map.put(edge.to.value, pathInfo);
		}

		vertices.forEach((V v2, Vertex<V, E> vertex2) -> {
			vertices.forEach((V v1, Vertex<V, E> vertex1) -> {
				vertices.forEach((V v3, Vertex<V, E> vertex3) -> {
					if (v1.equals(v2) || v2.equals(v3) || v1.equals(v3)) return;
					
					// v1 -> v2
					PathInfo<V, E> path12 = getPathInfo(v1, v2, paths);
					if (path12 == null) return;
					
					// v2 -> v3
					PathInfo<V, E> path23 = getPathInfo(v2, v3, paths);
					if (path23 == null) return;
					
					// v1 -> v3 
					PathInfo<V, E> path13 = getPathInfo(v1, v3, paths);
					
					E newWeight = weightManager.add(path12.weight, path23.weight);
					if (path13 != null && weightManager.compare(newWeight, path13.weight) >= 0) return;
					
					if (path13 == null) {
						path13 = new PathInfo<V, E>();
						paths.get(v1).put(v3, path13);
					} else {
						path13.edgeInfos.clear();
					}
					
					path13.weight = newWeight;
					path13.edgeInfos.addAll(path12.edgeInfos);
					path13.edgeInfos.addAll(path23.edgeInfos);
				});
			});
		});
		
		return paths;
	}
	
	private PathInfo<V, E> getPathInfo(V from, V to, Map<V, Map<V, PathInfo<V, E>>> paths) {
		Map<V, PathInfo<V, E>> map = paths.get(from);
		return map == null ? null : map.get(to);
	}

//	@Override
//	public void bfs(V begin) {
//		Vertex<V, E> beginVertex = vertices.get(begin);
//		if (beginVertex == null) return;
//		
//		Set<Vertex<V, E>> visitedVertices = new HashSet<>();
//		Queue<Vertex<V, E>> queue = new LinkedList<>();
//		queue.offer(beginVertex);
//		visitedVertices.add(beginVertex);
//		
//		while (!queue.isEmpty()) {
//			Vertex<V, E> vertex = queue.poll();
//			System.out.println(vertex.value);
//			
//			for (Edge<V, E> edge : vertex.outEdges) {
//				if (visitedVertices.contains(edge.to)) continue;
//				queue.offer(edge.to);
//				visitedVertices.add(edge.to);
//			}
//		}
//	}
//
//	@Override
//	public void dfs(V begin) {
//		Vertex<V, E> beginVertex = vertices.get(begin);
//		if (beginVertex == null) return;
//
//		Set<Vertex<V, E>> visitedVertices = new HashSet<>();
//		Stack<Vertex<V, E>> stack = new Stack<>();
//		
//		// 先访问起点
//		stack.push(beginVertex);
//		visitedVertices.add(beginVertex);
//		System.out.println(beginVertex.value);
//		
//		while (!stack.isEmpty()) {
//			Vertex<V, E> vertex = stack.pop();
//			
//			for (Edge<V, E> edge : vertex.outEdges) {
//				if (visitedVertices.contains(edge.to)) continue;
//				
//				stack.push(edge.from);
//				stack.push(edge.to);
//				visitedVertices.add(edge.to);
//				System.out.println(edge.to.value);
//				
//				break;
//			}
//		}
//	}
	

}
