import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class Term {
    private ArrayList<Factor> factors;
    private Process process;
    private int sign;

    public Term(int sign) {
        this.factors = new ArrayList<>();
        this.process = new Process();
        this.sign = sign;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public ArrayList<Factor> getFactors() {
        return factors;
    }

    public void setFactors(ArrayList<Factor> factors) {
        this.factors = factors;
    }

    public String toString() {
        int flag = this.sign;
        Iterator<Factor> iter = factors.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        if (iter.hasNext()) {
            //sb.append(" ");
            sb.append("*");
            sb.append(iter.next().toString());
            while (iter.hasNext()) {
                //sb.append(" ");
                sb.append("*");
                sb.append(iter.next().toString());
            }
        }
        return sb.toString();
    }

    public ArrayList<Term> expandTerms() {
        boolean flag1 = false;
        boolean flag2 = true;
        Expr expr = new Expr(1, 1);
        for (Factor factor : this.factors) {
            if (factor instanceof Expr) {
                flag1 = true;
                if (((Expr) factor).getPow() == 0) {
                    if (expr.getTerms().size() == 0) {
                        Term termOne = new Term(1);
                        Number factorOne = new Number(BigInteger.ONE, 1);
                        termOne.addFactor(factorOne);
                        expr.addTerm(termOne);
                    }  ///?
                } else {
                    for (int i = 0; i < ((Expr) factor).getPow(); ++i) {
                        expr = process.exprMultiExpr(expr, (Expr) factor);
                    }
                }
            } else if (factor instanceof Number) {
                flag2 = false;
                expr = process.exprMultiNumber(expr, (Number) factor);
            } else if (factor instanceof Var) {
                flag2 = false;
                expr = process.exprMultiVar(expr, (Var) factor);
            }
        }
        if (flag1 && flag2) {
            for (Term term : expr.getTerms()) {
                term.setSign(term.getSign()  * this.sign);
            }
        }
        return new ArrayList<>(expr.getTerms());
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }
}
