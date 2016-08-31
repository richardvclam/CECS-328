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
	
	public BigFraction add(BigFraction bf) {
		makeDenomEqual(bf);
		
		BigFraction f = new BigFraction(num.add(bf.num), denom);
		f.reduce();
		
		return f;
	}
	
	public BigFraction subtract(BigFraction bf) {
		makeDenomEqual(bf);
		
		BigFraction f = new BigFraction(num.subtract(bf.num), denom);
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
		return new BigFraction(num.abs(), denom);
	}
	
	public int compareTo(BigFraction bf) {
		makeDenomEqual(bf);
		
		return num.compareTo(bf.num);
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
		
		if (num.signum() == -1 && denom.signum() == -1) {
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
