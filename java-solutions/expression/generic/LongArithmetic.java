package expression.generic;

public class LongArithmetic implements Arithmetic<Long>{
    @Override
    public Long add(final Long n1, final Long n2) {
        return n1 + n2;
    }

    @Override
    public Long subtract(final Long n1, final Long n2) {
        return n1 - n2;
    }

    @Override
    public Long multiply(final Long n1, final Long n2) {
        return n1 * n2;
    }

    @Override
    public Long divide(final Long n1, final Long n2) {
        return n1 / n2;
    }

    @Override
    public Long mod(final Long n1, final Long n2) {
        return n1 % n2;
    }

    @Override
    public Long negate(final Long n) {
        return -n;
    }

    @Override
    public int compare(final Long n1, final Long n2) {
        return Long.compare(n1, n2);
    }

    @Override
    public Long valueOf(final Number n) {
        return n.longValue();
    }

    @Override
    public Long parse(final String s) {
        return Long.parseLong(s);
    }
}
