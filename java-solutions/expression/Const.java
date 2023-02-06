package expression;

public class Const implements Operand {
    private final Number value;

    public Const(final int value) {
        this.value = value;
    }

    public Const(final double value) {
        this.value = value;
    }

    @Override
    public int getOrder() {
        return Order.NUMBER.ordinal();
    }

    @Override
    public int evaluate(int x) {
        return value.intValue();
    }

    @Override
    public double evaluate(double x) {
        return value.doubleValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == Const.class) {
            return value.doubleValue() == ((Const) obj).value.doubleValue();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.intValue() + (int) ((value.doubleValue() - value.intValue()) * Integer.MAX_VALUE);
    }
}
