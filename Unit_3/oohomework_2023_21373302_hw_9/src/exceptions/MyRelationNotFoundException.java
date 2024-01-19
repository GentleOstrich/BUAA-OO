package exceptions;

import com.oocourse.spec1.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static final HashMap<Integer, Integer> COUNT = new HashMap<>();
    private final int id1;
    private final int id2;

    public MyRelationNotFoundException(int id1, int id2) {
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
        this.id1 = id1;
        this.id2 = id2;
    }

    @Override
    public void print() {
        int total = 0;
        for (Integer i : COUNT.keySet()) {
            total += COUNT.get(i);
        }
        if (this.id1 < this.id2) {
            System.out.println("rnf-" + total / 2 + ", " + this.id1 + "-" +
                    COUNT.get(this.id1) + ", " + this.id2 + "-" + COUNT.get(this.id2));
        } else {
            System.out.println("rnf-" + total / 2 + ", " + this.id2 + "-" +
                    COUNT.get(this.id2) + ", " + this.id1 + "-" + COUNT.get(this.id1));
        }
    }
}
