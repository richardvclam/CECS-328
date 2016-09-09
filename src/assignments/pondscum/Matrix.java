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
		long[][] four = {{2,5,3,5}, {4,6,6,3}, {11,3,2,-2}, {4,-7,9,3}}; // 2960
		long[][] H = {{1,5,4,2}, {-2,3,6,4}, {5,1,0,-1}, {2,3,-4,0}}; // 242
		long[][] five = {{2,5,3,5,6}, {4,6,6,3,2}, {11,3,2,-2,1}, {4,-7,9,3,0}, {22,6,2,5,9}}; // -21560
		long[][] B = { {4,6} , {3,8} }; // 14
		
		//System.out.println(determinant(four));
		//System.out.println(determinantFromUpperTriangular(five));
		
		
		BigFraction[] result = cramers(A, c);
		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i] + ",");
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
		
		BigFraction D = determinantFromUpperTriangular(matrix);
		//long det = determinant(matrix);
		//System.out.println("Det: " + det);
		//System.out.println("D:" + D);
		
		for (int k = 0; k < n; k++) {
			long[][] temp = new long[n][n];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					temp[i][j] = matrix[i][j];
				}
				temp[i][k] = constants[i];
			}
			//result[k] = new BigFraction(new BigInteger(""+determinant(temp)), new BigInteger(""+D));
			BigFraction t = determinantFromUpperTriangular(temp);
			result[k] = t.divide(D);
			//long tt = determinant(temp);
			//System.out.println("Det[" + k + "] " + tt);
			//System.out.println("D[" + k + "] " + t);
			System.out.println(k+1 + "/" + n + " Completed");
		}
		
		return result;
	}
	
	
}
