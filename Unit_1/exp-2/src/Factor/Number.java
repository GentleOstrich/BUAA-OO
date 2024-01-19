package Factor;

import java.math.BigInteger;

public class Number implements Factor {
    
    private BigInteger num;
    
    public Number(String num) {
        this.num = new BigInteger(num);
    }
    
    @Override
    public String toString() {
        return num.toString();
    }
    
    @Override
    public Factor derive() {
        Term term = new Term();
        /* TODO 1 */
        Number number = new Number("0");
        term.addFactor(number);
        return term;
    }
    
    @Override
    public Factor clone() {
        return new Number(num.toString());
    }
}
