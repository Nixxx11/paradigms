package expression.generic;

public class GenericBinaryExpression<T extends Number> implements GenericExpression<T> {
    private final GenericExpression<T> leftOperand;
    private final GenericExpression<T> rightOperand;
    private final OperatorBinary<T> operation;

    public GenericBinaryExpression(
            final GenericExpression<T> leftOperand,
            final GenericExpression<T> rightOperand,
            final OperatorBinary<T> operation
    ) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.operation = operation;
    }

    @Override
    public T evaluate(final T x, final T y, final T z) {
        return operation.calculate(leftOperand.evaluate(x, y, z), rightOperand.evaluate(x, y, z));
    }
}
