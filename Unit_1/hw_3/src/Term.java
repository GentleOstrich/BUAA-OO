import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;

public class Term {
    private BigInteger coefficient;
    private int coefficientSign;
    private final HashMap<Factor, Integer> factors;

    public Term() {
        this.coefficient = BigInteger.ONE;
        this.coefficientSign = 1;
        this.factors = new HashMap<>();
    }

    public void addFactor(Factor factor, int pow) {
        if (pow == 0) {
            this.coefficientSign = this.coefficientSign * factor.getSign();
        } else if (factor instanceof Expr) {
            for (int i = 1; i < pow; ++i) {
                Expr exprCopy = new Expr();
                for (Term term : ((Expr) factor).getTerms()) {
                    Term termCopy = new Term();
                    for (Factor factor1 : term.getFactors().keySet()) {
                        termCopy.addFactor(factor1, term.factors.get(factor1));
                    }
                    termCopy.setCoefficient(term.getCoef());
                    termCopy.setCoefficientSign(term.getCoefficientSign());
                    exprCopy.addTerm(termCopy);
                }
                this.factors.put(exprCopy, 1);
            }
            this.factors.put(factor, 1);
        } else {
            boolean has = false;
            if (factor instanceof Number) {
                if (((Number) factor).getNum().equals(BigInteger.ZERO)) {
                    this.coefficient = BigInteger.ZERO;
                }
                this.coefficient = this.coefficient.multiply(((Number) factor).getNum());
                this.coefficientSign = this.coefficientSign * factor.getSign();
                has = true; } else if (factor instanceof Var) {
                if (!this.factors.isEmpty()) {
                    for (Factor factor1 : this.factors.keySet()) {
                        if (factor1 instanceof Var &&
                                ((Var) factor1).getName().equals(((Var) factor).getName())) {
                            Var var = new Var(((Var) factor1).getName(),
                                    factor1.getSign() * factor.getSign());
                            this.factors.put(var, this.factors.get(factor1) + pow);
                            this.factors.remove(factor1);
                            has = true;
                            break; } } } } else if (factor instanceof Sin) {
                if (!this.factors.isEmpty()) {
                    for (Factor factor1 : this.factors.keySet()) {
                        if (factor1 instanceof Sin &&
                                ((Sin) factor1).getExpr().equal(((Sin) factor).getExpr())) {
                            Sin newSin = new Sin(((Sin) factor1).getExpr(),
                                    factor1.getSign() * factor.getSign());
                            this.factors.put(newSin, this.factors.get(factor1) + pow);
                            this.factors.remove(factor1);
                            has = true;
                            break; } } } } else if (factor instanceof Cos) {
                if (!this.factors.isEmpty()) {
                    for (Factor factor1 : this.factors.keySet()) {
                        if (factor1 instanceof Cos &&
                                ((Cos) factor1).getExpr().equal(((Cos) factor).getExpr())) {
                            Cos newCos = new Cos(((Cos) factor1).getExpr(),
                                    factor1.getSign() * factor.getSign());
                            this.factors.put(newCos, this.factors.get(factor1) + pow);
                            this.factors.remove(factor1);
                            has = true;
                            break; } } } }
            if (!has) {
                this.factors.put(factor, pow);
            } } }

