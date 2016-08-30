package assignments.pondscum;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		String[][] inputData = readFile("C:/Users/Richard/workspace/CECS 328/src/assignments/pondscum/ponds.txt");
		BigDecimal[] output;
		String[][] outputPond = Arrays.copyOf(inputData, inputData.length);
		int numPond = 0;
		double[][] matrix;
		double[] constants;
		HashMap<Integer, int[]> ponds = new HashMap<>();
		
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
		
		matrix = new double[numPond][numPond];
		constants = new double[numPond];
		
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
		int currentOutput = 0;
		
		for (int i = 0; i < outputPond.length; i++) {
			for (int j = 0; j < outputPond[i].length; j++) {
				if (outputPond[i][j].charAt(0) == '!') {
					outputPond[i][j] = String.valueOf(new BigFraction(output[currentOutput]));
					currentOutput++;
				}
			}
		}
		
		System.out.println("Ending Output");
		for (int i = 0; i < outputPond.length; i++) {
			for (int j = 0; j < outputPond[i].length; j++) {
				if (j != 0) {
					System.out.print(",");
				}
				System.out.print(outputPond[i][j]);	
			}
			System.out.println();
		}
		
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
	
	private static final double EPSILON = 1e-10;

    // Gaussian elimination with partial pivoting
    public static BigDecimal[] gaussian(double[][] A, double[] b) {
        int N = b.length;
        
        // Create a BigDecimal array for array A and b.
        BigDecimal[][] bd_AArray = new BigDecimal[N][N];
        BigDecimal[] bd_bArray = new BigDecimal[N]; 
        
        // Copy double arrays over to BigDecimal
        for (int i = 0; i < N; i++) {
        	bd_bArray[i] = new BigDecimal("" + b[i]);
        }
        for (int i = 0; i < N; i++) {
        	for (int j = 0; j < N; j++) {
        		bd_AArray[i][j] = new BigDecimal("" + A[i][j]);
        	}
        }

        for (int p = 0; p < N; p++) {

            // Find pivot row and swap
            int max = p;
            for (int i = p + 1; i < N; i++) {
            	if (bd_AArray[i][p].abs().compareTo(bd_AArray[max][p].abs()) == 1) {
            		max = i;
            	}
            }
            
            BigDecimal[] temp = bd_AArray[p]; bd_AArray[p] = bd_AArray[max]; bd_AArray[max] = temp;
            BigDecimal   t    = bd_bArray[p]; bd_bArray[p] = bd_bArray[max]; bd_bArray[max] = t;

            if (bd_AArray[p][p].abs().compareTo(BigDecimal.ZERO) == 2 || bd_AArray[p][p].abs().equals(BigDecimal.ZERO)) {
                throw new RuntimeException("Matrix is singular");
            }

            // Pivot between A and b
            for (int i = p + 1; i < N; i++) {
            	//MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
                BigDecimal alpha = bd_AArray[i][p].divide(bd_AArray[p][p], 100, RoundingMode.HALF_UP);
                bd_bArray[i] = bd_bArray[i].subtract(alpha.multiply(bd_bArray[p]));
                for (int j = p; j < N; j++) {
                	bd_AArray[i][j] = bd_AArray[i][j].subtract(alpha.multiply(bd_AArray[p][j]));
                }
            }
        }

        // Back substitution
        BigDecimal[] x = new BigDecimal[N];
        for (int i = N - 1; i >= 0; i--) {
        	BigDecimal sum = BigDecimal.ZERO;
        	for (int j = i + 1; j < N; j++) {
        		sum = sum.add(bd_AArray[i][j].multiply(x[j]));
        	}
        	x[i] = (bd_bArray[i].subtract(sum)).divide(bd_AArray[i][i], 100, RoundingMode.HALF_UP);
        }

        return x;
    }

}
