package assignments.mixtures;

import java.math.BigInteger;

public class Simplex {
	
	public static void main(String[] args) {
		/*
		BigFraction[][] matrix = {  {new BigFraction("2"), new BigFraction("3"), new BigFraction("2"), new BigFraction("1000")},
									{new BigFraction("1"), new BigFraction("1"), new BigFraction("2"), new BigFraction("800")},
									{new BigFraction("7"), new BigFraction("8"), new BigFraction("10"), new BigFraction("1")}
				
		};
		*/
		
		BigFraction[][] matrixx = {  {new BigFraction("1"), new BigFraction("3"), new BigFraction("2"), new BigFraction("3")},
									{new BigFraction("2"), new BigFraction("7"), new BigFraction("1"), new BigFraction("4")},
									{new BigFraction("3"), new BigFraction("1"), new BigFraction("2"), new BigFraction("5")}
			
		};
		
		BigFraction[][] matrix = {  {new BigFraction("81"), new BigFraction("48"), new BigFraction("43"), new BigFraction("94"), new BigFraction("10"), new BigFraction("8608")},
									{new BigFraction("1"), new BigFraction("35"), new BigFraction("8"), new BigFraction("37"), new BigFraction("77"), new BigFraction("8730")},
									{new BigFraction("10"), new BigFraction("16"), new BigFraction("55"), new BigFraction("100"), new BigFraction("47"), new BigFraction("11088")},
									{new BigFraction("70"), new BigFraction("89"), new BigFraction("95"), new BigFraction("10"), new BigFraction("4"), new BigFraction("9615")},
									{new BigFraction("46"), new BigFraction("71"), new BigFraction("59"), new BigFraction("83"), new BigFraction("72"), new BigFraction("14379")}
				
		};
		
		BigFraction[][] matrixxx = {  {new BigFraction("1"), new BigFraction("3"), new BigFraction("1"), new BigFraction("5")},
									{new BigFraction("2"), new BigFraction("-1"), new BigFraction("1"), new BigFraction("2")},
									{new BigFraction("4"), new BigFraction("3"), new BigFraction("-2"), new BigFraction("5")},
				
		};
		int N = matrix[0].length - 1;
		int[] B = new int[matrix.length];
		
		//matrix = transpose(matrix);
		
		printMatrix(matrix);
		
		matrix = createPhaseOneTableau(matrix, B);
		
		System.out.println();
		printMatrix(matrix);
		System.out.println();
		int prev = -1;
		
		//BigFraction[] obj = {new BigFraction("0"), new BigFraction("0"), new BigFraction("0"), new BigFraction("-1"), new BigFraction("-1"), new BigFraction("-1")};
		BigFraction[] obj = {new BigFraction("0"), new BigFraction("0"), new BigFraction("0"), new BigFraction("0"), new BigFraction("0"), new BigFraction("-1"), new BigFraction("-1"), new BigFraction("-1"), new BigFraction("-1"), new BigFraction("-1")};
		
		while (containsArtifical(B, N)) {
			prev = simplex(matrix, B, prev);
			updateZCValue(matrix, B, obj);
			printMatrix(matrix);
		}
		/*
		System.out.println();
		printMatrix(matrix);
		
		BigFraction[][] matrix2 = createPhaseTwoTableau(matrix, B);
		System.out.println();
		printMatrix(matrix2);
		
		//BigFraction[] obj2 = {new BigFraction("-1"), new BigFraction("-1"), new BigFraction("-1")};
		BigFraction[] obj2 = {new BigFraction("-1"), new BigFraction("-1"), new BigFraction("-1"), new BigFraction("-1"), new BigFraction("-1")};
		while (countNegatives(matrix) != 0) {
			simplex(matrix2, B);
			updateZCValue(matrix, B, obj2);
			printMatrix(matrix2);
			System.out.println();
		}
		*/
		
		/*
		for (int i = 0; i < matrix.length - 1; i++) {
			System.out.println(matrix[matrix.length - 1][i + matrix.length]);
		}
		*/
		for (int i = 0; i < matrix.length - 1; i++) {
			boolean found = false;
			for (int j = 0; j < matrix[i].length/2; j++) {
				if (matrix[j][i].equals(BigFraction.ONE)) {
					System.out.println(matrix[j][matrix[i].length-1]);
					found = true;
				}
			}
			if (!found) {
				System.out.println("0");
			}
		}
		
		
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
	
	public static int simplex(BigFraction[][] matrix, int[] basis, int prev) {
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
		BigFraction smallestRow = new BigFraction(BigInteger.valueOf(Long.MAX_VALUE));
		for (int i = 0; i < matrix.length - 1; i++) {
			BigFraction ratio = matrix[i][matrix[i].length - 1].divide(matrix[i][pivotColumn]);
			System.out.println(ratio);
			if (ratio.compareTo(smallestRow) < 0 && !ratio.isNegative() && i != prev) {
				smallestRow = ratio;
				pivotRow = i;
			}
		}
		
		// Set row leading coeff to 1
		for (int i = 0; i < matrix.length; i++) {
			BigFraction coeff = matrix[pivotRow][pivotColumn];
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[pivotRow][j] = matrix[pivotRow][j].divide(coeff);
			}
		}
		
		// Clear column
		for (int i = 0; i < matrix.length; i++) {
			if (i == pivotRow) {
				continue;
			}
			
			BigFraction coeff = null;
			for (int j = 0; j < matrix[i].length; j++) {
				if (coeff == null) {
					coeff = matrix[i][pivotColumn];
				}
				
				matrix[i][j] = matrix[i][j].subtract(matrix[pivotRow][j].multiply(coeff));
			}
		}
		
		prev = pivotColumn;
		System.out.println(basis[pivotRow] + " departs and " + pivotColumn + " enters");
		basis[pivotRow] = pivotColumn;
		return prev;
	}
	
	public static boolean containsArtifical(int[] basis, int N) {
		for (int i : basis) {
			if (i >= N) {
				return true;
			}
		}
		return false;
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
	
	public static BigFraction[][] createPhaseOneTableau(BigFraction[][] matrix, int[] basis) {
		int N = matrix[0].length - 1;
		BigFraction[][] result = new BigFraction[matrix.length + 1][(N * 2) + 1];
		
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
					basis[i] = j;
				} else if (j == result[0].length - 1) {
					result[i][j] = matrix[i][N];
				} else {
					result[i][j] = new BigFraction("0");
				}				
			}
		}
		
		return result;
	}
	
	public static BigFraction[][] createPhaseTwoTableau(BigFraction[][] matrix, int[] basis) {
		BigFraction[][] result = new BigFraction[matrix.length][(matrix[0].length / 2) + 1];
		
		for (int i = 0; i < result.length - 1; i++) {
			for (int j = 0; j < result[0].length; j++) {
				if (j == result[0].length - 1) {
					result[i][j] = matrix[i][matrix[0].length - 1];
				} else {
					result[i][j] = matrix[i][j];
				}
			}
		}
		
		for (int i = 0; i < result[0].length; i++) {
			if (result[result.length - 1][i] == null) {
				result[result.length - 1][i] = new BigFraction("0");
			}
			
			if (!containsBasis(basis, i)) {
				BigFraction sum = new BigFraction("0");
				for (int j = 0; j < result.length; j++) {
					sum.add(result[j][i].multiply(new BigFraction("-1")));
				}
				result[result.length-1][i] = sum.subtract(new BigFraction("-1"));
			} 
				
		}
		
		return result;
	}
	
	public static boolean containsBasis(int[] basis, int n) {
		for (int i : basis) {
			if (i == n) {
				return true;
			}
		}
		return false;
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
