package expression.parser;

import expression.TripleExpression;

import java.util.Map;

public class ExpressionParser implements TripleParser {
    private static final Map<String, BinaryOperations> BINARY_OPERATIONS = Map.of(
            "+", BasicBinaryOperations.ADD,
            "-", BasicBinaryOperations.SUBTRACT,
            "*", BasicBinaryOperations.MULTIPLY,
            "/", BasicBinaryOperations.DIVIDE,
            "set", BasicBinaryOperations.SET,
            "clear", BasicBinaryOperations.CLEAR
    );
    private static final Map<String, UnaryOperations> UNARY_OPERATIONS = Map.of(
            "-", BasicUnaryOperations.NEGATE,
            "count", BasicUnaryOperations.COUNT
    );

    @Override
    public TripleExpression parse(final String expression) {
        BasicExpressionParser parser = new BasicExpressionParser(new StringSource(expression),
                BINARY_OPERATIONS, UNARY_OPERATIONS);
        try {
            return parser.parseExpression();
        } catch (ParsingException e) {
            return null;
        }
    }
}
