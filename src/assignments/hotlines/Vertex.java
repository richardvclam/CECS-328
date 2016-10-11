package assignments.hotlines;

import java.util.ArrayList;

public class Vertex {
	
	int vertex;
	ArrayList<Integer> edges, from, to;
	
	public Vertex(int vertex) {
		this.vertex = vertex;
		edges = new ArrayList<Integer>();
		from = new ArrayList<Integer>();
		to = new ArrayList<Integer>();
	}
	
	public void addEdge(int edge) {
		edges.add(edge);
	}
	
	@Override
	public int hashCode() {
		return vertex;
	}

}
