package expression.exceptions;

import expression.Operand;
import expression.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(Operand expr1, Operand expr2) {
        super(expr1, expr2);
    }

    public void checkExceptions(int n1, int n2) throws ArithmeticException {
        int n1Min = n2 >= 0 ? Integer.MIN_VALUE + n2 : Integer.MIN_VALUE;
        int n1Max = n2 >= 0 ? Integer.MAX_VALUE : Integer.MAX_VALUE + n2;
        if (n1 > n1Max || n1 < n1Min) {
            throw new OverflowException(n1 + " - " + n2 + " caused an overflow", this);
        }
    }

    @Override
    public int calculate(int n1, int n2) {
        checkExceptions(n1, n2);
        return n1 - n2;
    }
}
