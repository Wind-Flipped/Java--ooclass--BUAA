import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiyFunction {
    private String function;
    private int num;
    private HashMap<Integer, String> vars;
    private String exp;
    //private String gfunction;
    //private String hfunction;

    public DiyFunction(String function) {
        this.function = function;
        vars = new HashMap<>();
        String[] strings = function.split("=");
        exp = strings[1];
        Pattern pattern1 = Pattern.compile("\\((?<var>.*?)\\)");
        Matcher matcher1 = pattern1.matcher(strings[0]);
        if (matcher1.find()) {
            String[] var = matcher1.group("var").split(",");
            num = var.length;
            for (int i = 1; i <= num; i += 1) {
                vars.put(i, var[i - 1]);
            }
        }
    }

    public Factor applyFunction(String string, String sign) {
        String newString = string.replaceAll("x", "a");
        Pattern pattern1 = Pattern.compile("^[fgh]\\((?<var>.*?)\\)$");
        Matcher matcher1 = pattern1.matcher(newString);
        String expression = exp;
        if (matcher1.find()) {
            //String[] var01 = matcher1.group("var").split(",");
            String[] var01 = getParameters(matcher1.group("var"),num);
            for (int i = 1; i <= num; i += 1) {
                String var02 = vars.get(i);
                expression = expression.replaceAll(var02, var01[i - 1]);
            }
            expression = expression.replaceAll("a", "x");
        }
        expression = simplify(expression);
        Lexer lexer = new Lexer(expression);
        return new Parser(lexer).parserExpr(sign);/*
        Expr exp;
        if ((lexer.getCurToken().equals("+") && sign.equals("+"))
                || (lexer.getCurToken().equals("-") && sign.equals("-"))) {
            lexer.readNext();
            Parser parser = new Parser(lexer);
            exp = parser.parserExpr("+");
        } else if ((lexer.getCurToken().equals("-") && sign.equals("+"))
                || (lexer.getCurToken().equals("+") && sign.equals("-"))) {
            lexer.readNext();
            Parser parser = new Parser(lexer);
            exp = parser.parserExpr("-");
        } else {
            Parser parser = new Parser(lexer);
            exp = parser.parserExpr(sign);
        }
        return exp;*/
    }

    public String simplify(String str) {
        return str.replaceAll("[ \t]+", "")
                .replaceAll("(\\+--)|(-\\+-)|(--\\+)", "+")
                .replaceAll("(-\\+\\+)|(\\+-\\+)|(\\+\\+-)", "-")
                .replaceAll("([+]{3})", "+")
                .replaceAll("([-]{3})", "-")
                .replaceAll("(\\+-)|(-\\+)", "-")
                .replaceAll("([+]{2})", "+")
                .replaceAll("([-]{2})", "+");
    }
    //    public void setGfunction(String gfunction) {
    //        this.gfunction = gfunction;
    //    }
    //
    //    public void setHfunction(String hfunction) {
    //        this.hfunction = hfunction;
    //    }

    public String[] getParameters(String str,int num) {
        int brackets = 0;
        int length = str.length();
        String[] parameters = new String[num];
        int lastPos = 0;
        int i = 0;
        int curPos = 0;
        for (; curPos < length; curPos += 1) {
            if (str.charAt(curPos) == ',' && brackets == 0) {
                parameters[i] = str.substring(lastPos,curPos);
                i += 1;
                lastPos = curPos + 1;
            } else if (str.charAt(curPos) == '(') {
                brackets += 1;
            } else if (str.charAt(curPos) == ')') {
                brackets -= 1;
            }
        }
        parameters[i] = str.substring(lastPos, curPos);
        return parameters;
    }
}
