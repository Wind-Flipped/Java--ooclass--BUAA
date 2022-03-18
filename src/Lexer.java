import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private int pos;
    private final int length;
    private final String line;
    private String curToken;
    private String type;

    private final String num = "^[0]*(?<num>[\\d]+)";
    private final String var = "^(?<var>x(\\*\\*[+]?[0]*(?<mi>[\\d]+))?)";
    private final String tri = "^(?<tri>(sin|cos))";
    private final String fgh = "^[fgh]";
    private final String sum = "^sum";
    private final String mi = "^\\*\\*";
    private final String triMi = "^\\*\\*[+]?[0]*(?<mi>[\\d]+)?";
    private final Pattern patternNum = Pattern.compile(num);
    private final Pattern patternVar = Pattern.compile(var);
    private final Pattern patternTri = Pattern.compile(tri);
    private final Pattern patternFgh = Pattern.compile(fgh);
    private final Pattern patternSum = Pattern.compile(sum);
    private final Pattern patternMi = Pattern.compile(mi);
    private final Pattern patternTrimi = Pattern.compile(triMi);

    public Lexer(String line) {
        this.line = line;
        length = line.length();
        pos = 0;
        type = "sign";
        readNext();
        //factor = new Factor();
    }

    public void readNext() {
        if (pos == length) {
            return;
        }
        String curStr = line.substring(pos);

        Matcher matcherNum = patternNum.matcher(curStr);
        Matcher matcherVar = patternVar.matcher(curStr);
        Matcher matcherTri = patternTri.matcher(curStr);
        Matcher matcherFgh = patternFgh.matcher(curStr);
        Matcher matcherSum = patternSum.matcher(curStr);
        Matcher matcherMi = patternMi.matcher(curStr);

        if (matcherNum.find()) {
            //factor = new Num(new BigInteger(matcherNum.group(0)));
            curToken = matcherNum.group("num");
            type = "num";
            pos += matcherNum.group(0).length();
        } else if (matcherVar.find()) {
            curToken = matcherVar.group("var");
            type = "var";
            pos += curToken.length();
        } else if (matcherTri.find()) {
            curToken = matcherTri.group(0);// sin|cos
            type = "tri";
            pos += curToken.length() + 1;
            curToken = curToken + "(" + getBrackets() + ")";
            curStr = line.substring(pos);
            matcherMi = patternTrimi.matcher(curStr);
            if (matcherMi.find()) {
                curToken = curToken + matcherMi.group(0);
                pos += matcherMi.group(0).length();
            }
        } else if (matcherFgh.find()) {
            curToken = matcherFgh.group(0);
            type = "fgh";
            pos += curToken.length() + 1;
            curToken = curToken + "(" + getBrackets() + ")";
        } else if (matcherSum.find()) {
            type = "sum";
            pos += matcherSum.group(0).length();
            pos += 1;
            curToken = getBrackets();
        } else if (matcherMi.find()) {
            curToken = "**";
            type = "mi";
            pos += 2;
        }
        else {
            curToken = String.valueOf(line.charAt(pos));
            type = "sign";
            pos += 1;
        }
    }

    public String getCurToken() {
        return curToken;
    }

    public String getType() {
        return type;
    }

    public String getBrackets() {
        int brackets = 1;
        StringBuilder stringBuilder = new StringBuilder();
        while (brackets != 0) {
            char ch = line.charAt(pos);
            pos += 1;
            if (ch == '(') {
                brackets += 1;
            } else if (ch == ')') {
                brackets -= 1;
            }
            if (brackets != 0) {
                stringBuilder.append(ch);
            }
        }
        return stringBuilder.toString();
    }
}
