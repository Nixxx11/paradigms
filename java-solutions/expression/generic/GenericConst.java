package expression.generic;

import expression.Order;

public class GenericConst<T extends Number> implements GenericExpression<T> {
    private final T value;

    public GenericConst(final T value) {
        this.value = value;
    }

    @Override
    public int getOrder() {
        return Order.NUMBER.ordinal();
    }

    @Override
    public T evaluate(final T x, final T y, final T z, final Arithmetic<T> a) {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj != null && obj.getClass() == GenericConst.class) {
            return value.equals(((GenericConst<?>) obj).value.doubleValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
