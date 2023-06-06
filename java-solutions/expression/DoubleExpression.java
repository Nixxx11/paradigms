package expression;

/**
 * One-argument arithmetic expression over doubles.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@FunctionalInterface
public interface DoubleExpression extends ToMiniString {
    double evaluate(double x);
}
