package exceptions;

import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;

import java.util.HashMap;

public class MyAcquaintanceNotFoundException extends AcquaintanceNotFoundException {
    private static long SUM = 0;
    private static final HashMap<Integer, Integer> COUNT = new HashMap<>();
    private final int id;

    public MyAcquaintanceNotFoundException(int id) {
        this.id = id;
        SUM++;
        if (COUNT.containsKey(id)) {
            COUNT.put(id, COUNT.get(id) + 1);
        } else {
            COUNT.put(id, 1);
        }
    }

    @Override
    public void print() {
        System.out.println("anf-" + SUM + ", " + id + "-" + COUNT.get(id));
    }
}
