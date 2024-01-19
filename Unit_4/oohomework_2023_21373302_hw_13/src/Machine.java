import java.util.ArrayList;
import java.util.Calendar;

public class Machine extends Admin {
    private ArrayList<Book> books;
    private Shelf shelf;
    private Logistics logistics;

    public Machine(Shelf shelf, Logistics logistics) {
        this.books = new ArrayList<>();
        this.shelf = shelf;
        this.logistics = logistics;
    }

    public boolean queryBook(BookType bookType, String bookId,
                             Calendar calendar, String studentId) {
        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                studentId + " queried " + bookType + "-" +
                bookId + " from self-service machine");
        return shelf.hasBook(bookType, bookId);
    }

    public void register(String studentId, Book book) {
        super.addCList(studentId, book);
    }

    public void detain(Book book) {
        this.books.add(book);
    }

    public boolean borrowBook(String studentId, Book book, Calendar calendar) {
        if (checkHasC(studentId, book.getBookId())) {
            detain(book);
            return false;
        } else {
            register(studentId, book);
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    studentId + " borrowed " + book.getType() + "-" +
                    book.getBookId() + " from self-service machine");
            return true;
        }
    }

    public void returnBook(Book book, Calendar calendar, Student student) {
        if (book.isSmeared()) {
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    student.getStudentId() + " got punished by " +
                    "borrowing and returning librarian");
        } else {
            detain(book);
        }
        removeCList(student.getStudentId(), book);
        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                student.getStudentId() + " returned " + book.getType() + "-" +
                book.getBookId() + " to self-service machine");
        if (book.isSmeared()) {
            logistics.repairBook(book, calendar);
        }
    }

    public ArrayList<Book> arrange() {
        ArrayList<Book> res = new ArrayList<>(books);
        books.clear();
        return res;
    }
}
