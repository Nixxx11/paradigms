package expression.generic;

public class ShortArithmetic implements Arithmetic<Short> {
    @Override
    public Short add(final Short n1, final Short n2) {
        return (short) (n1 + n2);
    }

    @Override
    public Short subtract(final Short n1, final Short n2) {
        return (short) (n1 - n2);
    }

    @Override
    public Short multiply(final Short n1, final Short n2) {
        return (short) (n1 * n2);
    }

    @Override
    public Short divide(final Short n1, final Short n2) {
        return (short) (n1 / n2);
    }

    @Override
    public Short mod(final Short n1, final Short n2) {
        return (short) (n1 % n2);
    }

    @Override
    public Short negate(final Short n) {
        return (short) -n;
    }

    @Override
    public boolean isNegative(final Short n) {
        return n < 0;
    }

    @Override
    public Short valueOf(final Number n) {
        return n.shortValue();
    }
}
