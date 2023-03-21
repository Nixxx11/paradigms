package expression.generic;

import expression.Order;
import expression.parser.CharSource;

import java.util.Map;

public class TabulatingMode<T extends Number> {
    public final Arithmetic<T> arithmetic;
    public final Map<String, BinaryOperation<T>> binaryOperations;
    public final Map<String, OperatorUnary<T>> unaryOperations;

    public TabulatingMode(final Arithmetic<T> arithmetic) {
        this.arithmetic = arithmetic;
        binaryOperations = Map.of(
                "+", new BinaryOperation<>(arithmetic::add, Order.ADDITIVE),
                "-", new BinaryOperation<>(arithmetic::subtract, Order.ADDITIVE),
                "*", new BinaryOperation<>(arithmetic::multiply, Order.MULTIPLICATIVE),
                "/", new BinaryOperation<>(arithmetic::divide, Order.MULTIPLICATIVE),
                "mod", new BinaryOperation<>(arithmetic::mod, Order.MULTIPLICATIVE)
        );
        unaryOperations = Map.of(
                "-", arithmetic::negate,
                "abs", arithmetic::abs,
                "square", (n) -> arithmetic.multiply(n, n)
        );
    }


    public GenericExpressionParser<T> makeParser(final CharSource source) {
        return new GenericExpressionParser<>(source, binaryOperations, unaryOperations, arithmetic);
    }
}
