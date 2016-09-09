package assignments.pondscum;

import java.math.BigInteger;

public class Other {
	
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
            
            if (max != k) {
	            BigFraction[] temp = coeff[k]; 
	            coeff[k] = coeff[max]; 
	            coeff[max] = temp;
	            
	            BigFraction t = consts[k]; 
	            consts[k] = consts[max]; 
	            consts[max] = t;
            }

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
