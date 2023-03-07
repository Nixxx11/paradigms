package expression.generic;

public class Abs extends GenericUnaryOperation {
    public Abs(final GenericExpression expr) {
        super(expr);
    }

    @Override
    protected String getSymbol() {
        return "abs";
    }

    @Override
    protected <T extends Number> T calculate(final T n, final Arithmetic<T> a) {
        return a.abs(n);
    }
}
