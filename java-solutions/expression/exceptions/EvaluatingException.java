package expression.exceptions;

import expression.TripleExpression;

public abstract class EvaluatingException extends RuntimeException {
    private final TripleExpression operation;
    protected EvaluatingException(final String message, final TripleExpression operation) {
        super(message);
        this.operation = operation;
    }
    public TripleExpression getOperation() {
        return operation;
    }
}
