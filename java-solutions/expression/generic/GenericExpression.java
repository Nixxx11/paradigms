package expression.generic;

import expression.ToMiniString;

public interface GenericExpression<T extends Number> extends ToMiniString {
    int getOrder();

    T evaluate(T x, T y, T z, Arithmetic<T> a);
}
