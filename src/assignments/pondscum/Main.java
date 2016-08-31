package assignments.pondscum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		//String[][] inputData = readFile("/Users/rvclam/Documents/workspace/CECS-328/src/assignments/pondscum/sample.txt"); 
		String[][] inputData = readFile("C:/Users/Richard/workspace/CECS 328/src/assignments/pondscum/ponds.txt");
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
					if (top == 0) {
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
					if (left == 0) {
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
					if (right == 0) {
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
					if (bottom == 0) {
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
		System.out.println("Constants");
		for (int i = 0; i < constants.length; i++) {
			if (i != 0) {
				System.out.print(",");
			}
			System.out.print(constants[i]);	
		}
		System.out.println("\n");
		
		output = gaussian(matrix, constants);
		//output = Matrix.cramers(matrix, constants);
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
			return 0;
		}
	}

    public static BigFraction[] gaussian(long[][] coefficientMatrix, long[] constants) {
        int n = constants.length;
        
        BigFraction[][] coeff = new BigFraction[n][n];
        BigFraction[] consts = new BigFraction[n]; 
        
        for (int i = 0; i < n; i++) {
        	consts[i] = new BigFraction(new BigInteger("" + constants[i]));
        }
        for (int i = 0; i < n; i++) {
        	for (int j = 0; j < n; j++) {
        		coeff[i][j] = new BigFraction(new BigInteger("" + coefficientMatrix[i][j]));
        	}
        }

        for (int k = 0; k < n; k++) {
            int max = k;
            for (int i = k + 1; i < n; i++) {
            	if (coeff[i][k].abs().compareTo(coeff[max][k].abs()) == 1) {
            		max = i;
            	}
            }
            
            BigFraction[] temp = coeff[k]; 
            coeff[k] = coeff[max]; 
            coeff[max] = temp;
            
            BigFraction t = consts[k]; 
            consts[k] = consts[max]; 
            consts[max] = t;

            for (int i = k + 1; i < n; i++) {
                BigFraction f = coeff[i][k].divide(coeff[k][k]);
                consts[i] = consts[i].subtract(f.multiply(consts[k]));
                for (int j = k; j < n; j++) {
                	coeff[i][j] = coeff[i][j].subtract(f.multiply(coeff[k][j]));
                }
            }
        }

        BigFraction[] result = new BigFraction[n];
        for (int i = n - 1; i >= 0; i--) {
        	BigFraction sum = new BigFraction(BigInteger.ZERO);
        	for (int j = i + 1; j < n; j++) {
        		sum = sum.add(coeff[i][j].multiply(result[j]));
        	}
        	result[i] = (consts[i].subtract(sum)).divide(coeff[i][i]);
        }

        return result;
    }

}
