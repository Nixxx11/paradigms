package expression.exceptions;

import expression.TripleExpression;

public class DivisionByZeroException extends EvaluatingException {
    public DivisionByZeroException(final String message, final TripleExpression operation) {
        super(message, operation);
    }
}
