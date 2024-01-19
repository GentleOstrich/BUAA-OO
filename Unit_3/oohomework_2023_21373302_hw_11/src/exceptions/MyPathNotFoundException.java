package exceptions;

import com.oocourse.spec3.exceptions.PathNotFoundException;

import java.util.HashMap;

public class MyPathNotFoundException extends PathNotFoundException {
    private static long SUM = 0;
    private static HashMap<Integer, Integer> COUNT = new HashMap<>();
    private final int id;

    public MyPathNotFoundException(int id) {
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
        System.out.println("pnf-" + SUM + ", " + id + "-" + COUNT.get(id));
    }
}
