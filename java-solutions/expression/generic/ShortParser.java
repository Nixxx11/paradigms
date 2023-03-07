package expression.generic;

public class ShortParser implements NumberParser<Short> {
    @Override
    public Short parse(final String s) {
        return Short.parseShort(s);
    }
}
