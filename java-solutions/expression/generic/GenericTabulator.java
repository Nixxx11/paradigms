package expression.generic;

import expression.parser.ParsingException;
import expression.parser.StringSource;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private static final Map<String, GenericBinaryOperations> BINARY_OPERATIONS = Map.of(
            "+", BasicGenericBinaryOperations.ADD,
            "-", BasicGenericBinaryOperations.SUBTRACT,
            "*", BasicGenericBinaryOperations.MULTIPLY,
            "/", BasicGenericBinaryOperations.DIVIDE
    );
    private static final Map<String, GenericUnaryOperations> UNARY_OPERATIONS = Map.of(
            "-", BasicGenericUnaryOperations.NEGATE
    );

    @Override
    public Object[][][] tabulate(
            final String mode, final String expression,
            final int x1, final int x2,
            final int y1, final int y2,
            final int z1, final int z2
    ) throws ParsingException {
        GenericExpressionParser parser = new GenericExpressionParser(
                new StringSource(expression),
                BINARY_OPERATIONS,
                UNARY_OPERATIONS
        );
        final GenericExpression expr = parser.parseExpression();
        final Arithmetic<? extends Number> a = switch (mode) {
            case "i" -> new CheckedIntArithmetic();
            case "d" -> new DoubleArithmetic();
            case "bi" -> new BigIntegerArithmetic();
            default -> throw new IllegalArgumentException("Unknown mode: " + mode);
        };
        return tabulate(a, expr, x1, x2, y1, y2, z1, z2);
    }

    private <T extends Number> Object[][][] tabulate(
            final Arithmetic<T> a, final GenericExpression expression,
            final int x1, final int x2,
            final int y1, final int y2,
            final int z1, final int z2
    ) {
        final int dx = x2 - x1 + 1;
        final int dy = y2 - y1 + 1;
        final int dz = z2 - z1 + 1;
        Object[][][] result = new Object[dx][dy][dz];
        for (int i = 0; i < dx; i++) {
            for (int j = 0; j < dy; j++) {
                for (int k = 0; k < dz; k++) {
                    try {
                        result[i][j][k] = expression.evaluate(
                                a.valueOf(x1 + i),
                                a.valueOf(y1 + j),
                                a.valueOf(z1 + k),
                                a
                        );
                    } catch (ArithmeticException e) {
                        result[i][j][k] = null;
                    }
                }
            }
        }
        return result;
    }
}
