import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Admin {
    private static HashSet<String> BList = new HashSet<>(); // students who have a B-book
    private static HashMap<String, ArrayList<String>>
            CList = new HashMap<>(); // students who have C-books

    public boolean checkHasB(String studentId) {
        if (BList.contains(studentId)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkHasC(String studentId, String bookId) {
        if (CList.containsKey(studentId) && CList.get(studentId).contains(bookId)) {
            return true;
        } else {
            return false;
        }
    }

    public void addBList(String studentId) {
        BList.add(studentId);
    }

    public void removeBList(String studentId) {
        BList.remove(studentId);
    }

    public void addCList(String studentId, Book book) {
        if (!CList.containsKey(studentId)) {
            CList.put(studentId, new ArrayList<>());
        }
        CList.get(studentId).add(book.getBookId());

    }

    public void removeCList(String studentId, Book book) {
        CList.get(studentId).remove(book.getBookId());
    }

}
