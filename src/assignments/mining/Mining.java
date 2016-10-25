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
