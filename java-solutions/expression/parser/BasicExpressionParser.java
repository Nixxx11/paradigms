package expression.parser;

import expression.Const;
import expression.Operand;
import expression.TripleExpression;
import expression.Variable;

import java.util.Map;

public class BasicExpressionParser extends BaseParser {
    protected final Map<String, BinaryOperations> binaryOperations;
    protected final Map<String, UnaryOperations> unaryOperations;
    protected BinaryOperations savedOp = null;
    protected String savedToken = null;

    public BasicExpressionParser(
            final CharSource source,
            final Map<String, BinaryOperations> binaryOperations,
            final Map<String, UnaryOperations> unaryOperations
    ) {
        super(source);
        this.binaryOperations = binaryOperations;
        this.unaryOperations = unaryOperations;
    }

    public TripleExpression parseExpression() throws ParsingException {
        final Operand result = parse();
        if (!eof() || savedToken != null) {
            throw error(String.valueOf(CharSource.EOF), getToken());
        }
        return result;
    }

    protected Operand parse() throws ParsingException {
        Operand left = getOperand();
        while (hasNextOperation()) {
            left = finishOperation(left, getBinaryOperation());
        }
        return left;
    }

    protected Operand finishOperation(final Operand left, final BinaryOperations op) throws ParsingException {
        Operand right = getOperand();
        while (hasNextOperation()) {
            final BinaryOperations nextOp = getBinaryOperation();
            if (nextOp.getOrder() >= op.getOrder()) {
                savedOp = nextOp;
                break;
            }
            right = finishOperation(right, nextOp);
        }
        return op.create(left, right);
    }

    protected Operand getOperand() throws ParsingException {
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
                    return new Const(Integer.parseInt(sb.toString()));
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
            case "x", "y", "z" -> new Variable(token);
            case "(" -> {
                final Operand result = parse();
                final String nextToken = getToken();
                if (!")".equals(nextToken)) {
                    throw error(")", nextToken);
                }
                yield result;
            }
            default -> throw error("an operand", token);
        };
    }

    protected BinaryOperations getBinaryOperation() throws ParsingException {
        if (savedOp != null) {
            final BinaryOperations result = savedOp;
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
