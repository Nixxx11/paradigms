package expression;

import java.util.Objects;

public abstract class UnaryOperation implements Operand {
    protected final Operand innerOperand;

    protected UnaryOperation(final Operand expr) {
        this.innerOperand = expr;
    }

    protected abstract int calculate(int value);

    protected abstract double calculate(double value);

    protected abstract String getSymbol();

    protected boolean needParentheses() {
        return this.getOrder() < innerOperand.getOrder();
    }

    @Override
    public int getOrder() {
        return Order.UNARY.ordinal();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calculate(innerOperand.evaluate(x, y, z));
    }

    @Override
    public int evaluate(int x) {
        return calculate(innerOperand.evaluate(x));
    }

    @Override
    public double evaluate(double x) {
        return calculate(innerOperand.evaluate(x));
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
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            return this.innerOperand.equals(((UnaryOperation) obj).innerOperand);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(innerOperand.hashCode(), this.getClass());
    }
}
