package expression.generic;

import expression.parser.ParsingException;
import expression.parser.StringSource;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private static final Map<String, TabulatingMode<?>> MODES = Map.of(
            "i", new TabulatingMode<>(new CheckedIntArithmetic()),
            "d", new TabulatingMode<>(new DoubleArithmetic()),
            "bi", new TabulatingMode<>(new BigIntegerArithmetic()),
            "u", new TabulatingMode<>(new IntegerArithmetic()),
            "l", new TabulatingMode<>(new LongArithmetic()),
            "s", new TabulatingMode<>(new ShortArithmetic())
    );

    @Override
    public Object[][][] tabulate(
            final String mode, final String expression,
            final int x1, final int x2,
            final int y1, final int y2,
            final int z1, final int z2
    ) throws ParsingException {
        final TabulatingMode<?> m = MODES.get(mode);
        if (m == null) {
            throw new IllegalArgumentException("Unknown mode: " + mode);
        }
        return tabulate(m, expression, x1, x2, y1, y2, z1, z2);
    }

    private <T extends Number> Object[][][] tabulate(
            final TabulatingMode<T> mode, final String expression,
            final int x1, final int x2,
            final int y1, final int y2,
            final int z1, final int z2
    ) throws ParsingException {
        return evaluate(
                mode.arithmetic,
                mode.makeParser(new StringSource(expression)).parse(),
                x1, x2,
                y1, y2,
                z1, z2
        );
    }

    private <T extends Number> Object[][][] evaluate(
            final Arithmetic<T> arithmetic, final GenericExpression<T> expression,
            final int x1, final int x2,
            final int y1, final int y2,
            final int z1, final int z2
    ) {
        final Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
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
