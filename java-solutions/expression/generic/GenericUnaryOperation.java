package expression.generic;

import expression.Order;

import java.util.Objects;

public abstract class GenericUnaryOperation implements GenericExpression {
    protected final GenericExpression innerOperand;

    protected GenericUnaryOperation(final GenericExpression expr) {
        this.innerOperand = expr;
    }

    protected abstract <T extends NumberType<T>> T calculate(T value);

    protected abstract String getSymbol();

    protected boolean needParentheses() {
        return this.getOrder() < innerOperand.getOrder();
    }

    @Override
    public int getOrder() {
        return Order.UNARY.ordinal();
    }

    @Override
    public <T extends NumberType<T>> T evaluate(final T x, final T y, final T z) {
        return calculate(innerOperand.evaluate(x, y, z));
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
            return this.innerOperand.equals(((GenericUnaryOperation) obj).innerOperand);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(innerOperand.hashCode(), this.getClass());
    }
}
