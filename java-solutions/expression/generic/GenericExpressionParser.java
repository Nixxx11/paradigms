package expression.generic;

import expression.parser.BaseParser;
import expression.parser.CharSource;
import expression.parser.ParsingException;

import java.util.Map;

public class GenericExpressionParser<T extends Number> extends BaseParser {
    protected final Map<String, GenericBinaryOperations> binaryOperations;
    protected final Map<String, GenericUnaryOperations> unaryOperations;
    protected final NumberParser<T> numberParser;
    protected GenericBinaryOperations savedOp = null;
    protected String savedToken = null;

    public GenericExpressionParser(
            final CharSource source,
            final Map<String, GenericBinaryOperations> binaryOperations,
            final Map<String, GenericUnaryOperations> unaryOperations,
            final NumberParser<T> numberParser
    ) {
        super(source);
        this.binaryOperations = binaryOperations;
        this.unaryOperations = unaryOperations;
        this.numberParser = numberParser;
    }

    public GenericExpression<T> parseExpression() throws ParsingException {
        final GenericExpression<T> result = parse();
        if (!eof() || savedToken != null) {
            throw error(String.valueOf(CharSource.EOF), getToken());
        }
        return result;
    }

    protected GenericExpression<T> parse() throws ParsingException {
        GenericExpression<T> left = getOperand();
        while (hasNextOperation()) {
            left = finishOperation(left, getBinaryOperation());
        }
        return left;
    }

    protected GenericExpression<T> finishOperation(
            final GenericExpression<T> left, final GenericBinaryOperations op
    ) throws ParsingException {
        GenericExpression<T> right = getOperand();
        while (hasNextOperation()) {
            final GenericBinaryOperations nextOp = getBinaryOperation();
            if (nextOp.getOrder() >= op.getOrder()) {
                savedOp = nextOp;
                break;
            }
            right = finishOperation(right, nextOp);
        }
        return op.create(left, right);
    }

    protected GenericExpression<T> getOperand() throws ParsingException {
        skipWhitespace();
        final String token;
        if (test('-') || Character.isDigit(peek())) {
            final StringBuilder sb = new StringBuilder();
            if (take('-')) {
                sb.append('-');
            }
            if (Character.isDigit(peek())) {
                while (Character.isDigit(peek())) {
                    sb.append(take());
                }
                try {
                    return new GenericConst<>(numberParser.parse(sb.toString()));
                } catch (NumberFormatException e) {
                    throw error("Invalid number format: " + sb);
                }
            } else {
                token = sb.toString();
            }
        } else {
            token = getToken();
        }
        if (unaryOperations.containsKey(token)) {
            return unaryOperations.get(token).create(getOperand());
        }
        return switch (token) {
            case "x", "y", "z" -> new GenericVariable<>(token);
            case "(" -> {
                final GenericExpression<T> result = parse();
                final String nextToken = getToken();
                if (!")".equals(nextToken)) {
                    throw error(")", nextToken);
                }
                yield result;
            }
            default -> throw error("an operand", token);
        };
    }

    protected GenericBinaryOperations getBinaryOperation() throws ParsingException {
        if (savedOp != null) {
            final GenericBinaryOperations result = savedOp;
            savedOp = null;
            return result;
        }
        skipWhitespace();
        final String operation = getToken();
        if (!binaryOperations.containsKey(operation)) {
            throw error("an operation", operation);
        }
        return binaryOperations.get(operation);
    }

    protected String getToken() {
        if (savedToken != null) {
            String result = savedToken;
            savedToken = null;
            return result;
        }
        if (Character.isJavaIdentifierStart(peek())) {
            final StringBuilder identifier = new StringBuilder();
            identifier.append(take());
            while (!eof() && Character.isJavaIdentifierPart(peek())) {
                identifier.append(take());
            }
            return identifier.toString();
        } else if (Character.getType(peek()) == Character.MATH_SYMBOL) {
            final StringBuilder identifier = new StringBuilder();
            while (!eof() && Character.getType(peek()) == Character.MATH_SYMBOL) {
                identifier.append(take());
            }
            return identifier.toString();
        }
        return String.valueOf(take());
    }

    protected boolean hasNextOperation() {
        skipWhitespace();
        if (savedOp != null) {
            return true;
        }
        if (eof()) {
            return false;
        }
        String token = getToken();
        if (binaryOperations.containsKey(token)) {
            savedOp = binaryOperations.get(token);
            return true;
        } else {
            savedToken = token;
            return false;
        }
    }
}
