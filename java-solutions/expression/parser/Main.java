package expression.parser;

import expression.TripleExpression;

public class Main {
    public static void main(String[] args) {
        TripleParser parser = new ExpressionParser();
        TripleExpression expression = parser.parse("x * (x - 2)*x + 1");
        System.out.println(expression.toString());
        System.out.println(expression.toMiniString());
        System.out.println(expression.evaluate(3, 0, 0));
    }
}
