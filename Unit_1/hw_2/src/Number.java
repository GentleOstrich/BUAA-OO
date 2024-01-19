import java.math.BigInteger;

public class Number implements Factor {
    private final BigInteger num;
    private int sign;

    public Number(BigInteger num, int sign) {
        this.num = num;
        this.sign = sign;
    }

    public String toString() {
        if (this.sign == -1) {
            return "-" + this.num.toString();
        } else {
            return this.num.toString();
        }
    }

    public BigInteger getNum() {
        return num;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public boolean equal(Factor factor) {
        return true;
    }
}
