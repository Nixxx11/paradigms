package expression.generic;

import expression.Order;

public class GenericMultiply extends GenericBinaryOperation {
    public GenericMultiply(final GenericExpression expr1, final GenericExpression expr2) {
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
    protected <T extends Number> T calculate(final T n1, final T n2, final Arithmetic<T> a) {
        return a.multiply(n1, n2);
    }

    @Override
    protected boolean needRightParentheses() {
        return this.getOrder() < rightOperand.getOrder() || rightOperand instanceof GenericDivide;
    }
}
