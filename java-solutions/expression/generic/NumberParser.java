package expression.generic;

public interface NumberParser<T extends Number> {
    T parse(String s);
}