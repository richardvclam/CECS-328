package assignments.mining;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Mining {
	
	public static void main(String[] args) throws FileNotFoundException {
		long startTime = System.currentTimeMillis();
		File file = new File("src/assignments/mining/pit.txt");
		Scanner in = new Scanner(file);
		ArrayList<ArrayList<int[]>> pit = new ArrayList<ArrayList<int[]>>(); 
		
		while (in.hasNextLine()) {
			String[] line = in.nextLine().split("\\),\\(|\\)|\\(");
			ArrayList<int[]> p = new ArrayList<int[]>();
			for (int i = 1; i < line.length; i++) {
				String[] temp = line[i].split(",");
				int[] a = {Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2])};
				p.add(a);
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
		
		
		
		PrintWriter writer = new PrintWriter("src/assignments/mining/blocks.txt");
		
		writer.close();
		in.close();
		
		long now = System.currentTimeMillis() - startTime;
		long minutes = now / 36000;
		long seconds = (long) ((now / 1000) % 60);
		long ms = now % 1000;
		System.out.println("\nTotal run time: " + minutes + "m " + seconds + "s " + ms + "ms");
	}

}
