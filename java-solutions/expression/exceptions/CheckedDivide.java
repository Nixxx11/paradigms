package expression.exceptions;

import expression.Divide;
import expression.Operand;

public class CheckedDivide extends Divide {
    public CheckedDivide(Operand expr1, Operand expr2) {
        super(expr1, expr2);
    }

    protected void checkExceptions(int n1, int n2) throws ArithmeticException {
        if (n2 == 0) {
            throw new DivisionByZeroException(n1 + " is being divided by 0", this);
        }
        if (n1 == Integer.MIN_VALUE && n2 == -1) {
            throw new OverflowException(n1 + " / " + n2 + " caused an overflow", this);
        }
    }

    @Override
    protected int calculate(int n1, int n2) {
        checkExceptions(n1, n2);
        return n1 / n2;
    }
}
