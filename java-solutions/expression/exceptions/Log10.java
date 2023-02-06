package expression.exceptions;

import expression.Operand;
import expression.UnaryOperation;

public class Log10 extends UnaryOperation {
    public Log10(Operand expr) {
        super(expr);
    }

    @Override
    protected String getSymbol() {
        return "log10";
    }

    @Override
    protected int calculate(int value) {
        if (value <= 0) {
            throw new IllegalOperandValueException("Non-positive value for log10: " + value, this);
        }
        int result = 0;
        while (value >= 10) {
            value /= 10;
            result++;
        }
        return result;
    }

    @Override
    protected double calculate(double value) {
        throw new UnsupportedOperationException("log10 is only possible with integers");
    }
}
