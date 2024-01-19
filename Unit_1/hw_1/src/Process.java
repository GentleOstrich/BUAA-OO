public class Process {
    private int pos;

    public Process() {
        pos = 0;
    }

    public String preProcess(String unprocessed) {
        String processed = unprocessed.replace("\t", "").replace(" ", "");
        while (processed.contains("--")) {
            processed = processed.replace("--", "+");
        }
        while (processed.contains("++")) {
            processed = processed.replace("++", "+");
        }
        while (processed.contains("+-")) {
            processed = processed.replace("+-", "-");
        }
        while (processed.contains("-+")) {
            processed = processed.replace("-+", "-");
        }
        while (processed.contains("**")) {
            processed = processed.replace("**", "^");
        }
        StringBuilder sb = new StringBuilder();
        while (pos < processed.length()) {
            if (processed.charAt(pos) == '0') {
                if (pos < processed.length() - 1 && pos > 0 &&
                        !Character.isDigit(processed.charAt(pos - 1)) &&
                        Character.isDigit(processed.charAt(pos + 1))) {
                    while (pos < processed.length() - 1 && processed.charAt(pos) == '0' &&
                            Character.isDigit(processed.charAt(pos + 1))) {
                        ++pos;
                    }
                }
            }
            sb.append(processed.charAt(pos));
            ++pos;
        }
        return sb.toString().replace(" ", "").replace("**", "^").
                replace("++", "+").replace("--", "+").
                replace("+-", "-").replace("-+", "-").replace("(y)", "y");
    }

    public Term termMultTerm(Term term1, Term term2) {
        Term term = new Term(1);
        for (Factor factor : term1.getFactors()) {
            term.addFactor(factor);
            term.setSign(term.getSign() * factor.getSign());
        }
        for (Factor factor : term2.getFactors()) {
            term.addFactor(factor);
            term.setSign(term.getSign() * factor.getSign());
        }
        return term;
    }

    public Expr exprMultiNumber(Expr expr, Number number) {
        if (expr.getTerms().size() == 0) {
            Term term = new Term(number.getSign());
            term.addFactor(number);
            expr.addTerm(term);
            return expr;
        }
        for (Term term : expr.getTerms()) {
            term.addFactor(number);
            term.setSign(term.getSign() * number.getSign());
        }
        return expr;
    }

    public Expr exprMultiExpr(Expr expr1, Expr expr2) {
        Expr expr = new Expr(1, 1);
        expr.setSign(expr2.getSign() * expr1.getSign());
        if (expr1.getTerms().size() == 0) {
            for (Term term2 : expr2.getTerms()) {
                expr.addTerm(term2);
            }
        } else {
            for (Term term1 : expr1.getTerms()) {
                for (Term term2 : expr2.getTerms()) {
                    expr.addTerm(termMultTerm(term1, term2));
                }
            }
        }
        return expr;
    }

    public Expr exprMultiVar(Expr expr, Var var) {
        if (expr.getTerms().size() == 0) {
            Term term = new Term(var.getSign());
            term.addFactor(var);
            expr.addTerm(term);
            return expr;
        }
        for (Term term : expr.getTerms()) {
            term.addFactor(var);
            term.setSign(term.getSign() * var.getSign());
        }
        return expr;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
