package expression.generic;

public class GenericNegate extends GenericUnaryOperation {
    public GenericNegate(final GenericExpression expr) {
        super(expr);
    }

    @Override
    protected String getSymbol() {
        return "-";
    }

    @Override
    protected <T extends NumberType<T>> T calculate(final T value) {
        return value.negate();
    }
}
