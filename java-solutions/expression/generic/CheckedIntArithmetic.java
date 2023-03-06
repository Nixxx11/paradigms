package expression.generic;

public class CheckedIntArithmetic implements Arithmetic<Integer> {
    @Override
    public Integer add(final Integer n1, final Integer n2) {
        int min = n2 >= 0 ? Integer.MIN_VALUE : Integer.MIN_VALUE - n2;
        int max = n2 >= 0 ? Integer.MAX_VALUE - n2 : Integer.MAX_VALUE;
        if (n1 > max || n1 < min) {
            throw new ArithmeticException(n1 + " + " + n2 + " caused an overflow");
        }
        return n1 + n2;
    }

    @Override
    public Integer subtract(final Integer n1, final Integer n2) {
        int min = n2 >= 0 ? Integer.MIN_VALUE + n2 : Integer.MIN_VALUE;
        int max = n2 >= 0 ? Integer.MAX_VALUE : Integer.MAX_VALUE + n2;
        if (n1 > max || n1 < min) {
            throw new ArithmeticException(n1 + " - " + n2 + " caused an overflow");
        }
        return n1 - n2;
    }

    @Override
    public Integer multiply(final Integer n1, final Integer n2) {
        if (n2 == 0) {
            return 0;
        }

        if (n2 == -1 && n1 == Integer.MIN_VALUE) {
            throw new ArithmeticException(n1 + " * " + n2 + " caused an overflow");
        }

        final int result = n1 * n2;
        if (result / n2 != n1) {
            throw new ArithmeticException(n1 + " * " + n2 + " caused an overflow");
        }
        return result;
    }

    @Override
    public Integer divide(final Integer n1, final Integer n2) throws ArithmeticException {
        if (n1 == Integer.MIN_VALUE && n2 == -1) {
            throw new ArithmeticException(n1 + " / " + n2 + " caused an overflow");
        }
        return n1 / n2;
    }

    @Override
    public Integer negate(final Integer n) {
        if (n == Integer.MIN_VALUE) {
            throw new ArithmeticException("- " + n + " caused an overflow");
        }
        return -n;
    }

    @Override
    public Integer valueOf(final Number n) {
        return n.intValue();
    }
}
