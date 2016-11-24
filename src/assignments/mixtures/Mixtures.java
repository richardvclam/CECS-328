package assignments.mixtures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
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
		
		String[] targetString = in.nextLine().split(":");
		int N = targetString.length;
		System.out.println("Number of chemicals: " + N);
		BigFraction[] target = new BigFraction[N];
		
		for (int i = 0; i < N; i++) {
			target[i] = new BigFraction(targetString[i]);
		}
		ArrayList<BigFraction[]> solutions = new ArrayList<BigFraction[]>();
		
		while (in.hasNextLine()) {
			String[] solString = in.nextLine().split(":");
			BigFraction[] solution = new BigFraction[N];
			for (int i = 0; i < N; i++) {
				solution[i] = new BigFraction(solString[i]);
			}
			solutions.add(solution);
		}
		
		System.out.println("Number of mixtures: " + solutions.size() + "\n");
		BigFraction[][] matrix = new BigFraction[N][solutions.size()];
		
		BigInteger[][] test = { {new BigInteger(""+1), new BigInteger(""+3)}, 
								{new BigInteger(""+2), new BigInteger(""+7)}, 
								{new BigInteger(""+3), new BigInteger(""+1)}};
		BigInteger[] cs = {new BigInteger("3"), new BigInteger("4"), new BigInteger("5")};
		
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < solutions.size(); j++) {
				matrix[i][j] = solutions.get(j)[i];
			}
		}
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.print(matrix[i][j] + ",");
			}
			System.out.print(target[i]);
			System.out.println();
		}
		System.out.println();
		for (int i = 0; i < matrix[0].length; i++) {
			BigFraction pivot = matrix[i][i];
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = matrix[i][j].divide(pivot);
				//System.out.println(matrix[i][j]);
			}
			
			target[i] = target[i].divide(pivot);
			//System.out.println(target[i]);
			
			for (int j = 0; j < matrix.length; j++) {
				if (j == i) {
					continue;
				}
				BigFraction elim = BigFraction.ZERO;
				for (int k = i; k < matrix[0].length; k++) {
					if (elim.equals(BigFraction.ZERO)) {
						elim = matrix[j][k];
					}
					matrix[j][k] = matrix[j][k].subtract(matrix[i][k].multiply(elim));
					//System.out.println(matrix[j][k]);
				}
				target[j] = target[j].subtract(target[i].multiply(elim));
				//System.out.println(target[j]);
			}
			//System.out.println();
			
			for (int k = 0; k < matrix.length; k++) {
				for (int j = 0; j < matrix[0].length; j++) {
					System.out.print(matrix[k][j] + ",");
				}
				System.out.print(target[k]);
				System.out.println();
			}
			System.out.println();
		}
		/*
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.print(matrix[i][j] + ",");
			}
			System.out.print(target[i]);
			System.out.println();
		}
		*/
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("src/assignments/mixtures/answer.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//System.out.println();
		for (BigFraction bf : target) {
			if (!bf.equals(BigFraction.ZERO)) {
				System.out.println(bf);
				writer.println(bf);
			} else {
				System.out.println(-1);
				writer.println(-1);
			}
		}
		
		writer.close();
		in.close();
		
		long now = System.currentTimeMillis() - startTime;
		long minutes = now / 36000;
		long seconds = (long) ((now / 1000) % 60);
		long ms = now % 1000;
		System.out.println("\nTotal run time: " + minutes + "m " + seconds + "s " + ms + "ms");
	}

}
