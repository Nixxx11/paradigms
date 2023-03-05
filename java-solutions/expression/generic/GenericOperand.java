package expression.generic;

import expression.ToMiniString;

public interface GenericOperand extends ToMiniString {
    int getOrder();

    <T extends NumberType<T>> T evaluate(T x, T y, T z);
}
