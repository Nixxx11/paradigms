package expression.exceptions;

import expression.Negate;
import expression.Operand;

public class CheckedNegate extends Negate {
    public CheckedNegate(Operand expr) {
        super(expr);
    }

    protected void checkExceptions(int n) throws ArithmeticException {
        if (n == Integer.MIN_VALUE) {
            throw new OverflowException("- " + n + " caused an overflow", this);
        }
    }

    @Override
    protected int calculate(int n) {
        checkExceptions(n);
        return -n;
    }
}
