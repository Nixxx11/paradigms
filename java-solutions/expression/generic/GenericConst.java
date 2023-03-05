package expression.generic;

import expression.Order;

public class GenericConst implements GenericOperand {
    private final Number value;

    public GenericConst(final int value) {
        this.value = value;
    }

    @Override
    public int getOrder() {
        return Order.NUMBER.ordinal();
    }

    @Override
    public <T extends NumberType<T>> T evaluate(final T x, final T y, final T z) {
        return x.valueOf(value.intValue());
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
            return value.doubleValue() == ((GenericConst) obj).value.doubleValue();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.intValue() + (int) ((value.doubleValue() - value.intValue()) * Integer.MAX_VALUE);
    }
}
