package expression.generic;

public class LongParser implements NumberParser<Long> {
    @Override
    public Long parse(final String s) {
        return Long.parseLong(s);
    }
}
