package expression.generic;

import expression.Order;

public class GenericMultiply<T extends Number> extends GenericBinaryOperation<T> {
    public GenericMultiply(final GenericExpression<T> expr1, final GenericExpression<T> expr2) {
        super(expr1, expr2);
    }

    public static final int ORDER = Order.MULTIPLICATIVE.ordinal();

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    protected String getSymbol() {
        return "*";
    }

    @Override
    protected T calculate(final T n1, final T n2, final Arithmetic<T> a) {
        return a.multiply(n1, n2);
    }

    @Override
    protected boolean needRightParentheses() {
        return this.getOrder() < rightOperand.getOrder() || rightOperand instanceof GenericDivide;
    }
}
