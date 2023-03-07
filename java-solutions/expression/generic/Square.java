package expression.generic;

public class Square extends GenericUnaryOperation {
    public Square(final GenericExpression expr) {
        super(expr);
    }

    @Override
    protected String getSymbol() {
        return "square";
    }

    @Override
    protected <T extends Number> T calculate(final T n, final Arithmetic<T> a) {
        return a.square(n);
    }
}
