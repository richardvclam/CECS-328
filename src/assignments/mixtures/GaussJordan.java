package assignments.mixtures;

import java.math.BigInteger;

public class GaussJordan {
	
	public static void main(String[] args) {
		BigFraction[][] matrix = {{new BigFraction(new BigInteger("1")), new BigFraction(new BigInteger("2"))}, 
							  	{new BigFraction(new BigInteger("4")), new BigFraction(new BigInteger("8"))},
							  	{new BigFraction(new BigInteger("7")), new BigFraction(new BigInteger("8"))},
							  	{new BigFraction("10"), new BigFraction("11")}
		};
		BigFraction[] constants = {new BigFraction(new BigInteger("3")), new BigFraction(new BigInteger("6")), new BigFraction(new BigInteger("9")), new BigFraction("12")};
		
		for (int i = 0; i < matrix[0].length; i++) {
			BigFraction pivot = matrix[i][i];
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = matrix[i][j].divide(pivot);
				//System.out.println(matrix[i][j]);
			}
			
			constants[i] = constants[i].divide(pivot);
			//System.out.println(constants[i]);
			
			for (int j = 0; j < matrix.length; j++) {
				if (j == i) {
					continue;
				}
				BigFraction elim = BigFraction.ZERO;
				for (int k = i; k < matrix[0].length; k++) {
					if (elim.equals(BigFraction.ZERO)) {
						elim = matrix[j][k];
					}
					matrix[j][k] = matrix[j][k].subtract(matrix[i][k].multiply(elim));
					//System.out.println(matrix[j][k]);
				}
				constants[j] = constants[j].subtract(constants[i].multiply(elim));
				//System.out.println(constants[j]);
			}
			//System.out.println();
			
			for (int k = 0; k < matrix.length; k++) {
				for (int j = 0; j < matrix[0].length; j++) {
					System.out.print(matrix[k][j] + ",");
				}
				System.out.print(constants[k]);
				System.out.println();
			}
			System.out.println();
		}
		
		
		
		
	
	}

}
