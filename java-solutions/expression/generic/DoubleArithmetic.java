package expression.generic;

public class DoubleArithmetic implements Arithmetic<Double> {
    @Override
    public Double add(final Double n1, final Double n2) {
        return n1 + n2;
    }

    @Override
    public Double subtract(final Double n1, final Double n2) {
        return n1 - n2;
    }

    @Override
    public Double multiply(final Double n1, final Double n2) {
        return n1 * n2;
    }

    @Override
    public Double divide(final Double n1, final Double n2) {
        return n1 / n2;
    }

    @Override
    public Double mod(final Double n1, final Double n2) {
        return n1 % n2;
    }

    @Override
    public Double negate(final Double n) {
        return -n;
    }

    @Override
    public boolean isNegative(final Double n) {
        return n < 0;
    }

    @Override
    public Double abs(final Double n) {
        return Math.abs(n);
    }

    @Override
    public Double valueOf(final Number n) {
        return n.doubleValue();
    }
}
