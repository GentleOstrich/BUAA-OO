import java.util.Objects;

public class Var implements Factor {
    private final String name;
    private int sign;

    public Var(String name, int sign) {
        this.name = name;
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public boolean equal(Factor factor) {
        if (factor instanceof Var) {
            return Objects.equals(((Var) factor).getName(), this.name);
        } else {
            return false;
        }
    }

    public String toString() {
        if (this.sign == -1) {
            return "-" + this.name;
        } else {
            return this.name;
        }
    }
}
