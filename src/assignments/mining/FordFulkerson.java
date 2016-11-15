package assignments.mining;

public class FordFulkerson {
	
	public static void main(String[] args) {
		int[][] graph = { 	{0, 16, 13, 0, 0, 0},
			                {0, 0, 10, 12, 0, 0},
			                {0, 4, 0, 0, 14, 0},
			                {0, 0, 9, 0, 0, 20},
			                {0, 0, 0, 7, 0, 4},
			                {0, 0, 0, 0, 0, 0}
              };
		
		int[][] flow = new int[graph.length][graph[0].length];
	}
	
	public static int maxFlow(int[][] graph, int[][] flow, int row, int col, int capacity) {
		int maxFlow = 0;
		
		for (int i = 0; i < graph[0].length; i++) {
			
		}
		
		return maxFlow;
	}

}
