import java.util.ArrayList;
import java.util.Calendar;

public class Logistics {
    private ArrayList<Book> books;

    public Logistics() {
        this.books = new ArrayList<>();
    }

    public void repairBook(Book book, Calendar calendar) {
        book.setSmeared(false);
        books.add(book);
        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                book.getType() + "-" + book.getBookId() +
                " got repaired by logistics division");
    }

    public ArrayList<Book> arrange() {
        ArrayList<Book> res = new ArrayList<>(books);
        books.clear();
        return res;
    }

}
