package expression;

public class Negate extends UnaryOperation {
    public Negate(Operand expr) {
        super(expr);
    }

    @Override
    protected String getSymbol() {
        return "-";
    }

    @Override
    protected int calculate(int value) {
        return -value;
    }

    @Override
    protected double calculate(double value) {
        return -value;
    }
}
