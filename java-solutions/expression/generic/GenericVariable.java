package expression.generic;

public class GenericVariable<T extends Number> implements GenericExpression<T> {
    private final String name;

    public GenericVariable(final String name) {
        this.name = name;
    }

    @Override
    public T evaluate(final T x, final T y, final T z) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new IllegalStateException("One of the variables is not named properly");
        };
    }
}
