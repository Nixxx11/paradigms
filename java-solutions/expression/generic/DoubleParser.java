package expression.generic;

public class DoubleParser implements NumberParser<Double> {
    @Override
    public Double parse(final String s) {
        return Double.parseDouble(s);
    }
}