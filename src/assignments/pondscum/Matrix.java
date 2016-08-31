package assignments.pondscum;

import java.math.BigInteger;

public class Matrix {
	
	public static void main(String[] args) {
		long[][] A = { {4, -1, 0},
                		 {-1, 4, -1},
                		 {0, -1, 4}
					   };
		
		long[] c = {602, 1018, 1077};
		
		long[][] D = {{2,1,1}, {1,-1,-1}, {1,2,1}}; // 3
		long[][] E = {{5,-2,1}, {0,3,-1}, {2,0,7}}; // 103
		long[][] F = {{4,-1,1}, {4,5,3}, {-2,0,0}}; // 16
		long[][] G = {{6,1,1}, {4,-2,5}, {2,8,7}}; // -306
		long[][] four = {{2,5,3,5}, {4,6,6,3}, {11,3,2,-2}, {4,-7,9,3}}; //2960
		long[][] H = {{1,5,4,2}, {-2,3,6,4}, {5,1,0,-1}, {2,3,-4,0}};
		long[][] five = {{2,5,3,5,6}, {4,6,6,3,2}, {11,3,2,-2,1}, {4,-7,9,3,0}, {22,6,2,5,9}};
		long[][] B = { {4,6} , {3,8} }; // 14
		
		System.out.println(determinant(four));
		
		/*
		BigFraction[] result = cramers(A, c);
		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i] + ",");
		}
		*/
	}
	
	public static long determinantFromUpperTriangularForm(long[][] matrix) {
		int n = matrix.length;
		
		BigFraction[][] result = new BigFraction[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = new BigFraction(new BigInteger(""+matrix[i][j]));
			}
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < i; j++) {
				if (!result[i][j].equals(0)) {
					for (int k = 0; k < n; k++) {
						result[i][k] = result[i][k].subtract(result[j][k]);
					}
				}
			}
		}
		
		return 0;
	}
	
	
	public static long determinant(long[][] A) {
		return determinant(A, 0);
	}
	
	private static long determinant(long[][] matrix, int column) {
		int n = matrix.length;
		
		if (n == 1) {
			return matrix[0][0];
		} else if (n == 2) {
			return (matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]);
		}
		
		long det = 0;
		
		for (int k = 0; k < n; k++) {
			long[][] subMatrix = new long[n-1][n-1];
			for (int i = 1; i < n; i++) {
				int counter = 0;
				for (int j = 0; j < n; j++) {
					if (j == k) {
						continue;
					} else {
						subMatrix[i-1][counter] = matrix[i][j];
						counter++;
					}
				}
			}
			det += (Math.pow(-1, 2 + k) * matrix[0][k] * determinant(subMatrix, column + 1));
		}
		
		return det;
	}
	
	
	/* Shortcut method for only 3x3 matrices
	public static long determinant(long[][] A) {
		int n = A.length;
		
		if (n == 2) {
			return (A[0][0] * A[1][1]) - (A[0][1] * A[1][0]);
		}
		
		long detOne = 0, detTwo = 0;
		for (int i = 0; i < n; i++) {
			long tempOne = 1;
			long tempTwo = 1;
			
			for (int j = 0; j < n; j++) {
				tempOne *= A[j][(i+j)%n];
				tempTwo *= A[n-j-1][(i+j)%n];
			}
			detOne += tempOne;
			detTwo += tempTwo;
		}
		
		return detOne - detTwo;
	}
	*/
	
	public static BigFraction[] cramers(long[][] matrix, long[] constants) {
		int n = constants.length;
		BigFraction[] result = new BigFraction[n];
		
		long D = determinant(matrix);
		
		for (int k = 0; k < n; k++) {
			long[][] temp = new long[n][n];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					temp[i][j] = matrix[i][j];
				}
				temp[i][k] = constants[i];
			}
			result[k] = new BigFraction(new BigInteger(""+determinant(temp)), new BigInteger(""+D));
		}
		
		return result;
	}
	
	
}
