package expression.generic;

public interface Arithmetic<T extends Number> {
    T add(T n1, T n2);

    T subtract(T n1, T n2);

    T multiply(T n1, T n2);

    T divide(T n1, T n2);

    T mod(T n1, T n2);

    T negate(T n);

    default T abs(T n) {
        return isNegative(n) ? negate(n) : n;
    }

    default T square(T n) {
        return multiply(n, n);
    }

    boolean isNegative(T n);

    T valueOf(Number n);
}
