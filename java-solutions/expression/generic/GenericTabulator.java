package expression.generic;

import expression.parser.ParsingException;
import expression.parser.StringSource;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private static final Map<String, GenericBinaryOperations> BINARY_OPERATIONS = Map.of(
            "+", BasicGenericBinaryOperations.ADD,
            "-", BasicGenericBinaryOperations.SUBTRACT,
            "*", BasicGenericBinaryOperations.MULTIPLY,
            "/", BasicGenericBinaryOperations.DIVIDE,
            "mod", BasicGenericBinaryOperations.MOD
    );
    private static final Map<String, GenericUnaryOperations> UNARY_OPERATIONS = Map.of(
            "-", BasicGenericUnaryOperations.NEGATE,
            "abs", BasicGenericUnaryOperations.ABS,
            "square", BasicGenericUnaryOperations.SQUARE
    );

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
        Arithmetic<?> a = MODES.getOrDefault(mode, null);
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
        final GenericExpressionParser<T> parser = new GenericExpressionParser<>(
                new StringSource(expression),
                BINARY_OPERATIONS,
                UNARY_OPERATIONS,
                arithmetic
        );
        final GenericExpression<T> expr = parser.parseExpression();
        final int dx = x2 - x1 + 1;
        final int dy = y2 - y1 + 1;
        final int dz = z2 - z1 + 1;
        Object[][][] result = new Object[dx][dy][dz];
        for (int i = 0; i < dx; i++) {
            for (int j = 0; j < dy; j++) {
                for (int k = 0; k < dz; k++) {
                    try {
                        result[i][j][k] = expr.evaluate(
                                arithmetic.valueOf(x1 + i),
                                arithmetic.valueOf(y1 + j),
                                arithmetic.valueOf(z1 + k),
                                arithmetic
                        );
                    } catch (ArithmeticException ignored) {
                    }
                }
            }
        }
        return result;
    }
}
