package expression.generic;

public interface Arithmetic<T extends Number> extends NumberParser<T> {
    T add(T n1, T n2);

    T subtract(T n1, T n2);

    T multiply(T n1, T n2);

    T divide(T n1, T n2);

    T mod(T n1, T n2);

    int compare(T n1, T n2);

    T negate(T n);

    default T abs(T n) {
        return compare(n, valueOf(0)) < 0 ? negate(n) : n;
    }

    T valueOf(Number n);
}
