package expression.generic;

public class CheckedIntType implements NumberType<CheckedIntType> {
    private final int number;

    public CheckedIntType(final int n) {
        number = n;
    }

    @Override
    public CheckedIntType add(final CheckedIntType x) throws OverflowException {
        int min = x.number >= 0 ? Integer.MIN_VALUE : Integer.MIN_VALUE - x.number;
        int max = x.number >= 0 ? Integer.MAX_VALUE - x.number : Integer.MAX_VALUE;
        if (number > max || number < min) {
            throw new OverflowException(number + " + " + x.number + " caused an overflow");
        }
        return new CheckedIntType(number + x.number);
    }

    @Override
    public CheckedIntType subtract(final CheckedIntType x) throws OverflowException {
        int min = x.number >= 0 ? Integer.MIN_VALUE + x.number : Integer.MIN_VALUE;
        int max = x.number >= 0 ? Integer.MAX_VALUE : Integer.MAX_VALUE + x.number;
        if (number > max || number < min) {
            throw new OverflowException(number + " - " + x.number + " caused an overflow");
        }
        return new CheckedIntType(number - x.number);
    }

    @Override
    public CheckedIntType multiply(final CheckedIntType x) throws OverflowException {
        if (x.number == 0) {
            return new CheckedIntType(0);
        }

        if (x.number == -1 && number == Integer.MIN_VALUE) {
            throw new OverflowException(number + " * " + x.number + " caused an overflow");
        }

        final int result = number * x.number;
        if (result / x.number != number) {
            throw new OverflowException(number + " * " + x.number + " caused an overflow");
        }
        return new CheckedIntType(result);
    }

    @Override
    public CheckedIntType divide(final CheckedIntType x) throws ArithmeticException {
        if (x.number == 0) {
            throw new DivisionByZeroException(number + " is being divided by 0");
        }
        if (number == Integer.MIN_VALUE && x.number == -1) {
            throw new OverflowException(number + " / " + x.number + " caused an overflow");
        }
        return new CheckedIntType(number / x.number);
    }

    @Override
    public CheckedIntType negate() throws OverflowException {
        if (number == Integer.MIN_VALUE) {
            throw new OverflowException("- " + number + " caused an overflow");
        }
        return new CheckedIntType(-number);
    }

    @Override
    public CheckedIntType valueOf(final Number n) {
        return new CheckedIntType(n.intValue());
    }

    @Override
    public Number value() {
        return number;
    }
}
