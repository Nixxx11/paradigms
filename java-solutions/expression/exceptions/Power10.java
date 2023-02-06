package expression.exceptions;

import expression.Operand;
import expression.UnaryOperation;

public class Power10 extends UnaryOperation {
    public Power10(Operand expr) {
        super(expr);
    }

    @Override
    protected String getSymbol() {
        return "pow10";
    }

    @Override
    protected int calculate(int value) {
        if (value >= 10) {
            throw new OverflowException("pow10 " + value + " caused an overflow", this);
        }
        if (value < 0) {
            throw new IllegalOperandValueException("Negative value for pow10: " + value, this);
        }
        int result = 1;
        for (int i = 0; i < value; i++) {
            result *= 10;
        }
        return result;
    }

    @Override
    protected double calculate(double value) {
        throw new UnsupportedOperationException("pow10 is only possible with integers");
    }
}
