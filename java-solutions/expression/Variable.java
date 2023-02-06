package expression;

import java.util.Objects;

public class Variable implements Operand {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public int getOrder() {
        return Order.NUMBER.ordinal();
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public double evaluate(double x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
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
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == Variable.class) {
            return Objects.equals(this.name, ((Variable) obj).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
