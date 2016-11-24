package assignments.mixtures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class MixturesRedux {
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Scanner in = null;
		try {
			File file = new File("src/assignments/mixtures/solutions.txt");
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String[] targetString = in.nextLine().split(":");
		int N = targetString.length;
		System.out.println("Number of chemicals: " + N);
		
		ArrayList<BigFraction[]> solutions = new ArrayList<BigFraction[]>();
		
		while (in.hasNextLine()) {
			String[] solString = in.nextLine().split(":");
			BigFraction[] solution = new BigFraction[N];
			for (int i = 0; i < N; i++) {
				solution[i] = new BigFraction(solString[i]);
			}
			solutions.add(solution);
		}
		
		int M = solutions.size();
		System.out.println("Number of mixtures: " + M + "\n");
		
		BigFraction[][] matrix = new BigFraction[N][M + 1];
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (j == matrix[i].length - 1) {
					matrix[i][j] = new BigFraction(targetString[i]);
				} else {
					matrix[i][j] = solutions.get(j)[i];
				}
			}
		}
		
		printMatrix(matrix);
		System.out.println();
		int[] B = new int[N];
		
		matrix = createPhaseOneTableau(matrix, B);
		
		System.out.println();
		printMatrix(matrix);
		System.out.println();
		
		BigFraction[] obj = new BigFraction[N + M];
		
		for (int i = 0; i < obj.length; i++) {
			if (i < M) {
				obj[i] = new BigFraction("0");
			} else {
				obj[i] = new BigFraction("-1");
			}
		}

		while (/*containsArtifical(B, N) ||*/ countNegatives(matrix) != 0) {
			simplex(matrix, B);
			updateZCValue(matrix, B, obj);
			//printMatrix(matrix);		
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("src/assignments/mixtures/answer.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		BigFraction[] answers = new BigFraction[M];
		for (int i = 0; i < B.length; i++) {
			if (B[i] < answers.length) {
				answers[B[i]] = matrix[i][matrix[0].length - 1];
			}
		}
		
		System.out.println();
		
		for (BigFraction f : answers) {
			if (f != null) {
				System.out.println(f.num);
				writer.println(f.num);
			} else {
				System.out.println("0");
				writer.println("0");
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
	
	public static int countNegatives(BigFraction[][] matrix) {
		int count = 0;
		
		for (int i = 0; i < matrix[0].length; i++) {
			if (matrix[matrix.length - 1][i].isNegative()) {
				count++;
			}
		}
		
		return count;
	}
	
	public static void simplex(BigFraction[][] matrix, int[] basis) {
		// Find smallest negative for pivot column
		int pivotColumn = 0;
		BigFraction smallestColumn = new BigFraction("0");
		for (int i = 0; i < matrix[0].length; i++) {
			if (matrix[matrix.length - 1][i].isNegative() && matrix[matrix.length - 1][i].compareTo(smallestColumn) < 0) {
				smallestColumn = matrix[matrix.length - 1][i];
				pivotColumn = i;
			}
		}
		
		// Find smallest row ratio for pivot row
		int pivotRow = 0;
		BigFraction smallestRow = new BigFraction(BigInteger.valueOf(2).pow(200));
		for (int i = 0; i < matrix.length - 1; i++) {
			if (matrix[i][pivotColumn].isNegative()) {
				continue;
			}
			BigFraction ratio = null;
			try {
				ratio = matrix[i][matrix[i].length - 1].divide(matrix[i][pivotColumn]);
			} catch (ArithmeticException e) {
				System.err.println("Dividing " + matrix[i][matrix[i].length - 1] + " by " + matrix[i][pivotColumn]);
				e.printStackTrace();
				continue;
			}
			if (ratio.compareTo(smallestRow) < 0 && !ratio.isNegative()) {
				smallestRow = ratio;
				pivotRow = i;
			}
		}
		
		// Set row leading coeff to 1
		BigFraction key = matrix[pivotRow][pivotColumn];
		for (int j = 0; j < matrix[0].length; j++) {
			matrix[pivotRow][j] = matrix[pivotRow][j].divide(key);
		}
		
		// Clear column
		for (int i = 0; i < matrix.length; i++) {
			if (i == pivotRow) {
				continue;
			}
			
			BigFraction coeff = matrix[i][pivotColumn];
			for (int j = 0; j < matrix[i].length; j++) {				
				matrix[i][j] = matrix[i][j].subtract(matrix[pivotRow][j].multiply(coeff));
			}
		}
		
		System.out.println(basis[pivotRow] + " departs and " + pivotColumn + " enters");
		basis[pivotRow] = pivotColumn;
	}
	
	public static void updateZCValue(BigFraction[][] matrix, int[] basis, BigFraction[] obj) {
		for (int i = 0; i < matrix[0].length - 1; i++) {
			if (matrix[matrix.length - 1][i] == null) {
				matrix[matrix.length - 1][i] = new BigFraction("0");
			}
			
			if (!containsBasis(basis, i)) {
				BigFraction sum = null;
				for (int j = 0; j < basis.length; j++) {
					if (sum == null || sum.num.equals(BigInteger.ZERO)) {
						sum = matrix[j][i].multiply(obj[basis[j]]);
					} else {
						sum.add(matrix[j][i].multiply(obj[basis[j]]));
					}
				}
				matrix[matrix.length-1][i] = sum.subtract(obj[i]);
			} 
		}
	}
	
	public static boolean containsArtifical(int[] basis, int N) {
		for (int i : basis) {
			if (i >= N) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean containsBasis(int[] basis, int n) {
		for (int i : basis) {
			if (i == n) {
				return true;
			}
		}
		return false;
	}
	
	public static BigFraction[][] createPhaseOneTableau(BigFraction[][] matrix, int[] basis) {
		int N = matrix[0].length - 1;
		BigFraction[][] result = new BigFraction[matrix.length + 1][matrix.length + matrix[0].length];
		
		for (int i = 0; i < result.length - 1; i++) {
			for (int j = 0; j < result[i].length; j++) {
				if (result[result.length - 1][j] == null) {
					result[result.length - 1][j] = new BigFraction("0");
				}

				if (j < N) {
					result[i][j] = matrix[i][j];
					result[result.length - 1][j] = result[result.length - 1][j].subtract(result[i][j]);
				} else if (j - (N) == i) {
					result[i][j] = new BigFraction("1");
					if (i < basis.length) {
						basis[i] = j;
					}
				} else if (j == result[0].length - 1) {
					result[i][j] = matrix[i][N];
				} else {
					result[i][j] = new BigFraction("0");
				}				
			}
		}
		
		return result;
	}
	
	public static BigFraction[][] createSimplexTableau(BigFraction[][] matrix) {
		int N = matrix[0].length - 1;
		BigFraction[][] result = new BigFraction[matrix.length][((N) * 2) + 2];
		
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				if (i != result.length - 1) {
					if (j < N) {
						result[i][j] = matrix[i][j];
					} else if (j - (N) == i) {
						result[i][j] = new BigFraction("1");
					} else if (j == result[0].length - 1) {
						result[i][j] = matrix[i][N];
					} else {
						result[i][j] = BigFraction.ZERO;
					}
				} else {
					if (j < N) {
						result[i][j] = matrix[i][j].negate();
					} else if (j == result[0].length - 2) {
						result[i][j] = new BigFraction("1");
					} else {
						result[i][j] = BigFraction.ZERO;
					}
				}
			}
		}
		
		return result;
	}
	
	public static BigFraction[][] transpose(BigFraction[][] matrix) {
		BigFraction[][] result = new BigFraction[matrix[0].length][matrix.length];
		
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = matrix[j][i];
			}
		}
		
		return result;
	}
	
	public static void printMatrix(BigFraction[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (j != 0) {
					System.out.print(", ");
				}
				System.out.print(matrix[i][j]);
			}
			System.out.println();
		}
	}

}
