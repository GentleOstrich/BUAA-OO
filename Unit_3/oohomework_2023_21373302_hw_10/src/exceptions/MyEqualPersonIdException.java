package exceptions;

import com.oocourse.spec2.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static int SUM = 0;
    private static final HashMap<Integer, Integer> COUNT = new HashMap<>();
    private final int id;

    public MyEqualPersonIdException(int id) {
        SUM++;
        if (COUNT.containsKey(id)) {
            COUNT.put(id, COUNT.get(id) + 1);
        } else {
            COUNT.put(id, 1);
        }
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("epi-" + SUM + ", " + this.id + "-" + COUNT.get(this.id));
    }
}
