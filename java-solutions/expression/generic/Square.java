package expression.generic;

public class Square<T extends Number> extends GenericUnaryOperation<T> {
    public Square(final GenericExpression<T> expr) {
        super(expr);
    }

    @Override
    protected String getSymbol() {
        return "square";
    }

    @Override
    protected T calculate(final T n, final Arithmetic<T> a) {
        return a.square(n);
    }
}
