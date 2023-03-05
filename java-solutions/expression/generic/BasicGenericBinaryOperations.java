package expression.generic;

public enum BasicGenericBinaryOperations implements GenericBinaryOperations {
    ADD(GenericAdd.ORDER) {
        @Override
        public GenericOperand create(final GenericOperand left, final GenericOperand right) {
            return new GenericAdd(left, right);
        }
    },
    SUBTRACT(GenericSubtract.ORDER) {
        @Override
        public GenericOperand create(final GenericOperand left, final GenericOperand right) {
            return new GenericSubtract(left, right);
        }
    },
    MULTIPLY(GenericMultiply.ORDER) {
        @Override
        public GenericOperand create(final GenericOperand left, final GenericOperand right) {
            return new GenericMultiply(left, right);
        }
    },
    DIVIDE(GenericDivide.ORDER) {
        @Override
        public GenericOperand create(final GenericOperand left, final GenericOperand right) {
            return new GenericDivide(left, right);
        }
    };
    private final int order;

    BasicGenericBinaryOperations(final int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public abstract GenericOperand create(final GenericOperand left, final GenericOperand right);
}