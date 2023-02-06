package expression.exceptions;

import expression.TripleExpression;

public class OverflowException extends EvaluatingException {
    public OverflowException(final String message, final TripleExpression operation) {
        super(message, operation);
    }
}
