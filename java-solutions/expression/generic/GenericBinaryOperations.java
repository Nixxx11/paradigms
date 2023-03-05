package expression.generic;

public interface GenericBinaryOperations {
    int getOrder();
    GenericOperand create(GenericOperand left, GenericOperand right);
}
