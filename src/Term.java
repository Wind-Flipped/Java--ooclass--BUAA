import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Term {
    private HashMap<HashMap<String, BigInteger>, BigInteger> terms;

    public Term() {
        terms = new HashMap<>();
        HashMap<String, BigInteger> init = new HashMap<>();
        init.put("x", BigInteger.ZERO);
        terms.put(init, BigInteger.ONE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Term term = (Term) o;
        return terms.equals(term.terms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terms);
    }

    public void addFactor(Factor factor) {
        //做乘法
        HashMap<HashMap<String, BigInteger>, BigInteger> newTerms = new HashMap<>();
        for (Map.Entry<HashMap<String, BigInteger>, BigInteger> entry1 : terms.entrySet()) {
            for (Map.Entry<HashMap<String, BigInteger>, BigInteger> entry2
                    : factor.getFactors().entrySet()) {
                HashMap<String, BigInteger> init = new HashMap<>(entry1.getKey());
                for (Map.Entry<String, BigInteger> entryIterator1 : entry2.getKey().entrySet()) {
                    int flag = 0;
                    for (Map.Entry<String, BigInteger> entryIterator2
                            : entry1.getKey().entrySet()) {
                        if (entryIterator1.getKey().equals(entryIterator2.getKey())) {
                            flag = 1;
                            init.put(entryIterator1.getKey(),
                                    entryIterator1.getValue().add(entryIterator2.getValue()));
                            break;
                        }
                    }
                    if (flag == 0) {
                        init.put(entryIterator1.getKey(), entryIterator1.getValue());
                    }
                }
                if (newTerms.containsKey(init)) {
                    newTerms.put(init,
                            newTerms.get(init).add(entry1.getValue().multiply(entry2.getValue())));
                } else {
                    newTerms.put(init, entry1.getValue().multiply(entry2.getValue()));
                }
            }
        }
        terms = newTerms;
    }

    public HashMap<HashMap<String, BigInteger>, BigInteger> getTerms() {
        return terms;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        terms.forEach((hashMap, coe) -> {
            output.append("+");
            output.append(coe);
            hashMap.forEach((str, index) -> {
                output.append("*");
                output.append(str);
                output.append("**");
                output.append(index);
            });
        });
        return output.toString();
    }
}
