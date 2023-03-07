package expression.parser;

public abstract class BaseParser {
    private final CharSource source;
    private char ch;

    protected BaseParser(final CharSource source) {
        this.source = source;
        ch = source.next();
    }

    protected char peek() {
        return ch;
    }

    protected char take() {
        final char result = ch;
        ch = source.next();
        return result;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected boolean eof() {
        return test(CharSource.EOF);
    }

    protected void skipWhitespace() {
        while (!eof() && Character.isWhitespace(ch)) {
            take();
        }
    }

    protected ParsingException error(final String message) {
        return source.error(message);
    }

    private String checkEof(final String str) {
        return str.equals(String.valueOf(CharSource.EOF)) ? "end of input" : str;
    }

    protected ParsingException error(final String expected, final String got) {
        return source.error("Expected " + checkEof(expected) + ", got: " + checkEof(got));
    }
}
