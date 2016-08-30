package assignments.pondscum;

import java.math.BigDecimal;

public class BigFraction {
	
	private int num;
	private int denom;
	
	public BigFraction(BigDecimal decimal) {
		String str = String.valueOf(decimal);
		int decDigits = str.length() - 1 - str.indexOf('.');
		
		this.denom = 1;
		for (int i = 0; i < decDigits; i++) {
			decimal.multiply(new BigDecimal(10));
			denom *= 10;
		}
		
		this.num = decimal.ROUND_UP;
		
		reduce();
	}
	
	public void reduce() {
		int gcd = gcd(num, denom);
		
		num /= gcd;
		denom /= gcd;
	}
	
	public int gcd(int num, int denom) {
		if (num == 0 || denom == 0) {
			return num + denom;
		}
		return gcd(denom, num % denom);
	}
	
	public String toString() {
		return num + "/" + denom;
	}
	
	public static void main(String[] args) {
		System.out.println(new BigFraction(new BigDecimal(1/2)));
	}

}
