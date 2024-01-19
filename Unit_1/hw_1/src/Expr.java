import java.util.ArrayList;
import java.util.Iterator;

public class Expr implements Factor {
    private ArrayList<Term> terms;
    private int pow;

    private int sign;

    public Expr(int pow, int sign) {
        this.terms = new ArrayList<>();
        this.pow = pow;
        this.sign = sign;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    public void setTerms(ArrayList<Term> terms) {
        this.terms = terms;
    }

    public int getPow() {
        return pow;
    }

    public void setPow(int pow) {
        this.pow = pow;
    }

    public String toString() {
        Term term;
        Iterator<Term> iter = terms.iterator();
        term = iter.next();
        StringBuilder sb = new StringBuilder();
        if (term.getSign() == -1) {
            sb.append("-");
        }
        sb.append(term.toString());
        if (iter.hasNext()) {
            term = iter.next();
            if (term.getSign() == 1) {
                sb.append("+");
            } else {
                sb.append("-");
            }
            sb.append(term.toString());
            while (iter.hasNext()) {
                term = iter.next();
                if (term.getSign() == 1) {
                    sb.append("+");
                } else {
                    sb.append("-");
                }
                sb.append(term.toString());
            }
        }
        return sb.toString();
    }

    @Override
    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }
}
