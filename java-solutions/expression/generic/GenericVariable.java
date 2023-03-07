package expression.generic;

import expression.Order;

public class GenericVariable<T extends Number> implements GenericExpression<T> {
    private final String name;

    public GenericVariable(final String name) {
        this.name = name;
    }

    @Override
    public int getOrder() {
        return Order.NUMBER.ordinal();
    }

    @Override
    public T evaluate(final T x, final T y, final T z, final Arithmetic<T> a) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new IllegalStateException("One of the variables is not named properly");
        };
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj != null && obj.getClass() == GenericVariable.class) {
            return name.equals(((GenericVariable<?>) obj).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
