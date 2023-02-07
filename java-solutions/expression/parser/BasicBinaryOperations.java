package expression.parser;

import expression.*;
import expression.exceptions.*;

public enum BasicBinaryOperations implements BinaryOperations {
    ADD {
        @Override
        public int getOrder() {
            return Add.ORDER;
        }

        @Override
        public Operand create(Operand left, Operand right) {
            return new Add(left, right);
        }
    }, CHECKED_ADD {
        @Override
        public int getOrder() {
            return CheckedAdd.ORDER;
        }

        @Override
        public Operand create(Operand left, Operand right) {
            return new CheckedAdd(left, right);
        }
    }, SUBTRACT {
        // :NOTE: getOrder -> order
        @Override
        public int getOrder() {
            return Subtract.ORDER;
        }

        @Override
        public Operand create(Operand left, Operand right) {
            return new Subtract(left, right);
        }
    }, CHECKED_SUBTRACT {
        @Override
        public int getOrder() {
            return CheckedSubtract.ORDER;
        }

        @Override
        public Operand create(Operand left, Operand right) {
            return new CheckedSubtract(left, right);
        }
    }, MULTIPLY {
        @Override
        public int getOrder() {
            return Multiply.ORDER;
        }

        @Override
        public Operand create(Operand left, Operand right) {
            return new Multiply(left, right);
        }
    }, CHECKED_MULTIPLY {
        @Override
        public int getOrder() {
            return CheckedMultiply.ORDER;
        }

        @Override
        public Operand create(Operand left, Operand right) {
            return new CheckedMultiply(left, right);
        }
    }, DIVIDE {
        @Override
        public int getOrder() {
            return Divide.ORDER;
        }

        @Override
        public Operand create(Operand left, Operand right) {
            return new Divide(left, right);
        }
    }, CHECKED_DIVIDE {
        @Override
        public int getOrder() {
            return CheckedDivide.ORDER;
        }

        @Override
        public Operand create(Operand left, Operand right) {
            return new CheckedDivide(left, right);
        }
    }, SET {
        @Override
        public int getOrder() {
            return Set.ORDER;
        }

        @Override
        public Operand create(Operand left, Operand right) {
            return new Set(left, right);
        }
    }, CLEAR {
        @Override
        public int getOrder() {
            return Clear.ORDER;
        }

        @Override
        public Operand create(Operand left, Operand right) {
            return new Clear(left, right);
        }
    };

    @Override
    public abstract int getOrder();

    @Override
    public abstract Operand create(final Operand left, final Operand right);
}
