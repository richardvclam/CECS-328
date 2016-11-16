package assignments.mining;

import java.util.ArrayList;

public class FordFulkerson {
	
	public static void main(String[] args) {
		int[][] graph = { 	{0, 16, 13, 0, 0, 0},
			                {0, 0, 10, 12, 0, 0},
			                {0, 4, 0, 0, 14, 0},
			                {0, 0, 9, 0, 0, 20},
			                {0, 0, 0, 7, 0, 4},
			                {0, 0, 0, 0, 0, 0}
              };
		int[][] graph2 = { 	{0, 10, 10, 0, 0, 0},
			                {0, 0, 2, 4, 8, 0},
			                {0, 0, 0, 0, 9, 0},
			                {0, 0, 0, 0, 0, 10},
			                {0, 0, 0, 6, 0, 10},
			                {0, 0, 0, 0, 0, 0}
				};
		
		int inf = Integer.MAX_VALUE;
		int[][] graph3 = { 	{0, 20, 9, 19, 0, 0, 0, 0, 0, 0, 0, 0},
			                {0, 0, 0, 0, inf, inf, 0, 0, 0, 0, 0, 0},
			                {0, 0, 0, 0, 0, inf, 0, 0, 0, 0, 0, 0},
			                {0, 0, 0, 0, 0, inf, inf, inf, 0, 0, 0, 0},
			                {0, 0, 0, 0, 0, 0, 0, 0, inf, inf, 0, 7},
			                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 14},
			                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, inf, 12},
			                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, inf, 3},
			                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7},
			                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
			                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
			                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
		};
		
		int[][] graph4 = {  {0, 1000, 1000, 0},
							{0, 0, 1, 1000},
							{0, 0, 0, 1000},
							{0, 0, 0, 0}
				
		};

		int maxFlow = 0;
		int prevFlow = 0;
		do {
			if (prevFlow != maxFlow) {
				prevFlow = maxFlow;
			}
			maxFlow += maxFlow(graph2, new ArrayList<Integer>(), 0, Integer.MAX_VALUE, 0);
			System.out.println("Max flow: " + maxFlow);
			
		} while (prevFlow != maxFlow);
		
		ArrayList<Integer> cuts = minCut(graph2, new ArrayList<Integer>(), 0);
		System.out.println(cuts);
	}
	
	public static ArrayList<Integer> minCut(int[][] capacityGraph, ArrayList<Integer> stack, int row) {
		stack.add(row);
		for (int i = 1; i < capacityGraph[row].length; i++) {
			if (i == row || stack.contains(i)) {
				continue;
			}
			if (capacityGraph[row][i] > 0) {
				minCut(capacityGraph, stack, i);
			}
		}
		return stack;
	}
	
	public static int maxFlow(int[][] capacityGraph, ArrayList<Integer> stack, int row, int capacity, int maxFlow) {
		if (row == capacityGraph.length - 1) {
			System.out.println(capacity);
			return capacity;
		}
		stack.add(row);
		for (int i = 1; i < capacityGraph[row].length; i++) {
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

}
