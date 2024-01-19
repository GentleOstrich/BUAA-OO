public class Cos implements Factor {
    private Expr expr;
    private int sign;

    public Cos(Expr expr, int sign) {
        this.expr = expr;
        this.sign = sign;
    }

    public int getSign() {
        return this.sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public boolean equal(Factor factor) {
        if (factor instanceof Cos) {
            return this.expr.equal(((Cos) factor).expr);
        } else {
            return false;
        }
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.sign == -1) {
            sb.append("-");
        }
        sb.append("cos");
        sb.append("(");

        sb.append("(");

        sb.append(this.expr.mergeExpr().toString());

        sb.append(")");

        sb.append(")");
        return sb.toString();
    }

}
