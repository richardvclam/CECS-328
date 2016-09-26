package assignments.hotlines;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Hotlines {
	
	public static void main(String[] args) throws FileNotFoundException {
		long startTime = System.currentTimeMillis();
		File file = new File("src/assignments/hotlines/edges.txt");
		Scanner in = new Scanner(file);
		
		int N = in.nextInt();
		in.nextLine();
		
		HashMap<Integer, ArrayList<Integer>> edges = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>(8192);
		ArrayList<Integer> path = new ArrayList<Integer>(N);
		path.add(0);
		
		
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
		
		findPath(edges, paths, path, N, 0);
		
		PrintWriter writer = new PrintWriter("src/assignments/hotlines/paths.txt");
		
		System.out.println("Paths: " + paths);
		System.out.println("Number of possible paths: " + paths.size());
		
		ArrayList<ArrayList<Integer>> combinations = new ArrayList<ArrayList<Integer>>();
		
		for (int i = 0; i < paths.size(); i++) {
			ArrayList<Integer> possibleCombination = new ArrayList<Integer>();
			if (possibleCombination.isEmpty()) {
				possibleCombination.add(i);
			}
			
			for (int j = 0; j < paths.size(); j++) {
				boolean unique = true;
				for (Integer element : paths.get(j)) {
					if (element == 0 || element == N) {
						continue;
					}
					if (!unique) {
						break;
					}
					for (int l = 0; l < possibleCombination.size(); l++) {
						if (paths.get(possibleCombination.get(l)).contains(element)) {
							unique = false;
							break;
						}
					}
				}
				
				if (unique) {
					possibleCombination.add(j);
				}
			}
			combinations.add(possibleCombination);
			//System.out.println(i);
		}
		
		int lastLargestSize = -1;
		int longest = -1;
		
		for (int i = 0; i < combinations.size(); i++) {
			if (combinations.get(i).size() > lastLargestSize) {
				lastLargestSize = combinations.get(i).size();
				longest = i;
			}
		}
		
		try {
			System.out.println("Longest path: ");
			for (int i = 0; i < combinations.get(longest).size(); i++) {
				System.out.println(paths.get(combinations.get(longest).get(i)));
				if (i != 0) {
					writer.print("\n");
				}
				for (Integer vertex : paths.get(combinations.get(longest).get(i))) {
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
	
	public static void findPath(HashMap<Integer, ArrayList<Integer>> edges, ArrayList<ArrayList<Integer>> paths, ArrayList<Integer> path, int lastVertex, int currentVertex) {
		if (edges.get(currentVertex) != null) {
			for (int vertex : edges.get(currentVertex)) {
				if (path.contains(vertex)) {
					continue;
				}
				path.add(vertex);
				if (vertex != lastVertex) {
					findPath(edges, paths, path, lastVertex, vertex);
				} else if (vertex == lastVertex) {
					paths.add(new ArrayList<Integer>(path));
					path.remove(path.size() - 1);
					return;
				}
				path.remove(path.size() - 1);
			}
		}
	}

}
