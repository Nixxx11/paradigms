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
    protected <T extends Number> T calculate(final T n, final Arithmetic<T> a) {
        return a.negate(n);
    }
}
