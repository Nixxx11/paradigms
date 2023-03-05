package expression.generic;

import expression.Order;

public class GenericAdd extends GenericBinaryOperation {
    public GenericAdd(final GenericOperand expr1, final GenericOperand expr2) {
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
    protected <T extends NumberType<T>> T calculate(final T n1, final T n2) {
        return n1.add(n2);
    }
}
