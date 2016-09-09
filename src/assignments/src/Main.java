import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		long startTime = System.currentTimeMillis();
		//String[][] inputData = readFile("/Users/rvclam/Documents/workspace/CECS-328/src/assignments/pondscum/ponds.txt"); 
		String[][] inputData = readFile("C:/Users/Richard/workspace/CECS 328/src/assignments/pondscum/pond.txt");
		BigFraction[] output;
		String[][] outputPond = Arrays.copyOf(inputData, inputData.length);
		int numPond = 0;
		long[][] matrix;
		long[] constants;
		HashMap<Integer, int[]> ponds = new HashMap<Integer, int[]>();
		
		System.out.println("Starting Input");
		for (int i = 0; i < inputData.length; i++) {
			for (int j = 0; j < inputData[i].length; j++) {
				if (j != 0) {
					System.out.print(",");
				}
				System.out.print(inputData[i][j]);	
			}
			System.out.println();
		}
		
		for (int i = 0; i < inputData.length; i++) {
			for (int j = 0; j < inputData[i].length; j++) {
				if (inputData[i][j].charAt(0) == '!') {
					int[] temp = {i,j};
					ponds.put(numPond, temp); // Save current position of variable pond
					numPond++;
				}
			}
		}
		
		System.out.println("Number of variable ponds: " + numPond);
		matrix = new long[numPond][numPond];
		constants = new long[numPond];
		
		numPond = 0;
		
		for (int i = 0; i < inputData.length; i++) {
			for (int j = 0; j < inputData[i].length; j++) {
				if (inputData[i][j].charAt(0) == '!') {
					int pondKey = 0;
					
					int top = getHeight(i-1, j, inputData);
					for (Entry<Integer, int[]> pond : ponds.entrySet()) {
						if (pond.getValue()[0] == i-1 && pond.getValue()[1] == j) {
							pondKey = pond.getKey();
						}
					}
					if (top == -1) {
						matrix[numPond][pondKey] = -1;
					} else {
						constants[numPond] += top;
					}
					
					int left = getHeight(i, j-1, inputData);
					for (Entry<Integer, int[]> pond : ponds.entrySet()) {
						if (pond.getValue()[0] == i && pond.getValue()[1] == j-1) {
							pondKey = pond.getKey();
						}
					}
					if (left == -1) {
						matrix[numPond][pondKey] = -1;
					} else {
						constants[numPond] += left;
					}
					
					int right = getHeight(i, j+1, inputData);
					for (Entry<Integer, int[]> pond : ponds.entrySet()) {
						if (pond.getValue()[0] == i && pond.getValue()[1] == j+1) {
							pondKey = pond.getKey();
						}
					}
					if (right == -1) {
						matrix[numPond][pondKey] = -1;
					} else {
						constants[numPond] += right;
					}
					
					int bottom = getHeight(i+1, j, inputData);
					for (Entry<Integer, int[]> pond : ponds.entrySet()) {
						if (pond.getValue()[0] == i+1 && pond.getValue()[1] == j) {
							pondKey = pond.getKey();
						}
					}
					if (bottom == -1) {
						matrix[numPond][pondKey] = -1;
					} else {
						constants[numPond] += bottom;
					}
					
					for (Entry<Integer, int[]> pond : ponds.entrySet()) {
						if (pond.getValue()[0] == i && pond.getValue()[1] == j) {
							matrix[numPond][pond.getKey()] = 4;
						}
					}
					numPond++;
				}
			}
		}
		
		System.out.println();
		/*
		System.out.println("Matrix");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (j != 0) {
					System.out.print(",");
				}
				System.out.print(matrix[i][j]);	
			}
			System.out.println();
		}
		*/
		System.out.println("Constants");
		for (int i = 0; i < constants.length; i++) {
			if (i != 0) {
				System.out.print(",");
			}
			System.out.print(constants[i]);	
		}
		System.out.println("\n");
		
		output = cramers(matrix, constants);
		int currentOutput = 0;
		
		for (int i = 0; i < outputPond.length; i++) {
			for (int j = 0; j < outputPond[i].length; j++) {
				if (outputPond[i][j].charAt(0) == '!') {
					outputPond[i][j] = String.valueOf(output[currentOutput]);
					currentOutput++;
				}
			}
		}
		
		PrintWriter writer = new PrintWriter("src/assignments/pondscum/heights.txt");
		
		System.out.println("Ending Output");
		for (int i = 0; i < outputPond.length; i++) {
			for (int j = 0; j < outputPond[i].length; j++) {
				if (j != 0) {
					System.out.print(",");
					writer.print(",");
				}
				System.out.print(outputPond[i][j]);	
				writer.print(outputPond[i][j]);
			}
			System.out.println();
			writer.println();
		}
		
		writer.close();
		
		long now = System.currentTimeMillis() - startTime;
		long minutes = now / 36000;
		long seconds = now / 1000;
		long ms = now % 1000;
		System.out.println("\nTotal run time: " + minutes + "m " + seconds + "s " + ms + "ms");
	}
	
	private static String[][] readFile(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		Scanner in = new Scanner(file);
		ArrayList<String> buffer = new ArrayList<String>();
		String[][] array;
		
		while (in.hasNextLine()) {
			buffer.add(in.nextLine());
		}
		
		array = new String[buffer.size()][];
		
		for (int i = 0; i < buffer.size(); i++) {
			array[i] = buffer.get(i).split(",");
		}
		
		in.close();
		
		return array;
	}
	
	private static int getHeight(int row, int col, String[][] data) {
		String pond = data[row][col];
		
		if (pond.charAt(0) != '!') {
			return Integer.parseInt(pond);
		} else {
			return -1;
		}
	}
	
	public static BigFraction determinantFromUpperTriangular(long[][] matrix) {
		int n = matrix.length;
		
		BigFraction[][] result = new BigFraction[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = new BigFraction(new BigInteger(""+matrix[i][j]));
			}
		}
		
		BigFraction neg = new BigFraction(BigInteger.ONE);
		
		for (int k = 0; k < n; k++) {
            int max = k;
            for (int i = k + 1; i < n; i++) {
            	if (result[i][k].abs().compareTo(result[max][k].abs()) == 1) {
            		max = i;
            	}
            }
            
            if (max != k) {
	            BigFraction[] temp = result[k]; 
	            result[k] = result[max]; 
	            result[max] = temp;
	            neg = neg.multiply(new BigFraction(new BigInteger("-1")));
            }

            for (int i = k + 1; i < n; i++) {
                BigFraction f = result[i][k].divide(result[k][k]);
                for (int j = k; j < n; j++) {
                	result[i][j] = result[i][j].subtract(f.multiply(result[k][j]));
                }
            }
        }
		
		BigFraction det = new BigFraction(BigInteger.ONE);
		for (int i = 0; i < n; i++) {
			det = det.multiply(result[i][i]);
		}

		return det.multiply(neg);
	}
	
	public static BigFraction[] cramers(long[][] matrix, long[] constants) {
		int n = constants.length;
		BigFraction[] result = new BigFraction[n];
		
		BigFraction D = determinantFromUpperTriangular(matrix);
		//System.out.println("D:" + D);
		
		for (int k = 0; k < n; k++) {
			long[][] temp = new long[n][n];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					temp[i][j] = matrix[i][j];
				}
				temp[i][k] = constants[i];
			}
			BigFraction t = determinantFromUpperTriangular(temp);
			result[k] = t.divide(D);
			//System.out.println("D[" + k + "] " + t);
			System.out.println(k+1 + "/" + n + " Completed");
		}
		
		return result;
	}

}
