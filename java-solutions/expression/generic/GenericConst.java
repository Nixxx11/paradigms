package expression.generic;

public class GenericConst<T extends Number> implements GenericExpression<T> {
    private final T value;

    public GenericConst(final T value) {
        this.value = value;
    }

    @Override
    public T evaluate(final T x, final T y, final T z) {
        return value;
    }
}
