package expression;

public class Divide extends BinaryOperation {
    public Divide(Operand expr1, Operand expr2) {
        super(expr1, expr2);
    }

    public static final int ORDER = Order.MULTIPLICATIVE.ordinal();

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    protected String getSymbol() {
        return "/";
    }

    @Override
    protected int calculate(int n1, int n2) {
        return n1 / n2;
    }

    @Override
    protected double calculate(double n1, double n2) {
        return n1 / n2;
    }

    @Override
    protected boolean needRightParentheses() {
        return this.getOrder() <= rightOperand.getOrder();
    }
}
