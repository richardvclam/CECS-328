package assignments.pondscum;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigFraction {
	
	public BigInteger num;
	public BigInteger denom;
	
	public BigFraction(BigInteger num) {
		this.num = num;
		this.denom = BigInteger.ONE;
	}
	
	public BigFraction(BigInteger num, BigInteger denom) {
		this.num = num;
		this.denom = denom;
		
		reduce();
	}
	
	public void add(BigFraction bf) {
		makeDenomEqual(bf);
		
		if (denom.equals(bf.denom)) {
			num = num.add(bf.num);
		}
		
		reduce();
	}
	
	public void subtract(BigFraction bf) {
		makeDenomEqual(bf);
		
		if (denom.equals(bf.denom)) {
			num = num.subtract(bf.num);
		}
		
		reduce();
	}
	
	public void multiply(BigFraction bf) {
		num = num.multiply(bf.num);
		denom = denom.multiply(bf.denom);
		
		reduce();
	}
	
	public void divide(BigFraction bf) {
		num = num.multiply(bf.denom);
		denom = denom.multiply(bf.num);
		
		reduce();
	}
	
	public void makeDenomEqual(BigFraction bf) {
		if (!denom.equals(bf.denom)) {
			BigInteger tempDenom = bf.denom;
			
			bf.num = bf.num.multiply(denom);
			bf.denom = bf.denom.multiply(denom);
			
			num = num.multiply(tempDenom);
			denom = denom.multiply(tempDenom);
		}
	}
	
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
		if (denom.equals(BigInteger.ONE)) {
			return num.toString();
		}
		return num + "/" + denom;
	}
	
	public static void main(String[] args) {
		//System.out.println((double) 177603/377);
		//double d = (double) 177603/377;
		//System.out.println(new BigFraction(new BigDecimal("" + d)));
		//System.out.println(new BigFraction(new BigDecimal("253.1964285714285714285714285714")));
		BigFraction a = new BigFraction(new BigInteger("4"), new BigInteger("3"));
		BigFraction b = new BigFraction(new BigInteger("5"), new BigInteger("2"));
		BigFraction c = new BigFraction(new BigInteger("2"));
		a.divide(c);
		
		System.out.println(a);
		
	}

}
