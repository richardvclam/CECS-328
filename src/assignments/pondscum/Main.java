package assignments.pondscum;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		String[][] inputData = readFile("C:/Users/Richard/workspace/CECS 328/src/assignments/pondscum/samp.txt");
		double[] output;
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
		
		output = lsolve(matrix, constants);
		int currentOutput = 0;
		
		for (int i = 0; i < outputPond.length; i++) {
			for (int j = 0; j < outputPond[i].length; j++) {
				if (outputPond[i][j].charAt(0) == '!') {
					outputPond[i][j] = String.valueOf(output[currentOutput]);
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
	
	private static double calculateHeight(int row, int col, String[][] data) {
		String topString = data[row - 1][col];
		String leftString = data[row][col - 1];
		String rightString = data[row][col + 1];
		String bottomString = data[row + 1][col];
		
		double top = 0, left = 0, right = 0, bottom = 0;
		
		if (topString.charAt(0) != '!') {
			top = Double.parseDouble(topString);
		}
		
		if (leftString.charAt(0) != '!') {
			left = Double.parseDouble(leftString);
		}
		
		if (rightString.charAt(0) != '!') {
			right = Double.parseDouble(rightString);
		}
		
		if (bottomString.charAt(0) != '!') {
			bottom = Double.parseDouble(bottomString);
		}
		
		
		if (topString.charAt(0) == '!') {
			top = (left + right + bottom + 4 * (Double.parseDouble(data[row-1][col]) + Double.parseDouble(data[col-1][row-1]) + Double.parseDouble(data[col+1][row-1]))) / 15;
		}
		
		if (leftString.charAt(0) == '!') {
			//left = (top + right + bottom + 4 * (data[row-1][col-1].charAt(0) == '!' ? calculateHeight(row - 1, col - 1, data) : Double.parseDouble(data[row-1][col-1]) + 
			//		data[row][col-2].charAt(0) == '!' ? calculateHeight(row, col - 2, data) : Double.parseDouble(data[row][col-2]) + 
			//	    data[row+1][col-1].charAt(0) == '!' ? calculateHeight(row+1, col-1, data) : Double.parseDouble(data[row+1][col-1]))) / 15;
			left = (top + right + bottom + 4 * (Double.parseDouble(data[row-1][col-1]) + Double.parseDouble(data[row][col-2]) + Double.parseDouble(data[row+1][col-1]))) / 15;	    
		}
		
		if (rightString.charAt(0) == '!') {
			right = (top + left + bottom + 4 * ((data[row-1][col+1].charAt(0) == '!' ? calculateHeight(row, col+1, data) : Double.parseDouble(data[row-1][col+1])) + 
					(data[row][col+2].charAt(0) == '!' ? calculateHeight(row, col+1, data) : Double.parseDouble(data[row][col+2])) + 
					(data[row+1][col+1].charAt(0) == '!' ? calculateHeight(row, col+1, data) : Double.parseDouble(data[row+1][col+1])))) / 15;
		}
		
		if (bottomString.charAt(0) == '!') {
			bottom = (top + left + right + 4 * ((data[row+1][col-1].charAt(0) == '!' ? calculateHeight(row+1, col, data) : Double.parseDouble(data[row+1][col-1])) + 
					(data[row+1][col+1].charAt(0) == '!' ? calculateHeight(row+1, col, data) : Double.parseDouble(data[row+1][col+1])) + 
					(data[row+2][col].charAt(0) == '!' ? calculateHeight(row+1, col, data) : Double.parseDouble(data[row+2][col])))) / 15;
		}
		
		return (top + left + right + bottom) / 4;
	}
	
	private static final double EPSILON = 1e-10;

    // Gaussian elimination with partial pivoting
    public static double[] lsolve(double[][] A, double[] b) {
        int N  = b.length;

        for (int p = 0; p < N; p++) {

            // find pivot row and swap
            int max = p;
            for (int i = p + 1; i < N; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            double[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            double   t    = b[p]; b[p] = b[max]; b[max] = t;

            // singular or nearly singular
            if (Math.abs(A[p][p]) <= EPSILON) {
                throw new RuntimeException("Matrix is singular or nearly singular");
            }

            // pivot within A and b
            for (int i = p + 1; i < N; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < N; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        // back substitution
        double[] x = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
    }

}
