package expression.generic;

import java.math.BigInteger;

public class BigIntegerType implements NumberType<BigIntegerType> {
    private final BigInteger number;

    public BigIntegerType(final long n) {
        number = BigInteger.valueOf(n);
    }

    public BigIntegerType(final BigInteger n) {
        number = n;
    }

    @Override
    public BigIntegerType add(final BigIntegerType x) {
        return new BigIntegerType(number.add(x.number));
    }

    @Override
    public BigIntegerType subtract(final BigIntegerType x) {
        return new BigIntegerType(number.subtract(x.number));
    }

    @Override
    public BigIntegerType multiply(final BigIntegerType x) {
        return new BigIntegerType(number.multiply(x.number));
    }

    @Override
    public BigIntegerType divide(final BigIntegerType x) {
        return new BigIntegerType(number.divide(x.number));
    }

    @Override
    public BigIntegerType negate() {
        return new BigIntegerType(number.negate());
    }

    @Override
    public BigIntegerType valueOf(final Number n) {
        return new BigIntegerType(BigInteger.valueOf(n.longValue()));
    }

    @Override
    public Number value() {
        return number;
    }
}
