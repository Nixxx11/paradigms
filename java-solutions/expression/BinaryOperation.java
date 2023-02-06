package expression;

import java.util.Objects;

public abstract class BinaryOperation implements Operand {
    protected final Operand leftOperand;
    protected final Operand rightOperand;

    protected BinaryOperation(final Operand leftOperand, final Operand rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    protected abstract int calculate(int n1, int n2);

    protected abstract double calculate(double n1, double n2);

    protected abstract String getSymbol();

    protected boolean needLeftParentheses() {
        return this.getOrder() < leftOperand.getOrder();
    }

    protected boolean needRightParentheses() {
        return this.getOrder() < rightOperand.getOrder();
    }

    @Override
    public int evaluate(int x) {
        return calculate(leftOperand.evaluate(x), rightOperand.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calculate(leftOperand.evaluate(x, y, z), rightOperand.evaluate(x, y, z));
    }

    @Override
    public double evaluate(double x) {
        return calculate(leftOperand.evaluate(x), rightOperand.evaluate(x));
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
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            return this.leftOperand.equals(((BinaryOperation) obj).leftOperand)
                    && this.rightOperand.equals(((BinaryOperation) obj).rightOperand);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftOperand.hashCode(), rightOperand.hashCode(), this.getClass());
    }
}
