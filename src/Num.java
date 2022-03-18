import java.math.BigInteger;
import java.util.HashMap;

public class Num extends Factor {
    private HashMap<HashMap<String,BigInteger>,BigInteger> factors;

    public Num(BigInteger value,String sign) {
        factors = new HashMap<>();
        HashMap<String,BigInteger> init = new HashMap<>();
        init.put("x",BigInteger.ZERO);
        if (sign.equals("+")) {
            factors.put(init,value);
        } else {
            factors.put(init,value.negate());
        }
    }

    public HashMap<HashMap<String, BigInteger>,BigInteger> getFactors() {
        return factors;
    }
}
