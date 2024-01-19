package exceptions;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

import java.util.HashMap;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private static long SUM = 0;
    private static HashMap<Integer, Integer> COUNT = new HashMap<>();
    private final int id;

    public MyEmojiIdNotFoundException(int id) {
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
        System.out.println("einf-" + SUM + ", " + id + "-" + COUNT.get(id));
    }
}
