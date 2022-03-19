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
                .replaceAll("(x)\\*\\*2([^\\d]|$)", "$1*x$2")
                .replaceAll("(sin\\(|cos\\()x\\*x\\)","$1x**2)")
                //.replaceAll("^(-.*?)\\+(.*)", "$2$1")
                .replaceAll("^\\+(.*)", "$1");
        if (simplifyOutput.charAt(0) == '-') {
            simplifyOutput = reverse(simplifyOutput);
        }

        return simplifyOutput;
    }

    public String reverse(String str) {
        int brackets = 0;
        int pos = 0;
        int length = str.length();
        for (; pos < length; pos += 1) {
            if (str.charAt(pos) == '(') {
                brackets += 1;
            } else if (str.charAt(pos) == ')') {
                brackets -= 1;
            } else if (str.charAt(pos) == '+') {
                if (brackets == 0) {
                    return str.substring(pos + 1, length) + str.substring(0, pos);
                }
            }
        }
        return str;
    }

    public String simplifySquare(String str) {
        int brackets = 0;
        int pos = 0;
        int length = str.length();
        for (; pos < length; pos += 1) {
            if (str.charAt(pos) == '(') {
                brackets += 1;
            } else if (str.charAt(pos) == ')') {
                brackets -= 1;
            } else if (str.charAt(pos) == '+') {
                if (brackets == 0) {
                    return str.substring(pos + 1, length) + str.substring(0, pos);
                }
            }
        }
        return str;
    }

    public HashMap<HashMap<String, BigInteger>, BigInteger> getFactors() {
        return exprs;
    }
}
