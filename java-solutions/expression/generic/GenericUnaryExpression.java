package expression.generic;

public class GenericUnaryExpression<T extends Number> implements GenericExpression<T> {
    private final GenericExpression<T> innerOperand;
    private final OperatorUnary<T> operation;

    public GenericUnaryExpression(final GenericExpression<T> expr, final OperatorUnary<T> operation) {
        this.innerOperand = expr;
        this.operation = operation;
    }

    @Override
    public T evaluate(final T x, final T y, final T z) {
        return operation.calculate(innerOperand.evaluate(x, y, z));
    }
}
