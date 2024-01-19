import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr(int s) {
        Expr expr = new Expr(1, s);
        for (Term term : parseTerm(1)) {
            expr.addTerm(term);
        }
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            int sign = (lexer.peek().equals("-")) ? -1 : 1;
            //lexer.next();
            for (Term term : parseTerm(1)) {
                expr.addTerm(term);
            }
        }
        return expr;
    }

    public ArrayList<Term> parseTerm(int sign) {
        Term term = new Term(sign);//新项
        Factor factor = parseFactor(1);
        term.addFactor(factor);//加入因子
        term.setSign(term.getSign() * factor.getSign());
        while (lexer.peek().equals("*")) {
            lexer.next();
            factor = parseFactor(1);
            term.addFactor(factor);
            term.setSign(term.getSign() * factor.getSign());
        }
        return term.expandTerms();
    }

    public Factor parseFactor(int outsign) {
        int sign = outsign;
        if (lexer.peek().equals("-")) {
            sign = -1;
            lexer.next();;
        } else if (lexer.peek().equals("+")) {
            lexer.next();
        }
        if (lexer.peek().equals("(")) {
            lexer.next();
            Expr expr = parseExpr(sign);
            lexer.next();
            if (lexer.peek().charAt(0) == '^') {
                lexer.next();
                int pow = lexer.peek().charAt(0) - '0';
                expr.setPow(pow);
                lexer.next();
            }
            return expr;
        } else if (lexer.peek().equals(("x")) || lexer.peek().equals("y")
                || lexer.peek().equals("z")) {
            Var var = new Var(lexer.peek(), 1, sign);
            lexer.next();
            if (lexer.peek().equals("^")) {
                lexer.next();
                int index = lexer.peek().charAt(0) - '0';
                var.setPow(index);
                lexer.next();
            }
            return var;
        } else {
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();

            return new Number(num, sign);
        }
    }
}
