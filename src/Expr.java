import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Expr extends Factor {
    private HashMap<HashMap<String, BigInteger>, BigInteger> exprs;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Expr expr = (Expr) o;
        return Objects.equals(exprs, expr.exprs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exprs);
    }

    public Expr() {
        exprs = new HashMap<>();
        HashMap<String, BigInteger> init = new HashMap<>();
        init.put("x", BigInteger.ZERO);
        exprs.put(init, BigInteger.ZERO);
    }

    public void addTerm(Term term) {
        HashMap<HashMap<String, BigInteger>, BigInteger> newExprs = new HashMap<>(exprs);
        for (Map.Entry<HashMap<String, BigInteger>, BigInteger> en
                : term.getTerms().entrySet()) {
            if (exprs.containsKey(en.getKey())) {
                newExprs.put(en.getKey(), newExprs.get(en.getKey()).add(en.getValue()));
            } else {
                newExprs.put(en.getKey(), en.getValue());
            }
        }
        exprs = newExprs;
    }

    public void addTermNeg(Term term) {
        HashMap<HashMap<String, BigInteger>, BigInteger> newExprs = new HashMap<>(exprs);
        for (Map.Entry<HashMap<String, BigInteger>, BigInteger> entry
                : term.getTerms().entrySet()) {
            if (exprs.containsKey(entry.getKey())) {
                newExprs.put(entry.getKey(),
                        newExprs.get(entry.getKey()).subtract(entry.getValue()));
            } else {
                newExprs.put(entry.getKey(), entry.getValue().negate());
            }
        }
        exprs = newExprs;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("0");
        for (Map.Entry<HashMap<String, BigInteger>, BigInteger> entry : exprs.entrySet()) {
            BigInteger coe = entry.getValue();
            HashMap<String, BigInteger> hashMap = entry.getKey();
            if (coe.compareTo(BigInteger.ZERO) != 0) {
                if (coe.compareTo(BigInteger.ZERO) > 0) {
                    output.append("+");
                }
                output.append(coe);
                for (Map.Entry<String, BigInteger> entry1 : hashMap.entrySet()) {
                    BigInteger index = entry1.getValue();
                    String str = entry1.getKey();
                    if (index.compareTo(BigInteger.ZERO) != 0) {
                        if (index.compareTo(BigInteger.ONE) != 0) {
                            output.append("*");
                            output.append(str);
                            output.append("**");
                            output.append(index);
                        } else {
                            output.append("*");
                            output.append(str);
                        }
                    }
                }
            }
        }
        String simplifyOutput = output.toString();
        simplifyOutput = simplifyOutput.replaceAll("0(.+)", "$1")
                .replaceAll("([^\\d])1\\*", "$1")
                //.replaceAll("(x)\\*\\*2([^\\d])", "$1*x$2")
                //.replaceAll("^(-.*?)\\+(.*)", "$2$1")
                .replaceAll("^\\+(.*)", "$1");
        return simplifyOutput;
    }

    public HashMap<HashMap<String, BigInteger>, BigInteger> getFactors() {
        return exprs;
    }
}
