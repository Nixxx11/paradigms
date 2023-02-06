package expression.exceptions;

import expression.TripleExpression;

public class IllegalOperandValueException extends EvaluatingException {
    public IllegalOperandValueException(final String message, final TripleExpression operation) {
        super(message, operation);
    }
}
