package exceptions;

import com.oocourse.spec3.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private static final HashMap<Integer, Integer> COUNT = new HashMap<>();
    private static int SUM = 0;
    private final int id1;
    private final int id2;

    public MyEqualRelationException(int id1, int id2) {
        SUM++;
        if (COUNT.containsKey(id1)) {
            COUNT.put(id1, COUNT.get(id1) + 1);
        } else {
            COUNT.put(id1, 1);
        }
        if (COUNT.containsKey(id2)) {
            COUNT.put(id2, COUNT.get(id2) + 1);
        } else {
            COUNT.put(id2, 1);
        }
        if (id1 == id2) {
            COUNT.put(id1, COUNT.get(id1) - 1);
        }
        this.id1 = id1;
        this.id2 = id2;
    }

    @Override
    public void print() {
        if (this.id1 < this.id2) {
            System.out.println("er-" + SUM + ", " + this.id1 + "-" +
                    COUNT.get(this.id1) + ", " + this.id2 + "-" + COUNT.get(this.id2));
        } else {
            System.out.println("er-" + SUM + ", " + this.id2 + "-" +
                    COUNT.get(this.id2) + ", " + this.id1 + "-" + COUNT.get(this.id1));
        }
    }
}
