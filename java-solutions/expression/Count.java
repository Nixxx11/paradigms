package expression;

public class Count extends UnaryOperation {
    public Count(Operand expr) {
        super(expr);
    }

    @Override
    protected String getSymbol() {
        return "count";
    }

    @Override
    protected int calculate(int value) {
        return Integer.bitCount(value);
    }

    @Override
    protected double calculate(double value) {
        throw new UnsupportedOperationException("count is only possible with integers");
    }
}
