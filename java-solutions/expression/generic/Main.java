package expression.generic;

import expression.parser.ParsingException;

import java.util.Set;

public class Main {
    private static final Set<String> modes = Set.of("-i", "-d", "-bi", "-u", "-l", "-s");

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Expected 2 arguments, got " + args.length);
            return;
        }
        final String mode = args[0];
        final String expression = args[1];
        if (!modes.contains(mode)) {
            System.out.println("Unknown mode: " + mode);
            System.out.println("Possible options:");
            for (String s : modes) {
                System.out.println(s);
            }
            return;
        }
        final GenericTabulator t = new GenericTabulator();
        try {
            final Object[][][] table = t.tabulate(
                    mode.substring(1), expression,
                    -2, 2, -2, 2, -2, 2
            );
            for (int x = -2; x <= 2; x++) {
                for (int y = -2; y <= 2; y++) {
                    for (int z = -2; z <= 2; z++) {
                        System.out.println("x = " + x + ", y = " + y + ", z = " + z + ": ");
                        System.out.println(table[x + 2][y + 2][z + 2]);
                        System.out.println();
                    }
                }
            }
        } catch (ParsingException e) {
            System.out.println(expression);
            System.out.println(e.getMessage());
        }
    }
}
