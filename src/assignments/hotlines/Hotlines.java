package assignments.hotlines;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Hotlines {
	
	public static void main(String[] args) throws FileNotFoundException {
		long startTime = System.currentTimeMillis();
		File file = new File("src/assignments/hotlines/edges.txt");
		Scanner in = new Scanner(file);
		
		int N;
		HashMap<Integer, ArrayList<Integer>> edges = new HashMap<Integer, ArrayList<Integer>>();
		LinkedList<LinkedList<Integer>> paths = new LinkedList<LinkedList<Integer>>();
		LinkedList<Integer> path = new LinkedList<Integer>();
		path.add(0);
		
		N = in.nextInt();
		in.nextLine();
		
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
		
		paths = findPath(edges, paths, path, N, 0);
		
		int largestPathNum = -1;
		int largestSize = -1;
		
		for (int i = 0; i < paths.size(); i++) {
			if (paths.get(i).size() > largestSize) {
				largestSize = paths.get(i).size();
				largestPathNum = i;
			}
		}
		
		PrintWriter writer = new PrintWriter("src/assignments/hotlines/paths.txt");
		
		//System.out.println("Paths: " + paths);
		System.out.println("Number of possible paths: " + paths.size());
		
		try {
			System.out.println("Longest path: " + paths.get(largestPathNum));
			
			for (int i = 0; i < paths.get(largestPathNum).size(); i++) {
				if (i != 0) {
					writer.print(",");
				}
				writer.print(paths.get(largestPathNum).get(i));
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
	
	public static LinkedList<LinkedList<Integer>> findPath(HashMap<Integer, ArrayList<Integer>> edges, LinkedList<LinkedList<Integer>> paths, LinkedList<Integer> path, int lastVertex, int currentVertex) {
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
			LinkedList<Integer> newPath = (LinkedList<Integer>) path.clone();
			newPath.add(vertex);
			if (vertex != lastVertex) {
				findPath(edges, paths, newPath, lastVertex, vertex);
			} else if (vertex == lastVertex) {
				paths.add(newPath);
				return paths;
			}
		}

		return paths;
	}

}
