package expression.generic;

import expression.Order;

import java.util.Objects;

public class GenericVariable implements GenericOperand {
    private final String name;

    public GenericVariable(final String name) {
        this.name = name;
    }

    @Override
    public int getOrder() {
        return Order.NUMBER.ordinal();
    }

    @Override
    public <T extends NumberType<T>> T evaluate(final T x, final T y, final T z) {
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
            return Objects.equals(this.name, ((GenericVariable) obj).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
