package expression.generic;

public interface Arithmetic<T extends Number> {
    T add(T n1, T n2);

    T subtract(T n1, T n2);

    T multiply(T n1, T n2);

    T divide(T n1, T n2);

    T negate(T n);

    T valueOf(Number n);
}
