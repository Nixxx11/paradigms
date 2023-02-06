package expression;

public class Set extends BinaryOperation {
    public Set(Operand expr1, Operand expr2) {
        super(expr1, expr2);
    }

    public static final int ORDER = Order.BITWISE.ordinal();

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    protected String getSymbol() {
        return "set";
    }

    @Override
    protected int calculate(int n1, int n2) {
        return n1 | (1 << n2);
    }

    @Override
    protected double calculate(double n1, double n2) {
        throw new UnsupportedOperationException("set is only possible with integers");
    }

    @Override
    protected boolean needRightParentheses() {
        return this.getOrder() <= rightOperand.getOrder();
    }
}
