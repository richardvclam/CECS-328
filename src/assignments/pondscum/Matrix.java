package assignments.pondscum;

public class Matrix {
	
	public static void main(String[] args) {
		double[][] A = { {4, -1, 0},
                		 {-1, 4, -1},
                		 {0, -1, 4}
					   };
		
		double[] c = {602, 1018, 1077};
		
		double[][] B = { {4,5} , {0,6} };
		
		System.out.println(determinant(B));
	}
	
	public static double[][] inverse(double[][] A) {
		int n = A.length;
		if (A[0].length != n) {
			throw new RuntimeException("The matrix must be a square.");
		}
		
		if (n == 2) { // if A matrix is a 2x2
			double det = 1 / determinant(A);

			double[][] B = {{A[1][1], -A[0][1]},
							{-A[1][0], A[0][0]}};
			
			for (int i = 0; i < n; i++) {
				for (int j = 0; i < n; j++) {
					B[i][j] *= det;
				}
			}
			
			return B;
		}
		
		return null;
		
	}
	
	public static double determinant(double[][] A) {
		int n = A.length;
		
		if (n == 2) {
			return (A[0][0] * A[1][1]) - (A[0][1] * A[1][0]);
		}
		
		int det1 = 0, det2 = 0;
		for (int i = 0; i < n; i++) {
			int temp1 = 0, temp2 = 0;
			for (int j = 0; j < n; j++) {
				temp1 *= A[(i + j) % n][j];
				temp2 *= A[(i + j) % n][n - 1 - j];
			}
			det1 += temp1;
			det2 += temp2;
		}
		return det1 - det2;
	}
	
	public static double[][] cofactor(double[][] A) {
		int n = A.length;
		
		
	}
}
