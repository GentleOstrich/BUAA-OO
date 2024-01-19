package exceptions;

import com.oocourse.spec1.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static final HashMap<Integer, Integer> COUNT = new HashMap<>();
    private final int id;

    public MyEqualPersonIdException(int id) {
        if (COUNT.containsKey(id)) {
            COUNT.put(id, COUNT.get(id) + 1);
        } else {
            COUNT.put(id, 1);
        }
        this.id = id;
    }

    @Override
    public void print() {
        int total = 0;
        for (Integer i : COUNT.keySet()) {
            total += COUNT.get(i);
        }
        System.out.println("epi-" + total + ", " + this.id + "-" + COUNT.get(this.id));
    }
}
