package expression.generic;

public class IntegerParser implements NumberParser<Integer>{
    @Override
    public Integer parse(final String s) {
        return Integer.parseInt(s);
    }
}