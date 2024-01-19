import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class Expr implements Factor {
    private final ArrayList<Term> terms;
    private int sign;

    public Expr() {
        this.terms = new ArrayList<>();
        this.sign = 1;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    public boolean equal(Factor factor) {
        HashMap<Term, Integer> map = new HashMap<>();
        if (factor instanceof Expr) {
            if (((Expr) factor).getTerms().size() == this.terms.size()) {
                for (Term term : this.terms) {
                    boolean flag = false;
                    for (Term term1 : ((Expr) factor).getTerms()) {
                        if (term1.equal(term) && !map.containsKey(term1)) {
                            map.put(term1, 1);
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Expr mergeExpr() {
        Expr exprMerged = new Expr();
        for (Term term : this.terms) {
            Term termCopy = new Term();
            int coefSignCopy = term.realSign();
            BigInteger coefCopy = term.getCoef();
            if (!term.getFactors().isEmpty() ||
                    !Objects.equals(term.getCoef(), BigInteger.ZERO)) {
                if (exprMerged.contain(term)) {
                    for (Term term1 : exprMerged.terms) {
                        if (term1.equal(term)) {
                            int compare = term1.getCoef().compareTo(term.getCoef());
                            if (term1.realSign() == 1 && term.realSign() == 1) {
                                coefSignCopy = 1;
                                coefCopy = term1.getCoef().add(term.getCoef());
                            } else if (term1.realSign() == 1 && term.realSign() == -1) {
                                coefSignCopy = (compare < 0) ? -1 : 1;
                                coefCopy = (compare < 0) ?
                                        term.getCoef().subtract(term1.getCoef()) :
                                        (compare == 0) ? BigInteger.valueOf(0) :
                                                term1.getCoef().subtract(term.getCoef());
                            } else if (term1.realSign() == -1 && term.realSign() == 1) {
                                coefSignCopy = (compare > 0) ? -1 : 1;
                                coefCopy = (compare < 0) ?
                                        term.getCoef().subtract(term1.getCoef()) :
                                        (compare == 0) ? BigInteger.valueOf(0) :
                                                term1.getCoef().subtract(term.getCoef());
                            } else {
                                coefSignCopy = -1;
                                coefCopy = term1.getCoef().add(term.getCoef());
                            }
                            exprMerged.getTerms().remove(term1);
                            break;
                        }
                    }
                }
                termCopy.setCoefficientSign(coefSignCopy);
                termCopy.setCoefficient(coefCopy);
                for (Factor factor : term.getFactors().keySet()) {
                    if (factor instanceof Var) {
                        Var varCopy = new Var(((Var) factor).getName(), 1);
                        termCopy.addFactor(varCopy, term.getFactors().get(factor));
                    } else if (factor instanceof Sin) {
                        Sin sinCopy = new Sin(((Sin) factor).getExpr(), 1);
                        termCopy.addFactor(sinCopy, term.getFactors().get(factor));
                    } else if (factor instanceof Cos) {
                        Cos cosCopy = new Cos(((Cos) factor).getExpr(), 1);
                        termCopy.addFactor(cosCopy, term.getFactors().get(factor));
                    }
                }
                exprMerged.addTerm(termCopy);
            }
        }
        return exprMerged;
    }

    public boolean contain(Term term) {
        for (Term term1 : this.terms) {
            if (term1.equal(term)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        if (terms.size() == 0) {
            return "0";
        }
        Term term;
        Iterator<Term> iter = terms.iterator();
        term = iter.next();
        StringBuilder sb = new StringBuilder();
        sb.append(term.toString());
        if (iter.hasNext()) {
            term = iter.next();
            String termString = term.toString();
            if (!Objects.equals(termString, "")) {
                sb.append("+");
            }
            sb.append(termString);
            while (iter.hasNext()) {
                term = iter.next();
                termString = term.toString();
                if (!Objects.equals(termString, "")) {
                    sb.append("+");
                }
                sb.append(termString);
            }
        }
        if (sb.toString().equals("")) {
            sb.append(0);
        }
        return sb.toString();
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }
}
