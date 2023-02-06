package expression.exceptions;

import expression.TripleExpression;
import expression.parser.*;

import java.util.Map;

public class ExpressionParser implements TripleParser {
    private static final Map<String, BinaryOperations> BINARY_OPERATIONS = Map.of(
            "+", BasicBinaryOperations.CHECKED_ADD,
            "-", BasicBinaryOperations.CHECKED_SUBTRACT,
            "*", BasicBinaryOperations.CHECKED_MULTIPLY,
            "/", BasicBinaryOperations.CHECKED_DIVIDE,
            "set", BasicBinaryOperations.SET,
            "clear", BasicBinaryOperations.CLEAR
    );
    private static final Map<String, UnaryOperations> UNARY_OPERATIONS = Map.of(
            "-", BasicUnaryOperations.CHECKED_NEGATE,
            "count", BasicUnaryOperations.COUNT,
            "pow10", BasicUnaryOperations.POWER10,
            "log10", BasicUnaryOperations.LOG10
    );

    @Override
    public TripleExpression parse(final String expression) throws ParsingException {
        BasicExpressionParser parser = new BasicExpressionParser(new StringSource(expression),
                BINARY_OPERATIONS, UNARY_OPERATIONS);
        return parser.parseExpression();
    }
}
