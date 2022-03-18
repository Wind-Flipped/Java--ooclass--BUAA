import com.oocourse.spec3.ExprInput;
import com.oocourse.spec3.ExprInputMode;

public class Main {
    private static String expression;
    private static DiyFunction function1;
    private static DiyFunction function2;
    private static DiyFunction function3;

    public static void main(String[] args) {
        // 实例化一个ExprInput类型的对象scanner
        // 由于是一般读入模式，所以我们实例化时传递的参数为ExprInputMode.NormalMode
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        // 获取自定义函数个数
        int cnt = scanner.getCount();
        // 读入自定义函数
        for (int i = 0; i < cnt; i++) {
            String func = scanner.readLine();
            // 存储或者解析逻辑
            func = func.replaceAll("[ \\t]+", "");
            if (func.charAt(0) == 'f') {
                function1 = new DiyFunction(func);
            } else if (func.charAt(0) == 'g') {
                function2 = new DiyFunction(func);
            } else if (func.charAt(0) == 'h') {
                function3 = new DiyFunction(func);
            }
        }
        // 读入最后一行表达式
        String expr = scanner.readLine();
        // 表达式括号展开相关的逻辑
        simplify(expr);

        //        Parser parser;
        //        Expr exprAll;
        //        if (lexer.getCurToken().equals("+")) {
        //            lexer.readNext();
        //            parser = new Parser(lexer);
        //            exprAll = parser.parserExpr("+");
        //        } else if (lexer.getCurToken().equals("-")) {
        //            lexer.readNext();
        //            parser = new Parser(lexer);
        //            exprAll = parser.parserExpr("-");
        //        } else {
        //            parser = new Parser(lexer);
        //            exprAll = parser.parserExpr("+");
        //        }
        Lexer lexer = new Lexer(expression);
        Parser parser = new Parser(lexer);
        Expr exprAll = parser.parserExpr("+");
        System.out.println(exprAll.toString());
    }

    public static void simplify(String str) {
        expression = str.replaceAll("[ \t]+", "")
                .replaceAll("(\\+--)|(-\\+-)|(--\\+)", "+")
                .replaceAll("(-\\+\\+)|(\\+-\\+)|(\\+\\+-)", "-")
                .replaceAll("([+]{3})", "+")
                .replaceAll("([-]{3})", "-")
                .replaceAll("(\\+-)|(-\\+)", "-")
                .replaceAll("([+]{2})", "+")
                .replaceAll("([-]{2})", "+");

        //.replaceAll("(sin\\(.*?\\)|cos\\(.*?\\))", "($0)");

        //        String index = "((?<expr>\\(.*?\\))(?<sign>\\*\\*[+]?[0]*(?<mi>[\\d]+)))";
        //        StringBuilder stringBuilder = new StringBuilder();
        //        Pattern patternIndex = Pattern.compile(index);
        //        int start = 0;
        //        int end = 0;
        //        int endLast = end;
        //        Matcher matcherIndex = patternIndex.matcher(exp);
        //        while (matcherIndex.find()) {
        //            endLast = end;
        //            start = matcherIndex.start();
        //            end = matcherIndex.end();
        //            stringBuilder.append(exp, endLast, start);
        //            if (matcherIndex.group("mi").equals("0")) {
        //                stringBuilder.append("1");
        //            } else {
        //                StringBuilder expr = new StringBuilder(matcherIndex.group("expr"));
        //                for (int i = 1; i < Integer.parseInt(matcherIndex.group("mi")); ++i) {
        //                    expr.append("*");
        //                    expr.append(matcherIndex.group("expr"));
        //                }
        //                stringBuilder.append(expr);
        //            }
        //        }
        //        stringBuilder.append(str, end, str.length());
        //        expression = stringBuilder.toString();
    }

    public static DiyFunction getFunction1() {
        return function1;
    }

    public static DiyFunction getFunction2() {
        return function2;
    }

    public static DiyFunction getFunction3() {
        return function3;
    }
}
