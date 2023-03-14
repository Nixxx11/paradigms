package expression.generic;

import expression.Order;
import expression.parser.ParsingException;
import expression.parser.StringSource;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private static final Map<String, Arithmetic<?>> MODES = Map.of(
            "i", new CheckedIntArithmetic(),
            "d", new DoubleArithmetic(),
            "bi", new BigIntegerArithmetic(),
            "u", new IntegerArithmetic(),
            "l", new LongArithmetic(),
            "s", new ShortArithmetic()
    );

    @Override
    public Object[][][] tabulate(
            final String mode, final String expression,
            final int x1, final int x2,
            final int y1, final int y2,
            final int z1, final int z2
    ) throws ParsingException {
        final Arithmetic<?> a = MODES.get(mode);
        if (a == null) {
            throw new IllegalArgumentException("Unknown mode: " + mode);
        }
        return tabulate(a, expression, x1, x2, y1, y2, z1, z2);
    }

    private <T extends Number> Object[][][] tabulate(
            final Arithmetic<T> arithmetic, final String expression,
            final int x1, final int x2,
            final int y1, final int y2,
            final int z1, final int z2
    ) throws ParsingException {
        return evaluate(arithmetic, parse(arithmetic, expression), x1, x2, y1, y2, z1, z2);
    }

    private <T extends Number> GenericExpression<T> parse(
            final Arithmetic<T> arithmetic, final String expression
    ) throws ParsingException {
        // :NOTE: precreate parser
        final Map<String, BinaryOperation<T>> BINARY_OPERATIONS = Map.of(
                "+", new BinaryOperation<>(arithmetic::add, Order.ADDITIVE),
                "-", new BinaryOperation<>(arithmetic::subtract, Order.ADDITIVE),
                "*", new BinaryOperation<>(arithmetic::multiply, Order.MULTIPLICATIVE),
                "/", new BinaryOperation<>(arithmetic::divide, Order.MULTIPLICATIVE),
                "mod", new BinaryOperation<>(arithmetic::mod, Order.MULTIPLICATIVE)
        );
        final Map<String, OperatorUnary<T>> UNARY_OPERATIONS = Map.of(
                "-", arithmetic::negate,
                "abs", arithmetic::abs,
                "square", (n) -> arithmetic.multiply(n, n)
        );
        final GenericExpressionParser<T> parser = new GenericExpressionParser<>(
                new StringSource(expression),
                BINARY_OPERATIONS,
                UNARY_OPERATIONS,
                arithmetic
        );
        return parser.parseExpression();
    }

    private <T extends Number> Object[][][] evaluate(
            final Arithmetic<T> arithmetic, final GenericExpression<T> expression,
            final int x1, final int x2,
            final int y1, final int y2,
            final int z1, final int z2
    ) {
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    try {
                        result[i - x1][j - y1][k - z1] = expression.evaluate(
                                arithmetic.valueOf(i),
                                arithmetic.valueOf(j),
                                arithmetic.valueOf(k)
                        );
                    } catch (ArithmeticException ignored) {
                    }
                }
            }
        }
        return result;
    }
}
