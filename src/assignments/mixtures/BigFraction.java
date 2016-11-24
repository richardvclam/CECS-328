package assignments.mixtures;

import java.math.BigInteger;

public class BigFraction {
	
	public static BigFraction ONE = new BigFraction(BigInteger.ONE);
	public static BigFraction ZERO = new BigFraction(BigInteger.ZERO);
	
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
	
	public BigFraction(String str) {
		this.num = new BigInteger(str);
		this.denom = BigInteger.ONE;
	}
	
	public BigFraction add(BigFraction bf) {
		BigFraction b = new BigFraction(bf.num, bf.denom);
		makeDenomEqual(b);
		
		BigFraction f = new BigFraction(num.add(b.num), denom);
		f.reduce();
		
		return f;
	}
	
	public BigFraction subtract(BigFraction bf) {
		BigFraction b = new BigFraction(bf.num, bf.denom);
		makeDenomEqual(b);
		
		BigFraction f = new BigFraction(num.subtract(b.num), denom);
		f.reduce();
		
		return f;
	}
	
	public BigFraction multiply(BigFraction bf) {
		BigFraction f = new BigFraction(num.multiply(bf.num), denom.multiply(bf.denom));
		f.reduce();
		
		return f;
	}
	
	public BigFraction divide(BigFraction bf) {
		BigFraction f = new BigFraction(num.multiply(bf.denom), denom.multiply(bf.num));
		f.reduce();
		
		return f;
	}
	
	public BigFraction abs() {
		return new BigFraction(num.abs(), denom.abs());
	}
	
	public int compareTo(BigFraction bf) {
		BigFraction b = new BigFraction(bf.num, bf.denom);
		makeDenomEqual(b);
		
		return num.compareTo(b.num);
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
	
	public BigFraction negate() {
		BigFraction f = new BigFraction(num.negate(), denom);
		
		return f;
	}
	
	public boolean isNegative() {
		return num.signum() == -1;
	}
	
	public void reduce() {
		BigInteger gcd = gcd(num, denom);
		
		num = num.divide(gcd);
		denom = denom.divide(gcd);
		
		if ((num.signum() == -1 && denom.signum() == -1) || denom.signum() == -1) {
			num = num.negate();
			denom = denom.negate();
		}
	}
	
	public BigInteger gcd(BigInteger num, BigInteger denom) {
		if (num.equals(BigInteger.ZERO) || denom.equals(BigInteger.ZERO)) {
			return num.add(denom);
		}
		return gcd(denom.abs(), num.mod(denom.abs()).abs());
	}
	
	public boolean equals(BigFraction bf) {
		return num.equals(bf.num) && denom.equals(bf.denom);
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
		a = a.subtract(c);
		
		System.out.println(a);
		
	}

}
