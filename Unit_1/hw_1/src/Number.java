import java.math.BigInteger;

public class Number implements Factor {
    private BigInteger num;

    private int sign;

    public Number(BigInteger num, int sign) {
        this.num = num;
        this.sign = sign;
    }

    public String toString() {
        return this.num.toString();
    }

    public BigInteger getNum() {
        return num;
    }

    public void setNum(BigInteger num) {
        this.num = num;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }
}
