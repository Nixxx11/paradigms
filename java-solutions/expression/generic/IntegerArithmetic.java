package expression.generic;

public class IntegerArithmetic implements Arithmetic<Integer> {
    @Override
    public Integer add(final Integer n1, final Integer n2) {
        return n1 + n2;
    }

    @Override
    public Integer subtract(final Integer n1, final Integer n2) {
        return n1 - n2;
    }

    @Override
    public Integer multiply(final Integer n1, final Integer n2) {
        return n1 * n2;
    }

    @Override
    public Integer divide(final Integer n1, final Integer n2) {
        return n1 / n2;
    }

    @Override
    public Integer mod(final Integer n1, final Integer n2) {
        return n1 % n2;
    }

    @Override
    public Integer negate(final Integer n) {
        return -n;
    }

    @Override
    public boolean isNegative(final Integer n) {
        return n < 0;
    }

    @Override
    public Integer valueOf(final Number n) {
        return n.intValue();
    }
}
