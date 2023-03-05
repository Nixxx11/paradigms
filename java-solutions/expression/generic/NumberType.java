package expression.generic;

public interface NumberType<T extends NumberType<T>> {
    T add(T x);

    T subtract(T x);

    T multiply(T x);

    T divide(T x);

    T negate();

    T valueOf(Number n);

    Number value();
}
