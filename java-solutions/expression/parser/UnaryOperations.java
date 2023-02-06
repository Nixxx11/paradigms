package expression.parser;

import expression.Operand;

public interface UnaryOperations {
    Operand create(Operand inner);
}
