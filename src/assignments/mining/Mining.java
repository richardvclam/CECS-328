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
		File file = new File("src/assignments/mining/pit_example.txt");
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
		boolean[][] bestMarked = new boolean[pit.size()][largestRow];
		int lastRow = pit.size() - 1;
		int numLastCols = pit.get(pit.size() - 1).size();
		/*
		for (int k = 0; k < numLastCols; k++) {
			boolean[][] marked = new boolean[pit.size()][largestRow];
			int newProfit = getProfit(pit.size() - 1, k, pit, parents, marked);
			System.out.println("profit: " + newProfit);
			if (newProfit > profit) {
				profit = newProfit;
				bestMarked = marked;
				System.out.println("New profit: " + profit);
				//System.out.println("There is a better one");
			}
			System.out.println(k);
			boolean[][] newMarked = copy(marked);
			for (int i = k + 1; i < numLastCols; i++) {
				newProfit = getProfit(pit.size() - 1, i, pit, parents, newMarked);
				if (newProfit > profit) {
					profit = newProfit;
					bestMarked = newMarked;
					System.out.println("New profit: " + profit);
					//System.out.println("There is a better one");
				}
				boolean[][] newMarkedd = copy(newMarked);
				System.out.println(k+""+i);
				for (int j = i + 1; j < numLastCols; j++) {
					 newProfit = getProfit(pit.size() - 1, j, pit, parents, newMarkedd);
					if (newProfit > profit) {
						profit = newProfit;
						bestMarked = newMarkedd;
						System.out.println("New profit: " + profit);
						//System.out.println("There is a better one");
					}
					System.out.println(k+""+i+""+j);
				}
			}
		}
		*/
		
		int prof = -10000;	
		//for (int i = pit.size() - 1; i > 0; i--) {
		for (int i = 0; i < pit.size(); i++) {
			
			for (int j = 0; j < pit.get(i).size(); j++) {
				boolean[][] marked = copy(bestMarked);
				if (getPitValue(i, j, pit) > 0) {
					mine(i, j, pit, parents, marked);
					int newProf = getTotalProfit(marked, pit);
					if (newProf > prof) {
						System.out.println("Old Profit: " + prof + " New Profit: " + newProf);
						prof = newProf;
						bestMarked = marked;
					}
				}
			}
		}
		
		System.out.println("Total profit: " + getTotalProfit(bestMarked, pit));
		
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
