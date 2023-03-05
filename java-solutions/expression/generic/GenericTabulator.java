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
        GenericOperand expr = parser.parseExpression();
        final int dx = x2 - x1 + 1;
        final int dy = y2 - y1 + 1;
        final int dz = z2 - z1 + 1;
        Object[][][] result = new Object[dx][dy][dz];
        switch (mode) {
            case "i" -> {
                for (int i = 0; i < dx; i++) {
                    for (int j = 0; j < dy; j++) {
                        for (int k = 0; k < dz; k++) {
                            try {
                                result[i][j][k] = expr.evaluate(
                                        new CheckedIntType(x1 + i),
                                        new CheckedIntType(y1 + j),
                                        new CheckedIntType(z1 + k)
                                ).value();
                            } catch (ArithmeticException e) {
                                result[i][j][k] = null;
                            }
                        }
                    }
                }
            }
            case "d" -> {
                for (int i = 0; i < dx; i++) {
                    for (int j = 0; j < dy; j++) {
                        for (int k = 0; k < dz; k++) {
                            try {
                                result[i][j][k] = expr.evaluate(
                                        new DoubleType(x1 + i),
                                        new DoubleType(y1 + j),
                                        new DoubleType(z1 + k)
                                ).value();
                            } catch (ArithmeticException e) {
                                result[i][j][k] = null;
                            }
                        }
                    }
                }
            }
            case "bi" -> {
                for (int i = 0; i < dx; i++) {
                    for (int j = 0; j < dy; j++) {
                        for (int k = 0; k < dz; k++) {
                            try {
                                result[i][j][k] = expr.evaluate(
                                        new BigIntegerType(x1 + i),
                                        new BigIntegerType(y1 + j),
                                        new BigIntegerType(z1 + k)
                                ).value();
                            } catch (ArithmeticException e) {
                                result[i][j][k] = null;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
