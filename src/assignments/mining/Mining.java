package assignments.mining;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Mining {
	
	public static void main(String[] args) throws FileNotFoundException {
		long startTime = System.currentTimeMillis();
		File file = new File("src/assignments/mining/pit_sample.txt");
		Scanner in = new Scanner(file);
		ArrayList<ArrayList<int[]>> pit = new ArrayList<ArrayList<int[]>>(); 
		int largestRow = 0;
		
		while (in.hasNextLine()) {
			String[] line = in.nextLine().split("\\),\\(|\\)|\\(");
			ArrayList<int[]> p = new ArrayList<int[]>();
			for (int i = 1; i < line.length; i++) {
				String[] temp = line[i].split(",");
				int[] a = {Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2])};
				p.add(a);
			}
			if (line.length > largestRow) {
				largestRow = line.length;
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

		int profit = 0;
		boolean[][] bestMarked = new boolean[pit.size()][largestRow - 1];
		
		// Real method
		ArrayList<int[]> positives = new ArrayList<int[]>();
		for (int i = 0; i < pit.size(); i++) {
			for (int j = 0; j < pit.get(i).size(); j++) {
				if (getPitValue(i, j, pit) > 0) {
					int[] pair = {i,j};
					positives.add(pair);
				}
			}
		}
		
		int[][] flow = new int[pit.size()][largestRow - 1];
		for (int[] pair : positives) {
			System.out.println(pair[0] + "," + pair[1]);
			flow[pair[0]][pair[1]] = flow(pair[0], pair[1], pit, parents, flow, getPitValue(pair[0],pair[1], pit));
		}
		
		for (int i = 0; i < flow.length; i++) {
			for (int j = 0; j < flow[i].length; j++) {
				System.out.print(flow[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		
		for (int[] pair : positives) {
			if (markPit(pair[0], pair[1], pit, parents, bestMarked, flow)) {
				bestMarked[pair[0]][pair[1]] = true;
			}
		}
		

		/*
		int prof = -10000;
		for (int k = 0; k < positives.size(); k++) {
			boolean[][] marked = new boolean[pit.size()][largestRow];
			int[] pair = positives.get(k);
			mine(pair[0], pair[1], pit, parents, marked);
			//System.out.println("profit: " + prof);
			int newProf = getTotalProfit(marked, pit);
			if (newProf > prof) {
				//System.out.println(i + " " + j + " Value: " + getPitValue(i,j,pit) + " Old Profit: " + prof + " New Profit: " + newProf + " Mined: " + mine);
				prof = newProf;
				bestMarked = copy(marked);
			}
			System.out.println(k);
			
			for (int i = k + 1; i < positives.size(); i++) {
				boolean[][] newMarked = copy(marked);
				pair = positives.get(i);
				mine(pair[0], pair[1], pit, parents, newMarked);
				newProf = getTotalProfit(newMarked, pit);
				if (newProf > prof) {
					//System.out.println(i + " " + j + " Value: " + getPitValue(i,j,pit) + " Old Profit: " + prof + " New Profit: " + newProf + " Mined: " + mine);
					prof = newProf;
					bestMarked = copy(newMarked);
				}
				
				//System.out.println(k+" "+i);
				
				for (int j = i + 1; j < positives.size(); j++) {
					boolean[][] newMarkedd = copy(newMarked);
					pair = positives.get(j);
					mine(pair[0], pair[1], pit, parents, newMarkedd);
					newProf = getTotalProfit(newMarkedd, pit);
					if (newProf > prof) {
						//System.out.println(i + " " + j + " Value: " + getPitValue(i,j,pit) + " Old Profit: " + prof + " New Profit: " + newProf + " Mined: " + mine);
						prof = newProf;
						bestMarked = copy(newMarkedd);
					}
					//System.out.println(k+" "+i+" "+j);
				}
			}
		}
		
		
		
		//for (int i = pit.size() - 1; i > 0; i--) {
		for (int i = 0; i < pit.size(); i++) {
			prof = -10000;	
			for (int j = 0; j < pit.get(i).size(); j++) {
			//for (int j = pit.get(i).size() - 1; j > 0; j--) {
				boolean[][] marked = copy(bestMarked);
				//System.out.println(getPitValue(i,j,pit));
				if (getPitValue(i, j, pit) > 0) {
					int mine = mine(i, j, pit, parents, marked);
					int newProf = getTotalProfit(marked, pit);
					if (newProf > prof) {
						//System.out.println(i + " " + j + " Value: " + getPitValue(i,j,pit) + " Old Profit: " + prof + " New Profit: " + newProf + " Mined: " + mine);
						prof = newProf;
						secondMethod = copy(marked);
					}
				}
			}
			System.out.println();
		}
		*/
		
		int profitOne = getTotalProfit(bestMarked, pit);
		
		System.out.println("Total Profit: " + profitOne);
		
		PrintWriter writer = new PrintWriter("src/assignments/mining/blocks.txt");
		for (int i = 0; i < bestMarked.length; i++) {
			ArrayList<Integer> valid = new ArrayList<Integer>();
			for (int j = 0; j < bestMarked[i].length; j++) {
				if (bestMarked[i][j] == true) {
					valid.add(j);
				}
			}
			for (int j = 0; j < valid.size(); j++) {
				if (j != 0) {
					System.out.print(",");
					writer.print(",");
				}
				System.out.print(valid.get(j));
				writer.print(valid.get(j));
			}
			System.out.println();
			writer.println();
		}
		
		writer.close();
		
		in.close();
		
		long now = System.currentTimeMillis() - startTime;
		long minutes = now / 36000;
		long seconds = (long) ((now / 1000) % 60);
		long ms = now % 1000;
		System.out.println("\nTotal run time: " + minutes + "m " + seconds + "s " + ms + "ms");
	}
	
	public static boolean[][] copy(boolean[][] array) {
		boolean[][] result = new boolean[array.length][array[0].length];
		
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				result[i][j] = array[i][j];
			}
		}
		
		return result;
	}
	
	public static int getPitValue(int row, int col, ArrayList<ArrayList<int[]>> pit) {
		int[] block = pit.get(row).get(col);
		return block[1] - block[2];
	}
	
	
	public static boolean markPit(int row, int col, ArrayList<ArrayList<int[]>> pit, ArrayList<ArrayList<ArrayList<Integer>>> parents, boolean[][] marked, int[][] flow) {
		if (marked[row][col] == true) {
			return true;
		}
		int value = getPitValue(row, col, pit);
		if ((value <= 0) && (Math.abs(value) == flow[row][col])) {
			marked[row][col] = true;
		} else if ((value <= 0) && (Math.abs(value) != flow[row][col])) {
			return false;
		}
		if (row == 0) {
			if (getPitValue(row, col, pit) > 0) {
				marked[row][col] = true;
			}
			return true;
		}
		ArrayList<Integer> p = parents.get(row - 1).get(col);
		for (int i = 0; i < p.size(); i++) {
			if (!markPit(row - 1, p.get(i), pit, parents, marked, flow)) {
				return false;
			}
			//System.out.println("Parent Profit: " + profit);
		}
		
		return true;
	}
	
	public static int mine(int row, int col, ArrayList<ArrayList<int[]>> pit, ArrayList<ArrayList<ArrayList<Integer>>> parents, boolean[][] marked) {
		if (marked[row][col] == true) {
			return 0;
		}
		int profit = 0;
		int[] block = pit.get(row).get(col);
		profit += block[1];
		profit -= block[2];
		//System.out.println("Profit: " + profit);
		marked[row][col] = true;
		
		if (row == 0) {
			return profit;
		}
		ArrayList<Integer> p = parents.get(row - 1).get(col);
		for (int i = 0; i < p.size(); i++) {
			profit += mine(row - 1, p.get(i), pit, parents, marked);
			//System.out.println("Parent Profit: " + profit);
		}
		
		//System.out.println("Total Profit: " + profit);
		return profit;
	}
	
	public static int flow(int row, int col, ArrayList<ArrayList<int[]>> pit, ArrayList<ArrayList<ArrayList<Integer>>> parents, int[][] flow, int capacity) {
		if (flow[row][col] == Math.abs(getPitValue(row, col, pit)) || (getPitValue(row, col, pit) > 0 && row == 0)) {
			return 0;
		} else if (row == 0) {
			return capacity;
		}

		ArrayList<Integer> p = parents.get(row - 1).get(col);
		int currentFlow = 0;
		for (int i = 0; i < p.size(); i++) {
			int parentRow = row - 1;
			int parentColumn = p.get(i);
			int parentCapacity = getPitValue(parentRow, parentColumn, pit);
			
			if (parentCapacity > 0) {
				continue;
			}
			
			if (flow[parentRow][parentColumn] < Math.abs(parentCapacity) || currentFlow < capacity) {
				int parentFlow = flow(parentRow, parentColumn, pit, parents, flow, Math.abs(parentCapacity));
				if (capacity <= parentFlow + currentFlow || capacity <= Math.abs(parentCapacity)) {
					flow[parentRow][parentColumn] = capacity;
					return capacity;
				} /*else if (parentFlow == 0) {
					flow[parentRow][parentColumn] += Math.abs(parentCapacity);
					currentFlow += Math.abs(parentCapacity);
				} */else {
					flow[parentRow][parentColumn] += parentFlow;
					currentFlow += parentFlow;
				}
			}
		}
		
		return currentFlow;
	}
	
	public static int getTotalProfit(boolean[][] mined, ArrayList<ArrayList<int[]>> pit) {
		int profit = 0;
		
		for (int i = 0; i < mined.length; i++) {
			for (int j = 0; j < mined[i].length; j++) {
				if (mined[i][j] == true) {
					int[] block = pit.get(i).get(j);
					profit += (block[1] - block[2]);
				}
			}
		}
		
		return profit;
	}

}
