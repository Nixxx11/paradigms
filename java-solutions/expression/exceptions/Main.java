package expression.exceptions;

import expression.TripleExpression;
import expression.parser.ParsingException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Expected 4 arguments, got " + args.length);
        }
        ExpressionParser parser = new ExpressionParser();
        TripleExpression expr;
        try {
            expr = parser.parse(args[0]);
        } catch (ParsingException e) {
            System.out.println("Invalid expression: " + e.getMessage());
            return;
        }
        try {
            System.out.println(expr.evaluate(Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3])
            ));
        } catch (ArithmeticException e) {
            System.out.println("Exception while evaluating: " + e.getMessage());
        }
    }
}
