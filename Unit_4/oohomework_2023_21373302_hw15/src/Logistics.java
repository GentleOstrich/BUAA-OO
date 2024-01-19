import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class Logistics {
    private ArrayList<Book> books;
    private String schoolName;

    public Logistics(String schoolName) {
        this.books = new ArrayList<>();
        this.schoolName = schoolName;
    }

    public void repairBook(Book book, Calendar calendar) {
        book.setSmeared(false);
        books.add(book);
        //TODO 书籍被修复时输出
        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                book.getOwner() + "-" + book.getType() + "-" + book.getBookId() +
                " got repaired by logistics division in " + schoolName);
    }

    public ArrayList<Book> arrange() {
        ArrayList<Book> res = new ArrayList<>(books);
        books.clear();
        return res;
    }

    public ArrayList<Book> getOtherBooks() {
        ArrayList<Book> res = new ArrayList<>();
        Iterator<Book> it = books.iterator();
        while (it.hasNext()) {
            Book book = it.next();
            if (!book.getOwner().equals(this.schoolName)) {
                res.add(book);
                it.remove();
            }
        }
        return res;
    }

}
