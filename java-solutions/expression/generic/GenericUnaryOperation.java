package expression.generic;

import expression.Order;

import java.util.Objects;

public abstract class GenericUnaryOperation<T extends Number> implements GenericExpression<T> {
    protected final GenericExpression<T> innerOperand;

    protected GenericUnaryOperation(final GenericExpression<T> expr) {
        this.innerOperand = expr;
    }

    protected abstract T calculate(T n, Arithmetic<T> a);

    protected abstract String getSymbol();

    protected boolean needParentheses() {
        return this.getOrder() < innerOperand.getOrder();
    }

    @Override
    public int getOrder() {
        return Order.UNARY.ordinal();
    }

    @Override
    public T evaluate(final T x, final T y, final T z, final Arithmetic<T> a) {
        return calculate(innerOperand.evaluate(x, y, z, a), a);
    }

    @Override
    public String toString() {
        return getSymbol() + '(' + innerOperand.toString() + ')';
    }

    @Override
    public String toMiniString() {
        if (needParentheses()) {
            return getSymbol() + '(' + innerOperand.toMiniString() + ')';
        } else {
            return getSymbol() + ' ' + innerOperand.toMiniString();
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            return innerOperand.equals(((GenericUnaryOperation<?>) obj).innerOperand);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(innerOperand.hashCode(), this.getClass());
    }
}
