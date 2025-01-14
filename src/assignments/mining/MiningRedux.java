package assignments.mining;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class MiningRedux {
	
	public static void main(String[] args) throws FileNotFoundException {
		long startTime = System.currentTimeMillis();
		File file = new File("src/assignments/mining/pit.txt");
		Scanner in = new Scanner(file);
		ArrayList<ArrayList<int[]>> pit = new ArrayList<ArrayList<int[]>>(); 
		int largestRow = 0;
		
		while (in.hasNextLine()) {
			String[] line = in.nextLine().split("\\),\\(|\\)|\\(");
			ArrayList<int[]> p = new ArrayList<int[]>();
			for (int i = 1; i < line.length; i++) {
				String[] temp = line[i].split(",");
				int[] a = {Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2])};
				p.add(a);
			}
			if (line.length > largestRow) {
				largestRow = line.length;
			}
			pit.add(p);
		}
		
		ArrayList<ArrayList<ArrayList<Integer>>> parents = new ArrayList<ArrayList<ArrayList<Integer>>>();
		
		for (int i = 1; i < pit.size(); i++) {
			ArrayList<ArrayList<Integer>> parentRow = new ArrayList<ArrayList<Integer>>();
			
			int currentParentNode = 0;	
			int[] parent = pit.get(i - 1).get(currentParentNode);
			int currentParentWidth = parent[0];
			
			for (int j = 0; j < pit.get(i).size(); j++) {
				int[] current = pit.get(i).get(j);
				boolean run = true;
				
				ArrayList<Integer> ps = new ArrayList<Integer>();
				
				if (currentParentWidth == 0) {
					currentParentNode++;
					parent = pit.get(i - 1).get(currentParentNode);
					currentParentWidth += parent[0];
				}
				
				do {
					if (currentParentWidth >= current[0]) {
						currentParentWidth -= current[0];
						ps.add(currentParentNode);
						run = false;
						break;
					} else if (currentParentWidth < current[0]) {
						ps.add(currentParentNode);
						currentParentNode++;
						parent = pit.get(i - 1).get(currentParentNode);
						currentParentWidth += parent[0];
					}
				} while (run);
				
				parentRow.add(ps);
			}
			parents.add(parentRow);
		}
		
		ArrayList<String> vertices = new ArrayList<String>();
		for (int i = 0; i < pit.size(); i++) {
			for (int j = 0; j < pit.get(i).size(); j++) {
				if (getPitValue(i, j, pit) > 0) {
					addParents(i, j, pit, parents, vertices);
				}
			}
		}
		int[][] graph = new int[vertices.size() + 2][vertices.size() + 2];
		
		// Make adj graph
		for (int i = 0; i < vertices.size(); i++) {
			String[] vertex = vertices.get(i).split(",");
			int value = getPitValue(Integer.parseInt(vertex[0]), Integer.parseInt(vertex[1]), pit);
			if (value > 0) {
				graph[0][i + 1] = value;
			} else {
				graph[i + 1][vertices.size()+1] = Math.abs(value);
			}

			if (Integer.parseInt(vertex[0]) == 0) {
				continue;
			}
			ArrayList<Integer> p = parents.get(Integer.parseInt(vertex[0]) - 1).get(Integer.parseInt(vertex[1]));
			for (int j = 0; j < p.size(); j++) {
				String pString = Integer.parseInt(vertex[0]) - 1 + "," + p.get(j);
				for (int k = 0; k < vertices.size(); k++) {
					if (vertices.get(k).equals(pString)) {
						graph[i+1][k+1] = Integer.MAX_VALUE;
						break;
					}
				}
			}
		}
		System.out.println("Number of vertices: " + graph.length);
		System.out.println("\nCreated adjacency matrix in " + (System.currentTimeMillis() - startTime) + " ms");
		int maxFlow = 0;
		int prevFlow = 0;
		do {
			if (prevFlow != maxFlow) {
				prevFlow = maxFlow;
			}
			//maxFlow += maxFlow(graph, new ArrayList<Integer>(), 0, Integer.MAX_VALUE, 0);
			maxFlow += bfsMaxFlow(graph);
			//System.out.println(maxFlow);
		} while (prevFlow != maxFlow);
		
		System.out.println("Finished max flow algorithm in " + (System.currentTimeMillis() - startTime) + " ms");
		
		boolean[][] marked = new boolean[pit.size()][largestRow];
		
		ArrayList<Integer> cuts = minCut(graph, new ArrayList<Integer>(), 0);
		
		for (int i = 1; i < cuts.size(); i++) {
			String[] pair = vertices.get(cuts.get(i) - 1).split(",");
			marked[Integer.parseInt(pair[0])][Integer.parseInt(pair[1])] = true;
		}
		
		System.out.println("\nTotal Profit: " + getTotalProfit(marked, pit) + "\n");
		
		PrintWriter writer = new PrintWriter("src/assignments/mining/blocks.txt");
		for (int i = 0; i < marked.length; i++) {
			ArrayList<Integer> valid = new ArrayList<Integer>();
			for (int j = 0; j < marked[i].length; j++) {
				if (marked[i][j] == true) {
					valid.add(j);
				}
			}
			for (int j = 0; j < valid.size(); j++) {
				if (j != 0) {
					System.out.print(",");
					writer.print(",");
				}
				System.out.print(valid.get(j));
				writer.print(valid.get(j));
			}
			System.out.println();
			writer.println();
		}
		
		writer.close();
		
		in.close();
		
		long now = System.currentTimeMillis() - startTime;
		long minutes = now / 36000;
		long seconds = (long) ((now / 1000) % 60);
		long ms = now % 1000;
		System.out.println("\nTotal run time: " + minutes + "m " + seconds + "s " + ms + "ms");
	}
	
	public static void addParents(int row, int col, ArrayList<ArrayList<int[]>> pit, ArrayList<ArrayList<ArrayList<Integer>>> parents, ArrayList<String> vertices) {
		String pair = row + "," + col;
		if (vertices.contains(pair)) {
			return;
		}
		vertices.add(pair);
		
		if (row == 0) {
			return;
		}

		ArrayList<Integer> p = parents.get(row - 1).get(col);
		for (int i = 0; i < p.size(); i++) {
			addParents(row - 1, p.get(i), pit, parents, vertices);
		}
	}
	
	public static int getPitValue(int row, int col, ArrayList<ArrayList<int[]>> pit) {
		int[] block = pit.get(row).get(col);
		return block[1] - block[2];
	}
	
	public static int maxFlow(int[][] capacityGraph, ArrayList<Integer> stack, int row, int capacity, int maxFlow) {
		if (row == capacityGraph.length - 1) {
			return capacity;
		}
		stack.add(row);
		for (int i = 0; i < capacityGraph[row].length; i++) {
			if (i == row || stack.contains(i)) {
				continue;
			}
			if (capacityGraph[row][i] > 0) {
				int flow = maxFlow(capacityGraph, stack, i, capacityGraph[row][i] < capacity ? capacityGraph[row][i] : capacity, maxFlow);
				if (flow > 0) {
					capacityGraph[row][i] -= flow;
					capacityGraph[i][row] += flow;
					maxFlow = flow;
					break;
				}
			}
		}
		stack.remove(stack.size() - 1);
		return maxFlow;
	}
	
	public static int bfsMaxFlow(int[][] capacityGraph) {
		boolean[] visited = new boolean[capacityGraph.length];
		int[] pathTo = new int[capacityGraph.length];
		int capacity = Integer.MAX_VALUE;
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(0);
	
		while (!q.isEmpty()) {
			int vertex = q.remove();
			for (int i = 0; i < capacityGraph.length; i++) {
				if (!visited[i] && capacityGraph[vertex][i] > 0) {
					visited[i] = true;
					pathTo[i] = vertex;
					q.add(i);
				}
			}
		}
		
		for (int i = capacityGraph.length - 1; i != 0; i = pathTo[i]) {
			if (capacityGraph[pathTo[i]][i] < capacity) {
				capacity = capacityGraph[pathTo[i]][i];
			}
		}

		for (int i = capacityGraph.length - 1; i != 0; i = pathTo[i]) {
			capacityGraph[pathTo[i]][i] -= capacity;
			capacityGraph[i][pathTo[i]] += capacity;
		}
		
		return capacity;
	}
	
	public static int getTotalProfit(boolean[][] mined, ArrayList<ArrayList<int[]>> pit) {
		int profit = 0;
		
		for (int i = 0; i < mined.length; i++) {
			for (int j = 0; j < mined[i].length; j++) {
				if (mined[i][j] == true) {
					int[] block = pit.get(i).get(j);
					profit += (block[1] - block[2]);
				}
			}
		}
		
		return profit;
	}
	
	public static ArrayList<Integer> minCut(int[][] capacityGraph, ArrayList<Integer> stack, int row) {
		stack.add(row);
		for (int i = 0; i < capacityGraph[row].length; i++) {
			if (i == row || stack.contains(i)) {
				continue;
			}
			if (capacityGraph[row][i] > 0) {
				minCut(capacityGraph, stack, i);
			}
		}
		return stack;
	}

}
