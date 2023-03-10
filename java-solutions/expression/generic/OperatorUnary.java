package expression.generic;

public interface OperatorUnary<T extends Number> {
    T calculate(T n);
}
