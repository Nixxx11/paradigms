package expression.generic;

import java.util.Objects;

public abstract class GenericBinaryOperation<T extends Number> implements GenericExpression<T> {
    protected final GenericExpression<T> leftOperand;
    protected final GenericExpression<T> rightOperand;

    protected GenericBinaryOperation(final GenericExpression<T> leftOperand, final GenericExpression<T> rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    protected abstract T calculate(T n1, T n2, Arithmetic<T> a);

    protected abstract String getSymbol();

    protected boolean needLeftParentheses() {
        return this.getOrder() < leftOperand.getOrder();
    }

    protected boolean needRightParentheses() {
        return this.getOrder() < rightOperand.getOrder();
    }

    @Override
    public T evaluate(final T x, final T y, final T z, final Arithmetic<T> a) {
        return calculate(leftOperand.evaluate(x, y, z, a), rightOperand.evaluate(x, y, z, a), a);
    }

    @Override
    public String toString() {
        return '(' + leftOperand.toString() + ' ' + getSymbol() + ' ' + rightOperand.toString() + ')';
    }

    @Override
    public String toMiniString() {
        StringBuilder sb = new StringBuilder();
        if (needLeftParentheses()) {
            sb.append('(');
            sb.append(leftOperand.toMiniString());
            sb.append(')');
        } else {
            sb.append(leftOperand.toMiniString());
        }
        sb.append(' ');
        sb.append(getSymbol());
        sb.append(' ');
        if (needRightParentheses()) {
            sb.append('(');
            sb.append(rightOperand.toMiniString());
            sb.append(')');
        } else {
            sb.append(rightOperand.toMiniString());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            return leftOperand.equals(((GenericBinaryOperation<?>) obj).leftOperand)
                    && rightOperand.equals(((GenericBinaryOperation<?>) obj).rightOperand);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftOperand.hashCode(), rightOperand.hashCode(), this.getClass());
    }
}
