package expression.generic;

import java.math.BigInteger;

public class BigIntegerParser implements NumberParser<BigInteger> {
    @Override
    public BigInteger parse(final String s) {
        return new BigInteger(s);
    }
}