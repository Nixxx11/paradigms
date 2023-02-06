package expression.parser;

import expression.Count;
import expression.Negate;
import expression.Operand;
import expression.exceptions.CheckedNegate;
import expression.exceptions.Log10;
import expression.exceptions.Power10;

public enum BasicUnaryOperations implements UnaryOperations {
    NEGATE {
        @Override
        public Operand create(Operand inner) {
            return new Negate(inner);
        }
    }, CHECKED_NEGATE {
        @Override
        public Operand create(Operand inner) {
            return new CheckedNegate(inner);
        }
    }, COUNT {
        @Override
        public Operand create(Operand inner) {
            return new Count(inner);
        }
    }, POWER10 {
        @Override
        public Operand create(Operand inner) {
            return new Power10(inner);
        }
    }, LOG10 {
        @Override
        public Operand create(Operand inner) {
            return new Log10(inner);
        }
    };

    public abstract Operand create(final Operand inner);
}
