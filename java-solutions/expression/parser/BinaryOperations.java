package expression.parser;

import expression.Operand;

public interface BinaryOperations {
    int getOrder();
    Operand create(Operand left, Operand right);
}
