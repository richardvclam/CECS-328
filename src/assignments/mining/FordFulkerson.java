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
		
		int[][] residualGraph = new int[graph.length][graph[0].length];
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph.length; j++) {
				residualGraph[i][j] = graph[i][j];
			}
		}
		int maxFlow = 0;
		for (int i = 0; i < 5; i++) {
			maxFlow += maxFlow(graph, residualGraph, new ArrayList<Integer>(), 0, Integer.MAX_VALUE, maxFlow);
			//System.out.println("Max flow: " + maxFlow);
		}
	}
	
	public static int maxFlow(int[][] capacityGraph, int[][] residualGraph, ArrayList<Integer> stack, int row, int capacity, int maxFlow) {
		if (row == capacityGraph.length - 1) {
			System.out.println(capacity);
			return capacity;
		}
		
		for (int i = 0; i < capacityGraph[row].length; i++) {
			if (i == row || stack.contains(i) || i == 0) {
				continue;
			}
			if (capacityGraph[row][i] > 0 /*&& residualGraph[row][i] < capacityGraph[row][i]*/) {
				stack.add(i);
				int flow = maxFlow(capacityGraph, residualGraph, stack, i, capacityGraph[row][i] < capacity ? capacityGraph[row][i] : capacity, maxFlow);
				stack.remove(stack.size() - 1);
				//residualGraph[row][i] -= flow;
				//residualGraph[i][row] += flow;
				capacityGraph[row][i] -= flow;
				capacityGraph[i][row] += flow;
				maxFlow = flow;
				break;
			}
		}
		
		return maxFlow;
	}

}
