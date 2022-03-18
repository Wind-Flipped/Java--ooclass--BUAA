import java.math.BigInteger;
import java.util.HashMap;

public class Var extends Factor {
    private HashMap<HashMap<String,BigInteger>,BigInteger> factors;

    public Var(BigInteger index, String sign) {
        factors = new HashMap<>();
        HashMap<String,BigInteger> init = new HashMap<>();
        init.put("x",index);
        if (sign.equals("+")) {
            factors.put(init,BigInteger.ONE);
        } else {
            factors.put(init,BigInteger.ONE.negate());
        }
    }

    public HashMap<HashMap<String, BigInteger>,BigInteger> getFactors() {
        return factors;
    }
}
