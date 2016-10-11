package assignments.hotlines;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import sun.misc.Queue;

public class HotlinesRedux {
	
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		long startTime = System.currentTimeMillis();
		File file = new File("src/assignments/hotlines/edges.txt");
		PrintWriter writer = new PrintWriter("src/assignments/hotlines/paths.txt");
		Scanner in = new Scanner(file);
		
		int N = in.nextInt();
		in.nextLine();
		
		HashMap<Integer, ArrayList<Integer>> edges = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<String> collisions = new ArrayList<String>();
		
		System.out.println("Number of vertices: " + N);
		
		while (in.hasNextLine()) {
			String edgeS = in.nextLine();
			String[] edgeString = edgeS.split(",");
			
			if (edges.containsKey(Integer.parseInt(edgeString[0]))) {
				edges.get(Integer.parseInt(edgeString[0])).add(Integer.parseInt(edgeString[1]));
			} else {
				ArrayList<Integer> tempEdge = new ArrayList<Integer>();
				tempEdge.add(Integer.parseInt(edgeString[1]));
				edges.put(Integer.parseInt(edgeString[0]), tempEdge);
			};
		}
		
		boolean[] visited = new boolean[N + 1];
		boolean[] used = new boolean[N + 1];
		int[] pathTo = new int[N + 1];
		ArrayList<Integer> finalPath = new ArrayList<Integer>();
		Queue<Integer> q = new Queue<Integer>();
		q.enqueue(0);
		
		while (!q.isEmpty()) {
			int vertex = q.dequeue();
			if (edges.get(vertex) != null) {
				for (int i : edges.get(vertex)) {
					String ex = vertex + "," + i;
					if (!visited[i]) {
						pathTo[i] = vertex;
						visited[i] = true;
						q.enqueue(i);
					} else {
						collisions.add(ex);
					}
					
					if (i == N) {
						finalPath.add(vertex);
					}
				}
			}
		}
		
		ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
		System.out.println(collisions.size());
		for (int i : finalPath) {
			int current = i;
			ArrayList<Integer> path = new ArrayList<Integer>();
			path.add(N);
			while (current != 0) {
				boolean good = true;
				if (used[current] && current != 0) {
					good = false;
				}

				if (!good) {
					for (String s : collisions) {
						String[] ed = s.split(",");
						if (Integer.parseInt(ed[1]) == path.get(path.size()-1) && !used[Integer.parseInt(ed[0])]) {
							current = Integer.parseInt(s.split(",")[0]);
							break;
						}
					}
				}
				path.add(current);
				used[current] = true;
				current = pathTo[current];
			}
			path.add(0);
			Collections.reverse(path);
			paths.add(path);
		}
		
		System.out.println(paths);
		
		try {
			for (int i = 0; i < paths.size(); i++) {
				if (i != 0) {
					writer.print("\n");
				}
				for (Integer vertex : paths.get(i)) {
					if (vertex != 0) {
						writer.print(",");
					}
					writer.print(vertex);
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("There aren't any paths.");
		}
		
		writer.close();
		in.close();
		
		long now = System.currentTimeMillis() - startTime;
		long minutes = now / 36000;
		long seconds = (long) ((now / 1000) % 60);
		long ms = now % 1000;
		System.out.println("\nTotal run time: " + minutes + "m " + seconds + "s " + ms + "ms");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static ArrayList<ArrayList<ArrayList<Integer>>> findPaths(HashMap<Integer, ArrayList<Integer>> edges, ArrayList<ArrayList<ArrayList<Integer>>> pathCombinations, ArrayList<ArrayList<Integer>> paths, ArrayList<Integer> path, int lastVertex, int currentVertex) {
		ArrayList<Integer> edge = edges.get(currentVertex);
		int num = 0;
		
		if (edge != null) {
			 num = edge.size();
		}
		
		for (int i = 0; i < num; i++) {
			int vertex = edge.get(i);
			if (path.contains(vertex)) {
				continue;
			}
			boolean unique = true;
			
			for (ArrayList<Integer> p : paths) {
				if (p.contains(vertex) && vertex != lastVertex) {
					unique = false;
				}
			}
			
			if (unique) {
				path.add(vertex);
			} else {
				continue;
			}
			if (vertex != lastVertex) {
				findPaths(edges, pathCombinations, (ArrayList<ArrayList<Integer>>) paths.clone(), path, lastVertex, vertex);
			} else if (vertex == lastVertex) {
				paths.add((ArrayList<Integer>) path.clone());
				path.remove(path.size() - 1);
				
				//return pathCombinations;
			}
			path.remove(path.size() - 1);
		}
		
		pathCombinations.add(paths);
		
		return pathCombinations;
	}
}
