package expression.parser;

public interface CharSource {
    char EOF = '\0';

    char next();

    ParsingException error(String message);
}