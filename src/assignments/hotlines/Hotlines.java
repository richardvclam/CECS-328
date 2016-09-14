package assignments.hotlines;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Hotlines {
	
	public static void main(String[] args) throws FileNotFoundException {
		long startTime = System.currentTimeMillis();
		File file = new File("C:/Users/Richard/workspace/CECS 328/src/assignments/hotlines/edges.txt");
		Scanner in = new Scanner(file);
		
		int N, num = 0;
		HashMap<Integer, ArrayList<Integer>> edges = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> path = new ArrayList<Integer>();
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
			}
			num++;
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
		
		System.out.println("Paths: " + paths);
		System.out.println("Longest path: " + paths.get(largestPathNum));
		
		PrintWriter writer = new PrintWriter("src/assignments/hotlines/paths.txt");
		
		for (int i = 0; i < paths.get(largestPathNum).size(); i++) {
			if (i != 0) {
				writer.print(",");
			}
			writer.print(paths.get(largestPathNum).get(i));
		}
		
		writer.close();
		in.close();
		
		long now = System.currentTimeMillis() - startTime;
		long minutes = now / 36000;
		long seconds = (long) ((now / 1000) % 60);
		long ms = now % 1000;
		System.out.println("\nTotal run time: " + minutes + "m " + seconds + "s " + ms + "ms");
	}
	
	public static ArrayList<ArrayList<Integer>> findPath(HashMap<Integer, ArrayList<Integer>> edges, ArrayList<ArrayList<Integer>> paths, ArrayList<Integer> path, int lastVertex, int currentVertex) {
		ArrayList<Integer> edge = edges.get(currentVertex);
		int num = 0;
		
		if (edge != null) {
			 num = edge.size();
		} else if (num == 0 || edge == null) {
			paths.add(path);
		}
		
		for (int i = 0; i < num; i++) {
			int vertex = edge.get(i);
			ArrayList<Integer> newPath = (ArrayList<Integer>) path.clone();
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
