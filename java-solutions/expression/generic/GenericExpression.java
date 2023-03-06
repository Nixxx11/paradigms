package expression.generic;

import expression.ToMiniString;

public interface GenericExpression extends ToMiniString {
    int getOrder();

    <T extends Number> T evaluate(T x, T y, T z, Arithmetic<T> a);
}
