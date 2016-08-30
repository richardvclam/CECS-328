package assignments.pondscum;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigFraction {
	
	private BigInteger num;
	private BigInteger denom;
	
	public BigFraction(BigDecimal decimal) {
		String str = decimal.stripTrailingZeros().toPlainString();
		long decDigits = (long)str.length() - (long)str.indexOf('.') - 1;
		
		//System.out.println("dec digits: " + decDigits);
		
		denom = new BigInteger("1");
		for (int i = 0; i < decDigits; i++) {
			decimal = decimal.multiply(new BigDecimal("10"));
			denom = denom.multiply(new BigInteger("10"));
		}
		
		num = decimal.toBigIntegerExact();
		
		reduce();
	}
	
	public void reduce() {
		BigInteger gcd = gcd(num, denom);
		
		num = num.divide(gcd);
		denom = denom.divide(gcd);
	}
	
	public BigInteger gcd(BigInteger num, BigInteger denom) {
		if (num.equals(BigInteger.ZERO) || denom.equals(BigInteger.ZERO)) {
			return num.add(denom);
		}
		return gcd(denom, num.mod(denom));
	}
	
	public String toString() {
		return num + "/" + denom;
	}
	
	public static void main(String[] args) {
		System.out.println((double) 177603/377);
		double d = (double) 177603/377;
		System.out.println(new BigFraction(new BigDecimal("" + d)));
		System.out.println(new BigFraction(new BigDecimal("253.1964285714285714285714285714")));
		
	}

}
