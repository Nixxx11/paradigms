package expression.generic;

public enum BasicGenericBinaryOperations implements GenericBinaryOperations {
    ADD(GenericAdd.ORDER) {
        @Override
        public <T extends Number> GenericExpression<T> create(final GenericExpression<T> left, final GenericExpression<T> right) {
            return new GenericAdd<>(left, right);
        }
    },
    SUBTRACT(GenericSubtract.ORDER) {
        @Override
        public <T extends Number> GenericExpression<T> create(final GenericExpression<T> left, final GenericExpression<T> right) {
            return new GenericSubtract<>(left, right);
        }
    },
    MULTIPLY(GenericMultiply.ORDER) {
        @Override
        public <T extends Number> GenericExpression<T> create(final GenericExpression<T> left, final GenericExpression<T> right) {
            return new GenericMultiply<>(left, right);
        }
    },
    DIVIDE(GenericDivide.ORDER) {
        @Override
        public <T extends Number> GenericExpression<T> create(final GenericExpression<T> left, final GenericExpression<T> right) {
            return new GenericDivide<>(left, right);
        }
    },
    MOD(Mod.ORDER) {
        @Override
        public <T extends Number> GenericExpression<T> create(final GenericExpression<T> left, final GenericExpression<T> right) {
            return new Mod<>(left, right);
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
    public abstract <T extends Number> GenericExpression<T> create(final GenericExpression<T> left, final GenericExpression<T> right);
}