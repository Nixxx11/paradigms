package expression.parser;

import expression.*;
import expression.exceptions.*;

public enum BasicBinaryOperations implements BinaryOperations {
    ADD(Add.ORDER) {
        @Override
        public Operand create(Operand left, Operand right) {
            return new Add(left, right);
        }
    }, CHECKED_ADD(CheckedAdd.ORDER) {
        @Override
        public Operand create(Operand left, Operand right) {
            return new CheckedAdd(left, right);
        }
    }, SUBTRACT(Subtract.ORDER) {
        @Override
        public Operand create(Operand left, Operand right) {
            return new Subtract(left, right);
        }
    }, CHECKED_SUBTRACT(CheckedSubtract.ORDER) {
        @Override
        public Operand create(Operand left, Operand right) {
            return new CheckedSubtract(left, right);
        }
    }, MULTIPLY(Multiply.ORDER) {
        @Override
        public Operand create(Operand left, Operand right) {
            return new Multiply(left, right);
        }
    }, CHECKED_MULTIPLY(CheckedMultiply.ORDER) {
        @Override
        public Operand create(Operand left, Operand right) {
            return new CheckedMultiply(left, right);
        }
    }, DIVIDE(Divide.ORDER) {
        @Override
        public Operand create(Operand left, Operand right) {
            return new Divide(left, right);
        }
    }, CHECKED_DIVIDE(CheckedDivide.ORDER) {
        @Override
        public Operand create(Operand left, Operand right) {
            return new CheckedDivide(left, right);
        }
    }, SET(Set.ORDER) {
        @Override
        public Operand create(Operand left, Operand right) {
            return new Set(left, right);
        }
    }, CLEAR(Clear.ORDER) {
        @Override
        public Operand create(Operand left, Operand right) {
            return new Clear(left, right);
        }
    };

    private final int order;

    BasicBinaryOperations(final int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public abstract Operand create(final Operand left, final Operand right);
}
