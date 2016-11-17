package assignments.mixtures;

import java.io.File;
import java.io.FileNotFoundException;
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
		
		String target = in.nextLine();
		
		while (in.hasNextLine()) {
			in.nextLine();
		}
		
		long now = System.currentTimeMillis() - startTime;
		long minutes = now / 36000;
		long seconds = (long) ((now / 1000) % 60);
		long ms = now % 1000;
		System.out.println("\nTotal run time: " + minutes + "m " + seconds + "s " + ms + "ms");
	}

}
