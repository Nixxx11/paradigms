package expression.generic;

public enum BasicGenericUnaryOperations implements GenericUnaryOperations {
    NEGATE {
        @Override
        public <T extends Number> GenericExpression<T> create(final GenericExpression<T> inner) {
            return new GenericNegate<>(inner);
        }
    },
    ABS {
        @Override
        public <T extends Number> GenericExpression<T> create(final GenericExpression<T> inner) {
            return new Abs<>(inner);
        }
    },
    SQUARE {
        @Override
        public <T extends Number> GenericExpression<T> create(final GenericExpression<T> inner) {
            return new Square<>(inner);
        }
    };

    @Override
    public abstract <T extends Number> GenericExpression<T> create(final GenericExpression<T> inner);
}
