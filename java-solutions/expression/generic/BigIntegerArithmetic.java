package expression.generic;

import java.math.BigInteger;

public class BigIntegerArithmetic implements Arithmetic<BigInteger> {
    @Override
    public BigInteger add(final BigInteger n1, final BigInteger n2) {
        return n1.add(n2);
    }

    @Override
    public BigInteger subtract(final BigInteger n1, final BigInteger n2) {
        return n1.subtract(n2);
    }

    @Override
    public BigInteger multiply(final BigInteger n1, final BigInteger n2) {
        return n1.multiply(n2);
    }

    @Override
    public BigInteger divide(final BigInteger n1, final BigInteger n2) {
        return n1.divide(n2);
    }

    @Override
    public BigInteger mod(final BigInteger n1, final BigInteger n2) {
        return n1.mod(n2);
    }

    @Override
    public int compare(final BigInteger n1, final BigInteger n2) {
        return n1.compareTo(n2);
    }

    @Override
    public BigInteger negate(final BigInteger n) {
        return n.negate();
    }

    @Override
    public BigInteger abs(final BigInteger n) {
        return n.abs();
    }

    @Override
    public BigInteger valueOf(final Number n) {
        return BigInteger.valueOf(n.longValue());
    }

    @Override
    public BigInteger parse(final String s) {
        return new BigInteger(s);
    }
}
