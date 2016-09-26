package assignments.hotlines;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class HotlinesRedux {
	
	public static void main(String[] args) throws FileNotFoundException {
		long startTime = System.currentTimeMillis();
		File file = new File("src/assignments/hotlines/edges_samplee.txt");
		PrintWriter writer = new PrintWriter("src/assignments/hotlines/paths.txt");
		Scanner in = new Scanner(file);
		
		int N = in.nextInt();
		in.nextLine();
		
		HashMap<Integer, ArrayList<Integer>> edges = new HashMap<Integer, ArrayList<Integer>>();
		
		System.out.println("Number of vertices: " + N);
		
		while (in.hasNextLine()) {
			String[] edgeString = in.nextLine().split(",");
			
			if (edges.containsKey(Integer.parseInt(edgeString[0]))) {
				edges.get(Integer.parseInt(edgeString[0])).add(Integer.parseInt(edgeString[1]));
			} else {
				ArrayList<Integer> edge = new ArrayList<Integer>();
				edge.add(Integer.parseInt(edgeString[1]));
				edges.put(Integer.parseInt(edgeString[0]), edge);
			};
		}
		
		ArrayList<ArrayList<ArrayList<Integer>>> pathCombinations = new ArrayList<ArrayList<ArrayList<Integer>>>();
		ArrayList<Integer> path = new ArrayList<Integer>();
		path.add(0);
		
		pathCombinations = findPaths(edges, pathCombinations, new ArrayList<ArrayList<Integer>>(), path, N, 0);
		
		int lastLargestSize = -1;
		int longest = -1;
		
		for (int i = 0; i < pathCombinations.size(); i++) {
			if (pathCombinations.get(i).size() > lastLargestSize) {
				lastLargestSize = pathCombinations.get(i).size();
				longest = i;
			}
		}
		
		try {
			System.out.println("Longest path: ");
			for (int i = 0; i < pathCombinations.get(longest).size(); i++) {
				System.out.println(pathCombinations.get(longest));
				if (i != 0) {
					writer.print("\n");
				}
				for (Integer vertex : pathCombinations.get(longest).get(i)) {
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
	
	public static ArrayList<ArrayList<Integer>> getPath(HashMap<Integer, ArrayList<Integer>> edges, ArrayList<Integer> path, int currentVertex, int lastVertex) {
		HashMap<Integer, ArrayList<Integer>> e = (HashMap<Integer, ArrayList<Integer>>) edges.clone();
		ArrayList<Integer> edge = e.get(currentVertex);
		
		for (int n : edge) {
			path.add(n);
			if (n == lastVertex) {
				return null;
			} else {
				getPath(e, path, n, lastVertex);
			}
			
		}
		return null;
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
