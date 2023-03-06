package expression.generic;

public enum BasicGenericUnaryOperations implements GenericUnaryOperations {
    NEGATE {
        @Override
        public GenericExpression create(final GenericExpression inner) {
            return new GenericNegate(inner);
        }
    };

    @Override
    public abstract GenericExpression create(final GenericExpression inner);
}