    public HashMap<Factor, Integer> getFactors() {
        return factors;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!this.coefficient.equals(BigInteger.ZERO)) {
            if (this.factors.isEmpty()) {
                sb.append((this.coefficientSign == -1) ? "-" : "").
                        append(this.coefficient);
            } else {
                if (!this.coefficient.equals(BigInteger.ONE)) {
                    sb.append((this.coefficientSign == -1) ? "-" : "").
                            append(this.coefficient);
                    sb.append("*");
                } else {
                    sb.append((this.coefficientSign == -1) ? "-" : "");
                }
                Iterator<Factor> iter = this.factors.keySet().iterator();
                Factor factor = iter.next();
                int pow = this.factors.get(factor);
                if (pow == 0) {
                    sb.append(1);
                } else if (pow == 1) {
                    sb.append(factor.toString());
                } else {
                    sb.append(factor.toString()).append("**").append(pow);
                }
                if (iter.hasNext()) {
                    sb.append("*");
                    factor = iter.next();
                    pow = this.factors.get(factor);
                    if (pow == 0) {
                        sb.append(1);
                    } else if (pow == 1) {
                        sb.append(factor.toString());
                    } else {
                        sb.append(factor.toString()).append("**").append(pow);
                    }
                    while (iter.hasNext()) {
                        sb.append("*");
                        factor = iter.next();
                        pow = this.factors.get(factor);
                        if (pow == 0) {
                            sb.append(1);
                        } else if (pow == 1) {
                            sb.append(factor.toString());
                        } else {
                            sb.append(factor.toString()).append("**").append(pow);
                        }
                    }
                }

            }
        } else {
            sb.append("");
        }
        return sb.toString();
    }

    public Expr expand() {
        boolean hasExpr = false;
        Expr expr = new Expr(); // create
        for (Factor factor : this.factors.keySet()) {
            if (factor instanceof Expr) {
                hasExpr = true;
                if (factor.getSign() == -1) { // if expr is negative then invert every term's sign
                    for (Term term : ((Expr) factor).getTerms()) {
                        term.setCoefficientSign(term.getCoefficientSign() * -1);
                    }
                    factor.setSign(1);
                }
                // factor is (-a*-b - b)
                // 造一个个新的term，里面包含括号里的每个term里的每个factor和括号外的全体factor和
                for (Term term1 : ((Expr) factor).getTerms()) {
                    Term term = new Term();
                    term.setCoefficient(term.coefficient.multiply(term1.getCoef()).
                            multiply(this.coefficient));
                    term.setCoefficientSign(term.coefficientSign * term1.coefficientSign *
                            this.coefficientSign);
                    if (!term1.getFactors().isEmpty()) {
                        for (Factor factor1 : term1.factors.keySet()) {
                            term.addFactor(factor1, term1.getFactors().get(factor1));
                        }
                    }
                    for (Factor factor1 : this.factors.keySet()) {
                        if (!factor1.equals(factor)) {
                            term.addFactor(factor1, this.factors.get(factor1));
                        }
                    }
                    expr.getTerms().addAll(term.expand().getTerms());
                }
            }
            if (hasExpr) {
                break;
            }
        }
        if (!hasExpr) {
            Term term = new Term();
            term.setCoefficient(this.coefficient);
            term.setCoefficientSign(this.coefficientSign);
            for (Factor factor1 : this.factors.keySet()) {
                term.addFactor(factor1, this.getFactors().get(factor1));
            }
            expr.addTerm(term);
        }
        return expr;
    }

    public BigInteger getCoef() {
        return coefficient;
    }

    public void setCoefficient(BigInteger coefficient) {
        this.coefficient = coefficient;
    }

    public int getCoefficientSign() {
        return coefficientSign;
    }

    public void setCoefficientSign(int coefficientSign) {
        this.coefficientSign = coefficientSign;
    }

    public boolean equal(Term term) {
        if (term.getFactors().keySet().size() == this.factors.keySet().size()) {
            if (term.getFactors().keySet().size() == 0) {
                return true;/*term.getCoef().equals(this.coefficient) &&
                        term.getCoefficientSign() == this.coefficientSign;*/
            }
            for (Factor factor1 : term.getFactors().keySet()) {
                if (!this.contain(factor1, term.getFactors().get(factor1))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean allEqual(Term term) {

        if (term.getFactors().keySet().size() == this.factors.keySet().size()) {
            if (term.getFactors().keySet().size() == 0) {
                return term.coefficient.equals(this.coefficient) &&
                        term.coefficientSign == this.coefficientSign;
            } else {
                if (!(this.coefficientSign == term.coefficientSign &&
                        this.coefficient.equals(term.coefficient))) {
                    return false;
                }
                for (Factor factor1 : term.getFactors().keySet()) {
                    if (!this.contain(factor1, term.getFactors().get(factor1))) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean contain(Factor factor1, int pow) {
        for (Factor factor : this.factors.keySet()) {
            if (factor.equal(factor1) &&
                    this.factors.get(factor) == pow) {
                return true;
            }
        }
        return false;
    }

    public int realSign() {
        int realSign = this.coefficientSign;
        for (Factor factor : this.factors.keySet()) {
            realSign *= factor.getSign();
        }
        return realSign;
    }

    public Expr derivation(int needDerivation) {
        Expr exprRet = new Expr();
        for (Factor factor : this.factors.keySet()) {
            if (factor instanceof Expr) {
                exprRet.getTerms().addAll(((Expr) factor).
                        derivation(needDerivation).getTerms()); // 不会有这种情况？
            } else if (factor instanceof Var) {
                char c = (char) ('x' + needDerivation - 1);
                if (((Var) factor).getName().equals(String.valueOf(c))) {
                    Var var = new Var(((Var) factor).getName(), factor.getSign());
                    Term term = new Term();
                    BigInteger bi = BigInteger.valueOf(this.factors.get(factor));
                    term.setCoefficient(this.coefficient.multiply(bi));
                    term.setCoefficientSign(this.coefficientSign);
                    term.addFactor(var, this.factors.get(factor) - 1);
                    for (Factor factor1 : this.factors.keySet()) {
                        if (!factor1.equals(factor)) {
                            term.addFactor(factor1, this.factors.get(factor1));
                        }
                    }
                    exprRet.addTerm(term);
                }
            } else if (factor instanceof Sin) {
                Term term = new Term();
                BigInteger bi = BigInteger.valueOf(this.factors.get(factor));
                term.setCoefficient(this.coefficient.multiply(bi));
                term.setCoefficientSign(this.coefficientSign * factor.getSign());

                Cos cos = new Cos(((Sin) factor).getExpr(), factor.getSign());
                Expr expr = ((Sin) factor).getExpr().derivation(needDerivation);

                term.addFactor(cos, 1);
                term.addFactor(expr, 1);
                term.addFactor(factor, this.factors.get(factor) - 1);

                for (Factor factor1 : this.factors.keySet()) {
                    if (!factor1.equals(factor)) {
                        term.addFactor(factor1, this.factors.get(factor1));
                    }
                }
                exprRet.addTerm(term);

            } else if (factor instanceof Cos) {
                Term term = new Term();
                BigInteger bi = BigInteger.valueOf(this.factors.get(factor));
                term.setCoefficient(this.coefficient.multiply(bi));
                term.setCoefficientSign(this.coefficientSign * -1 * factor.getSign());

                Sin sin = new Sin(((Cos) factor).getExpr(), factor.getSign());
                Expr expr = ((Cos) factor).getExpr().derivation(needDerivation);

                term.addFactor(sin, 1);
                term.addFactor(expr, 1);
                term.addFactor(factor, this.factors.get(factor) - 1);

                for (Factor factor1 : this.factors.keySet()) {
                    if (!factor1.equals(factor)) {
                        term.addFactor(factor1, this.factors.get(factor1));
                    }
                }
                exprRet.addTerm(term);
            }
        }
        return exprRet;
    }

}
