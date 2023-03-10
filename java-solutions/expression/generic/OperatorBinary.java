package expression.generic;

public interface OperatorBinary<T extends Number> {
    T calculate(T n1, T n2);
}
