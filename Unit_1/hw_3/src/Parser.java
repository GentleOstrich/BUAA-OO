import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.getTerms().addAll(parseTerm().getTerms());
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            expr.getTerms().addAll(parseTerm().getTerms());
        }
        return expr;
    }

    public Expr parseTerm() {
        Term term = new Term();//新项
        Factor factor = parseFactor();
        int pow = 1;
        if (lexer.peek().charAt(0) == '^') {
            lexer.next();
            pow = lexer.peek().charAt(0) - '0';
            lexer.next();
        }
        term.addFactor(factor, pow);//加入因子
        while (lexer.peek().equals("*")) {
            lexer.next();
            factor = parseFactor();
            pow = 1;
            if (lexer.peek().charAt(0) == '^') {
                lexer.next();
                pow = lexer.peek().charAt(0) - '0';
                lexer.next();
            }
            term.addFactor(factor, pow);
        }
        return term.expand();
    }

    public  Factor parseFactor() {
        int sign = 1;
        if (lexer.peek().equals("-")) {
            sign = -1;
            lexer.next();;
        } else if (lexer.peek().equals("+")) {
            lexer.next();
        }
        if (lexer.peek().equals("(")) {
            lexer.next();
            Expr expr = parseExpr();
            expr.setSign(sign);
            lexer.next();
            return expr;
        } else if (lexer.peek().equals(("x")) || lexer.peek().equals("y")
                || lexer.peek().equals("z")) {
            Var var = new Var(lexer.peek(), sign);
            lexer.next();
            return var;
        } else if (lexer.peek().equals(("sin"))) {
            lexer.next(); // 过掉左括号
            lexer.next();
            Expr expr = parseExpr().mergeExpr();
            if (expr.toString().equals("0") || expr.toString().equals("")) {
                lexer.next();
                return new Number(BigInteger.ZERO, sign);
            }
            Sin sin = new Sin(expr, sign);
            //lexer.next();
            lexer.next(); // 过掉右括号
            return sin;
        } else if (lexer.peek().equals(("cos"))) {
            lexer.next(); //
            lexer.next();
            Expr expr = parseExpr().mergeExpr();
            if (expr.toString().equals("0") || expr.toString().equals("")) {
                lexer.next();
                return new Number(BigInteger.ONE, sign);
            }
            Cos cos = new Cos(expr, sign);
            lexer.next(); // 过掉右括号
            return cos;
        } else if (lexer.peek().equals("dx") || lexer.peek().equals("dy")
                || lexer.peek().equals("dz")) {
            int needDerivation = lexer.peek().equals("dx") ? 1 :
                                lexer.peek().equals("dy") ? 2 :
                                lexer.peek().equals("dz") ? 3 : 0;
            lexer.next();
            lexer.next();
            Expr expr = parseExpr().derivation(needDerivation);
            expr.setSign(sign);
            lexer.next();
            return expr;
        } else {
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Number(num, sign);
        }
    }
}
