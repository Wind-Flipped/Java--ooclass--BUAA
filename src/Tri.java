import java.math.BigInteger;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tri extends Factor {
    private HashMap<HashMap<String, BigInteger>, BigInteger> factors;
    private String expression;

    private final String triMode = "^(sin|cos)\\((?<content>.*?)\\)$";
    private final Pattern patternTriMode = Pattern.compile(triMode);
    //get inner expr
    private final String num = "^[+-]?\\d*?$";
    private final Pattern patternNum = Pattern.compile(num);
    //match numFactor
    private final String var = "^x(\\*\\*\\d*)?$";
    private final Pattern patternVar = Pattern.compile(var);
    //match varFactor
    private final String tri = "^(sin|cos)\\(.*?\\)(\\*\\*\\d*)?$";
    private final Pattern patternTri = Pattern.compile(tri);
    //match triFactor

    public Tri(String str, BigInteger index, String sign) {
        factors = new HashMap<>();
        HashMap<String, BigInteger> init = new HashMap<>();
        if (index.compareTo(BigInteger.ZERO) == 0) {
            factors.put(init, BigInteger.ONE);
            return;
        }
        Matcher matcherTriMode = patternTriMode.matcher(str);
        if (matcherTriMode.find()) {
            simplify(matcherTriMode.group("content"));
        }
        Lexer lexer = new Lexer(expression);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parserExpr("+");
        expression = expr.toString();
        Matcher matcherNum = patternNum.matcher(expression);
        Matcher matcherVar = patternVar.matcher(expression);
        Matcher matcherTri = patternTri.matcher(expression);
        if (matcherTri.find() || matcherNum.find() || matcherVar.find()) {
            expression = str.substring(0, 3) + "(" + expression + ")";
        } else {
            expression = str.substring(0, 3) + "((" + expression + "))";
        }
        //TODO 将三角函数表达式化简（还是化成字符串吧）
        //将三角函数表达式化简后只剩0时
        if (expression.charAt(4) == '0') {
            String prefix = expression.substring(0, 3);
            if (prefix.equals("sin")) {
                factors.put(init, BigInteger.ZERO);
            } else {
                factors.put(init, BigInteger.ONE);
            }
            return;
        }
        init.put(expression, index);
        if (sign.equals("+")) {
            factors.put(init, BigInteger.ONE);
        } else {
            factors.put(init, BigInteger.ONE.negate());
        }
    }

    public void simplify(String str) {
        expression = str.replaceAll("[ \t]+", "")
                .replaceAll("(\\+--)|(-\\+-)|(--\\+)", "+")
                .replaceAll("(-\\+\\+)|(\\+-\\+)|(\\+\\+-)", "-")
                .replaceAll("([+]{3})", "+")
                .replaceAll("([-]{3})", "-")
                .replaceAll("(\\+-)|(-\\+)", "-")
                .replaceAll("([+]{2})", "+")
                .replaceAll("([-]{2})", "+");
    }

    public HashMap<HashMap<String, BigInteger>, BigInteger> getFactors() {
        return factors;
    }
}
