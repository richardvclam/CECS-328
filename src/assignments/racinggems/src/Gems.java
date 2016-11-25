package assignments.racinggems;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Gems {
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Scanner in = null;
		try {
			File file = new File("src/assignments/racinggems/gems.txt");
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String[] initString = in.nextLine().split(",");
		double[] init = new double[initString.length];
		for (int i = 0; i < init.length; i++) {
			init[i] = Double.parseDouble(initString[i]);
		}
		
		System.out.println("Number of gems: " + init[0]);
		ArrayList<double[]> gems = new ArrayList<double[]>();
		
		for (int i = 0; i < init[0]; i++) {
			String[] gemString = in.nextLine().split(",");
			double[] gem = {Double.parseDouble(gemString[0]), Double.parseDouble(gemString[1]), Double.parseDouble(gemString[2])};
			gems.add(gem);
		}
		
		Collections.sort(gems, new Comparator<double[]>() {
			@Override
			public int compare(double[] o1, double[] o2) {
				double val = o1[1] - o2[1];
				if (val < 0) {
					return -1;
				} else if (val == 0) {
					return 0;
				} else {
					return 1;
				} 
			}
			
		});
		
		ArrayList<ArrayList<Integer>> neighbors = new ArrayList<ArrayList<Integer>>();
		
		for (int i = 0; i < gems.size(); i++) {
			double[] gemFrom = gems.get(i);
			ArrayList<Integer> parent = new ArrayList<Integer>();
			for (int j = 0; j < i; j++) {
				double[] gemTo = gems.get(j);
				/*
				if (i == j || gemFrom[1] <= gemTo[1]) {
					continue;
				}
				*/
				if (isValidGem(gemFrom[0], gemFrom[1], gemTo[0], gemTo[1], init[1])) {
					parent.add(j);
				}
			}
			neighbors.add(parent);
		}
		
		
		System.out.println("Created graph in " + (System.currentTimeMillis() - startTime) + " ms");
		
		double[] distances = new double[gems.size()];
		
		for (int i = 0; i < gems.size(); i++) {
			double largestSize = 0;
			for (int j = 0; j < neighbors.get(i).size(); j++) {
				if (distances[neighbors.get(i).get(j)] > largestSize) {
					largestSize = distances[neighbors.get(i).get(j)];
				}
			}
			distances[i] = largestSize + gems.get(i)[2];
		}
		
		ArrayList<Integer> bestPath = new ArrayList<Integer>();
		
		double highest = 0;
		int highestIndex = 0;
		for (int i = 0; i < distances.length; i++) {
			if (distances[i] > highest) {
				highest = distances[i];
				highestIndex = i;
			}
		}
		
		int index = highestIndex;
		boolean run = true;
		while (run) {
			bestPath.add(index);
			highest = 0;
			highestIndex = 0;
			for (int i = 0; i < neighbors.get(index).size(); i++) {
				if (distances[neighbors.get(index).get(i)] > highest) {
					highest = distances[neighbors.get(index).get(i)];
					highestIndex = neighbors.get(index).get(i);
				}
			}
			if (neighbors.get(index).isEmpty()) {
				run = false;
			}
			index = highestIndex;
		}
		
		Collections.reverse(bestPath);
		
		System.out.println(bestPath);
		System.out.println("Profit: " + calculateProfit(bestPath, gems));
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("src/assignments/racinggems/race.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < bestPath.size(); i++) {
			double[] gem = gems.get(bestPath.get(i));
			System.out.println(gem[0] + "," + gem[1] + "," + (int) gem[2]);
			writer.println(gem[0] + "," + gem[1] + "," + (int) gem[2]);
		}
		
		writer.close();
		in.close();
		
		long now = System.currentTimeMillis() - startTime;
		long minutes = now / 36000;
		long seconds = (long) ((now / 1000) % 60);
		long ms = now % 1000;
		System.out.println("\nTotal run time: " + minutes + "m " + seconds + "s " + ms + "ms");
	}
	
	public static boolean isValidGem(double xFrom, double yFrom, double xTo, double yTo, double r) {
		double xVelocity = 1/r;
		double deltaY = Math.abs(yTo - yFrom);
		double deltaX = Math.abs(xTo - xFrom);
		double xMaxTravel = xVelocity * deltaY;
		if (xMaxTravel >= deltaX) {
			return true;
		}
		return false;
	}
	
	public static int calculateProfit(ArrayList<Integer> path, ArrayList<double[]> gems) {
		int profit = 0;
		for (Integer i : path) {
			profit += gems.get(i)[2];
		}
		
		return profit;
	}

}
