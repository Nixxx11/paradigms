package expression.generic;

public class CheckedIntArithmetic implements Arithmetic<Integer> {
    @Override
    public Integer add(final Integer n1, final Integer n2) throws OverflowException {
        int min = n2 >= 0 ? Integer.MIN_VALUE : Integer.MIN_VALUE - n2;
        int max = n2 >= 0 ? Integer.MAX_VALUE - n2 : Integer.MAX_VALUE;
        if (n1 > max || n1 < min) {
            throw new OverflowException(n1 + " + " + n2 + " caused an overflow");
        }
        return n1 + n2;
    }

    @Override
    public Integer subtract(final Integer n1, final Integer n2) throws OverflowException {
        int min = n2 >= 0 ? Integer.MIN_VALUE + n2 : Integer.MIN_VALUE;
        int max = n2 >= 0 ? Integer.MAX_VALUE : Integer.MAX_VALUE + n2;
        if (n1 > max || n1 < min) {
            throw new OverflowException(n1 + " - " + n2 + " caused an overflow");
        }
        return n1 - n2;
    }

    @Override
    public Integer multiply(final Integer n1, final Integer n2) throws OverflowException {
        if (n2 == 0) {
            return 0;
        }

        if (n2 == -1 && n1 == Integer.MIN_VALUE) {
            throw new OverflowException(n1 + " * " + n2 + " caused an overflow");
        }

        final int result = n1 * n2;
        if (result / n2 != n1) {
            throw new OverflowException(n1 + " * " + n2 + " caused an overflow");
        }
        return result;
    }

    @Override
    public Integer divide(final Integer n1, final Integer n2) throws ArithmeticException {
        if (n2 == 0) {
            throw new DivisionByZeroException(n1 + " is being divided by 0");
        }
        if (n1 == Integer.MIN_VALUE && n2 == -1) {
            throw new OverflowException(n1 + " / " + n2 + " caused an overflow");
        }
        return n1 / n2;
    }

    @Override
    public Integer negate(final Integer n) throws OverflowException {
        if (n == Integer.MIN_VALUE) {
            throw new OverflowException("- " + n + " caused an overflow");
        }
        return -n;
    }

    @Override
    public Integer valueOf(final Number n) {
        return n.intValue();
    }
}
