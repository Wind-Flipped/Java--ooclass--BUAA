import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parserExpr(String flag) {
        //TODO 如何考虑 +-(-
        Expr expr = new Expr();
        String sign = lexer.getCurToken();
        Term term;
        if ((flag.equals("+") && sign.equals("+")) || (flag.equals("-") && sign.equals("-"))) {
            lexer.readNext();
            term = parserTerm();
            expr.addTerm(term);
        } else if ((flag.equals("-") && sign.equals("+")) ||
                (flag.equals("+") && sign.equals("-"))) {
            lexer.readNext();
            term = parserTerm();
            expr.addTermNeg(term);
        } else {
            term = parserTerm();
            expr.addTerm(term);
        }
        while (true) {
            sign = lexer.getCurToken();
            if (sign.equals(")")) {
                break;
            }
            lexer.readNext();
            term = parserTerm();
            if ((sign.equals("+") && flag.equals("+")) ||
                    (sign.equals("-") && flag.equals("-"))) {
                expr.addTerm(term);
            } else if ((sign.equals("+") && flag.equals("-")) ||
                    (sign.equals("-") && flag.equals("+"))) {
                expr.addTermNeg(term);
            } else {
                //TODO 如何考虑退出条件
                break;
            }
        }
        return expr;
    }

    public Term parserTerm() {
        Term term = new Term();
        Factor factor = parserFactor("+");
        if (lexer.getCurToken().equals("**")) {
            lexer.readNext();
            if (lexer.getCurToken().equals("+")) {
                lexer.readNext();
            }
            if (lexer.getCurToken().equals("0")) {
                lexer.readNext();
            } else {
                int n = Integer.parseInt(lexer.getCurToken());
                for (int i = 0; i < n; i += 1) {
                    term.addFactor(factor);
                }
                lexer.readNext();
            }
        } else {
            term.addFactor(factor);
        }
        while (lexer.getCurToken().equals("*")) {
            lexer.readNext();
            if (lexer.getCurToken().equals("+")) {
                lexer.readNext();
                factor = parserFactor("+");
                //lexer.readNext();
            } else if (lexer.getCurToken().equals("-")) {
                lexer.readNext();
                factor = parserFactor("-");
                //lexer.readNext();
            } else {
                factor = parserFactor("+");
                //lexer.readNext();
            }
            //lexer.readNext();
            if (lexer.getCurToken().equals("**")) {
                lexer.readNext();
                if (lexer.getCurToken().equals("+")) {
                    lexer.readNext();
                }
                if (lexer.getCurToken().equals("0")) {
                    lexer.readNext();
                } else {
                    int n = Integer.parseInt(lexer.getCurToken());
                    for (int i = 0; i < n; i += 1) {
                        term.addFactor(factor);
                    }
                    lexer.readNext();
                }
            } else {
                term.addFactor(factor);
            }
        }
        return term;
    }

    public Factor parserFactor(String sign) {
        String str = lexer.getCurToken();//表示当前因子
        Pattern patternmi = Pattern.compile("^x\\*\\*[+]?[0]*(?<mi>\\d+)");
        Matcher matchermi = patternmi.matcher(str);
        Pattern patterntri = Pattern.compile("^(?<tri>(sin\\(.*?\\)|cos\\(.*?\\)))" +
                "(\\*\\*[+]?[0]*(?<mi>[\\d]+))?$");
        Matcher matchertri = patterntri.matcher(str);
        if (str.equals("(")) {
            lexer.readNext();
            Factor expr = parserExpr(sign);
            lexer.readNext();
            return expr;
        } else if (str.equals("x")) {
            lexer.readNext();
            return new Var(BigInteger.ONE, sign);
        } else if (matchermi.find()) {
            lexer.readNext();
            return new Var(new BigInteger(matchermi.group("mi")), sign);
        } else if (matchertri.find()) {
            try {
                lexer.readNext();
                return new Tri(matchertri.group("tri"),
                        new BigInteger(matchertri.group("mi")), sign);
            } catch (Exception e) {
                return new Tri(matchertri.group("tri"), BigInteger.ONE, sign);
            }
        } else if (lexer.getType().equals("fgh")) {
            if (lexer.getCurToken().charAt(0) == 'f') {
                Factor factor = Main.getFunction1().applyFunction(lexer.getCurToken(), sign);
                lexer.readNext();
                return factor;
            } else if (lexer.getCurToken().charAt(0) == 'g') {
                Factor factor = Main.getFunction2().applyFunction(lexer.getCurToken(), sign);
                lexer.readNext();
                return factor;
            } else if (lexer.getCurToken().charAt(0) == 'h') {
                Factor factor = Main.getFunction3().applyFunction(lexer.getCurToken(), sign);
                lexer.readNext();
                return factor;
            } else {
                return new Num(BigInteger.ONE, sign);
            }
        } else if (lexer.getType().equals("sum")) {
            Sum sum = new Sum(str, sign);
            lexer.readNext();
            return sum.applySum();
        } else {
            lexer.readNext();
            return new Num(new BigInteger(str), sign);
        }
    }
}
