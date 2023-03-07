package expression.generic;

public class IntegerArithmetic implements Arithmetic<Integer> {
    @Override
    public Integer add(final Integer n1, final Integer n2) {
        return n1 + n2;
    }

    @Override
    public Integer subtract(final Integer n1, final Integer n2) {
        return n1 - n2;
    }

    @Override
    public Integer multiply(final Integer n1, final Integer n2) {
        return n1 * n2;
    }

    @Override
    public Integer divide(final Integer n1, final Integer n2) {
        return n1 / n2;
    }

    @Override
    public Integer mod(final Integer n1, final Integer n2) {
        return n1 % n2;
    }

    @Override
    public Integer negate(final Integer n) {
        return -n;
    }

    @Override
    public int compare(final Integer n1, final Integer n2) {
        return Integer.compare(n1 ,n2);
    }

    @Override
    public Integer valueOf(final Number n) {
        return n.intValue();
    }

    @Override
    public Integer parse(final String s) {
        return Integer.parseInt(s);
    }
}
