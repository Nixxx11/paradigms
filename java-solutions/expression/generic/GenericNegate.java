package expression.generic;

public class GenericNegate<T extends Number> extends GenericUnaryOperation<T> {
    public GenericNegate(final GenericExpression<T> expr) {
        super(expr);
    }

    @Override
    protected String getSymbol() {
        return "-";
    }

    @Override
    protected T calculate(final T n, final Arithmetic<T> a) {
        return a.negate(n);
    }
}
