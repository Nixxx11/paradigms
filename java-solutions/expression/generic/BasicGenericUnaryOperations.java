package expression.generic;

public enum BasicGenericUnaryOperations implements GenericUnaryOperations {
    NEGATE {
        @Override
        public GenericOperand create(final GenericOperand inner) {
            return new GenericNegate(inner);
        }
    };

    @Override
    public abstract GenericOperand create(final GenericOperand inner);
}
