package expression.generic;

public interface GenericUnaryOperations {
    <T extends Number> GenericExpression<T> create(GenericExpression<T> inner);
}
