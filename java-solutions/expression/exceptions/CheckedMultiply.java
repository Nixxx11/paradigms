package expression.exceptions;

import expression.Multiply;
import expression.Operand;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(Operand expr1, Operand expr2) {
        super(expr1, expr2);
    }

    @Override
    protected int calculate(int n1, int n2) {
        if (n2 == 0) {
            return 0;
        }

        if (n2 == -1 && n1 == Integer.MIN_VALUE) {
            throw new OverflowException(n1 + " * " + n2 + " caused an overflow", this);
        }

        int result = n1 * n2;
        if (result / n2 != n1) {
            throw new OverflowException(n1 + " * " + n2 + " caused an overflow", this);
        }
        return result;
    }
}
