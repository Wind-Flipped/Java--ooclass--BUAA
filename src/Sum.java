import java.math.BigInteger;

public class Sum extends Factor {
    private String line;
    private BigInteger start;
    private BigInteger end;
    private String str;
    private String sign;

    public Sum(String line, String sign) {
        this.line = line;
        this.sign = sign;
        String[] parameters = line.split(",");
        parameters[1] = parameters[1].replaceAll("[+]?(?<num>\\d+)", "$1");
        parameters[1] = parameters[1].replaceAll("[0]*(?<num>\\d+)", "$1");
        parameters[2] = parameters[2].replaceAll("[+]?(?<num>\\d+)", "$1");
        parameters[2] = parameters[2].replaceAll("[0]*(?<num>\\d+)", "$1");
        start = new BigInteger(parameters[1]);
        end = new BigInteger(parameters[2]);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("0");
        if (start.compareTo(end) > 0) {
            str = "0";
            return;
        }
        for (; start.compareTo(end) <= 0; start = start.add(BigInteger.ONE)) {
            stringBuilder.append("+");
            //stringBuilder.append("(");
            String var = parameters[3].replaceAll("sin","abc");
            var = var.replaceAll("i", "(" + start + ")");
            var = var.replaceAll("abc","sin");
            stringBuilder.append(var);
            //stringBuilder.append(")");
        }
        str = stringBuilder.toString();
        str = simplify(str);
    }

    public String simplify(String str) {
        return str.replaceAll("[ \t]+", "")
                .replaceAll("(\\+--)|(-\\+-)|(--\\+)", "+")
                .replaceAll("(-\\+\\+)|(\\+-\\+)|(\\+\\+-)", "-")
                .replaceAll("([+]{3})", "+")
                .replaceAll("([-]{3})", "-")
                .replaceAll("(\\+-)|(-\\+)", "-")
                .replaceAll("([+]{2})", "+")
                .replaceAll("([-]{2})", "+")
                .replaceAll("\\(\\+", "(0+")
                .replaceAll("\\(-", "(0-");
    }

    public Factor applySum() {
        Lexer lexer = new Lexer(str);
        Parser parser = new Parser(lexer);
        return parser.parserExpr(sign);
    }
}
