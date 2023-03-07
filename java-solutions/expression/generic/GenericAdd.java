package expression.generic;

import expression.Order;

public class GenericAdd<T extends Number> extends GenericBinaryOperation<T> {
    public GenericAdd(final GenericExpression<T> expr1, final GenericExpression<T> expr2) {
        super(expr1, expr2);
    }

    public static final int ORDER = Order.ADDITIVE.ordinal();

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    protected String getSymbol() {
        return "+";
    }

    @Override
    protected T calculate(final T n1, final T n2, final Arithmetic<T> a) {
        return a.add(n1, n2);
    }
}
