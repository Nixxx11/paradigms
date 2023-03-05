package expression.generic;

public class DoubleType implements NumberType<DoubleType> {
    private final double number;

    public DoubleType(final double n) {
        number = n;
    }

    @Override
    public DoubleType add(final DoubleType x) {
        return new DoubleType(number + x.number);
    }

    @Override
    public DoubleType subtract(final DoubleType x) {
        return new DoubleType(number - x.number);
    }

    @Override
    public DoubleType multiply(final DoubleType x) {
        return new DoubleType(number * x.number);
    }

    @Override
    public DoubleType divide(final DoubleType x) {
        return new DoubleType(number / x.number);
    }

    @Override
    public DoubleType negate() {
        return new DoubleType(-number);
    }

    @Override
    public DoubleType valueOf(final Number n) {
        return new DoubleType(n.doubleValue());
    }

    @Override
    public Number value() {
        return number;
    }
}
