public class Var implements Factor {
    private String name;
    private int pow;
    private int sign;

    public Var(String name, int pow, int sign) {
        this.name = name;
        this.pow = pow;
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPow() {
        return pow;
    }

    public void setPow(int pow) {
        this.pow = pow;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public String toString() {
        return (this.pow == 0) ? "1" : ((this.pow == 1) ? this.name : this.name + "^" + this.pow);
    }
}
