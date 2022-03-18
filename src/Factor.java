import java.math.BigInteger;
import java.util.HashMap;

public class Factor {
    private HashMap<HashMap<String, BigInteger>,BigInteger> factors;

    public Factor() {
        factors = new HashMap<>();
    }

    public HashMap<HashMap<String, BigInteger>,BigInteger> getFactors() {
        return factors;
    }
}
