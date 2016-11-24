package assignments.racinggems;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import javafx.scene.shape.Path;

public class Gems {
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Scanner in = null;
		try {
			File file = new File("src/assignments/racinggems/gems_sample2.txt");
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
		ArrayList<ArrayList<Integer>> parents = new ArrayList<ArrayList<Integer>>();
		
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
		
		for (int i = 0; i < gems.size(); i++) {
			double[] gemFrom = gems.get(i);
			ArrayList<Integer> parent = new ArrayList<Integer>();
			for (int j = i + 1; j < gems.size(); j++) {
				double[] gemTo = gems.get(j);
				if (isValidGem(gemFrom[0], gemFrom[1], gemTo[0], gemTo[1], init[1])) {
					parent.add(j);
				}
				
				//System.out.println("From: " + gemFrom[0] + "," + gemFrom[1] + " To: " + gemTo[0] + "," + gemTo[1] + " " + isValidGem(gemFrom[0], gemFrom[1], gemTo[0], gemTo[1], init[1]));
			}
			parents.add(parent);
		}
		
		/*
		ArrayList<ArrayList<Integer>> incomings = new ArrayList<ArrayList<Integer>>();
		
		for (int i = 0; i < gems.size(); i++) {
			double[] gemFrom = gems.get(i);
			ArrayList<Integer> parent = new ArrayList<Integer>();
			for (int j = 0; j < gems.size(); j++) {
				double[] gemTo = gems.get(j);
				if (i == j || gemFrom[1] <= gemTo[1]) {
					continue;
				}
				if (isValidGem(gemFrom[0], gemFrom[1], gemTo[0], gemTo[1], init[1])) {
					parent.add(j);
				}
				
				//System.out.println("From: " + gemFrom[0] + "," + gemFrom[1] + " To: " + gemTo[0] + "," + gemTo[1] + " " + isValidGem(gemFrom[0], gemFrom[1], gemTo[0], gemTo[1], init[1]));
			}
			incomings.add(parent);
		}
		*/
		
		System.out.println("Created graph in " + (System.currentTimeMillis() - startTime) + " ms");
		
		int[] paths = new int[gems.size()];
		for (int i = 0; i < paths.length; i++) {
			paths[i] = Integer.MAX_VALUE;
		}
		
		ArrayList<Integer> bestPath = new ArrayList<Integer>();
		
		int profit = 0;
		for (int i = gems.size() - 1; i > 0; i--) {
			//bestPath = findBestPath(gems, parents, new ArrayList<Integer>(), bestPath, i, profit);
			int bestParentIndex = 0;
			double highest = 0;
			for (int j = 0; j < parents.get(i).size(); j++) {
				if (gems.get(parents.get(i).get(j))[2] > highest) {
					highest = gems.get(parents.get(i).get(j))[2];
					bestParentIndex = parents.get(i).get(j);
				}
			}
			
			if (parents.get(i).isEmpty()) {
				paths[i] = -1;
			} else {
				paths[i] = bestParentIndex;
			}
			
		}
		
		System.out.println(bestPath);
		System.out.println("Profit: " + calculateProfit(bestPath, gems));
		
		for (int i = 0; i < paths.length; i++) {
			if (paths[i] == 0) {
				continue;
			}
			if (paths[i] == -1) {
				break;
			}
			double[] gem = gems.get(paths[i]);
			System.out.println(gem[0] + "," + gem[1] + "," + (int) gem[2]);
			i = paths[i] - 1;
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("src/assignments/racinggems/race.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		/*
		for (int i = 0; i < bestPath.size(); i++) {
			double[] gem = gems.get(bestPath.get(i));
			System.out.println(gem[0] + "," + gem[1] + "," + (int) gem[2]);
			writer.println(gem[0] + "," + gem[1] + "," + (int) gem[2]);
		}
		*/
		
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
	
	public static ArrayList<Integer> findBestPath(ArrayList<double[]> gems, ArrayList<ArrayList<Integer>> parents, ArrayList<Integer> stack, ArrayList<Integer> bestPath, int current, int profit) {
		if (parents.get(current).isEmpty()) {
			stack.add(current);
			int newProfit = calculateProfit(stack, gems);
			profit = calculateProfit(bestPath, gems);
			if (newProfit > profit) {
				
				bestPath = (ArrayList<Integer>) stack.clone();
				profit = newProfit;
				//System.out.println("Best path: " + bestPath);
				//System.out.println("Profit: " + profit);
			}
			stack.remove(stack.size() - 1);
			return bestPath;
		}
		stack.add(current);
		
		for (int i = 0; i < parents.get(current).size(); i++) {
			bestPath = findBestPath(gems, parents, stack, bestPath, parents.get(current).get(i), profit);
		}
		
		stack.remove(stack.size() - 1);
		
		return bestPath;
	}
	
	public static int calculateProfit(ArrayList<Integer> path, ArrayList<double[]> gems) {
		int profit = 0;
		for (Integer i : path) {
			profit += gems.get(i)[2];
		}
		
		return profit;
	}

}
