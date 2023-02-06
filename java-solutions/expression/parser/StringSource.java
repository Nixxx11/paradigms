package expression.parser;

public class StringSource implements CharSource {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public char next() {
        return pos < data.length() ? data.charAt(pos++) : CharSource.EOF;
    }

    @Override
    public ParsingException error(final String message) {
        return new ParsingException(pos + ": " + message);
    }
}
