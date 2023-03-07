package expression.generic;

public enum BasicGenericUnaryOperations implements GenericUnaryOperations {
    NEGATE {
        @Override
        public GenericExpression create(final GenericExpression inner) {
            return new GenericNegate(inner);
        }
    },
    ABS {
        @Override
        public GenericExpression create(final GenericExpression inner) {
            return new Abs(inner);
        }
    },
    SQUARE {
        @Override
        public GenericExpression create(final GenericExpression inner) {
            return new Square(inner);
        }
    };

    @Override
    public abstract GenericExpression create(final GenericExpression inner);
}
