package assignments.mixtures;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Mixtures {
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Scanner in = null;
		try {
			File file = new File("src/assignments/mixtures/solutions_sample.txt");
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String[] targetString = in.nextLine().split(",");
		int[] target = new int[targetString.length];
		ArrayList<int[]> solutions = new ArrayList<int[]>();
		
		while (in.hasNextLine()) {
			String[] solString = in.nextLine().split(":");
			int[] solution = new int[solString.length];
			for (int i = 0; i < solString.length; i++) {
				solution[i] = Integer.parseInt(solString[i]);
			}
			solutions.add(solution);
		}
		
		long now = System.currentTimeMillis() - startTime;
		long minutes = now / 36000;
		long seconds = (long) ((now / 1000) % 60);
		long ms = now % 1000;
		System.out.println("\nTotal run time: " + minutes + "m " + seconds + "s " + ms + "ms");
	}

}
