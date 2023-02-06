package expression;

public interface Operand extends Expression, DoubleExpression, TripleExpression {
    int getOrder();
}
