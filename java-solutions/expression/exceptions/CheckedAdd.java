package expression.exceptions;

import expression.Add;
import expression.Operand;

public class CheckedAdd extends Add {
    public CheckedAdd(Operand expr1, Operand expr2) {
        super(expr1, expr2);
    }

    protected void checkExceptions(int n1, int n2) throws ArithmeticException {
        int n1Min = n2 >= 0 ? Integer.MIN_VALUE : Integer.MIN_VALUE - n2;
        int n1Max = n2 >= 0 ? Integer.MAX_VALUE - n2 : Integer.MAX_VALUE;
        if (n1 > n1Max || n1 < n1Min) {
            throw new OverflowException(n1 + " + " + n2 + " caused an overflow", this);
        }
    }

    @Override
    protected int calculate(int n1, int n2) {
        checkExceptions(n1, n2);
        return n1 + n2;
    }
}
