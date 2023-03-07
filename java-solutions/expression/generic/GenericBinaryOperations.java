package expression.generic;

public interface GenericBinaryOperations {
    int getOrder();
    <T extends Number> GenericExpression<T> create(GenericExpression<T> left, GenericExpression<T> right);
}
