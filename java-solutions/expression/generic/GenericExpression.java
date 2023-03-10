package expression.generic;

public interface GenericExpression<T extends Number> {
    T evaluate(T x, T y, T z);
}
