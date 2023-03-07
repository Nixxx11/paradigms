package expression.generic;

public class Abs<T extends Number> extends GenericUnaryOperation<T> {
    public Abs(final GenericExpression<T> expr) {
        super(expr);
    }

    @Override
    protected String getSymbol() {
        return "abs";
    }

    @Override
    protected T calculate(final T n, final Arithmetic<T> a) {
        return a.abs(n);
    }
}
