package expression.generic;

public interface GenericBinaryOperations {
    int getOrder();
    GenericExpression create(GenericExpression left, GenericExpression right);
}
