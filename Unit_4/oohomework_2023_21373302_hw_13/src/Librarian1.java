import java.util.ArrayList;
import java.util.Calendar;

public class Librarian1 extends Admin {
    private ArrayList<Book> books;
    private Logistics logistics;

    public Librarian1(Logistics logistics) {
        this.books = new ArrayList<>();
        this.logistics = logistics;
    }

    private void register(String studentId) {
        addBList(studentId);
    }

    private void detain(Book book) {
        this.books.add(book);
    }

    public boolean borrowBook(String studentId, Book book, Calendar calendar) {
        if (checkHasB(studentId)) {
            detain(book);
            return false;
        } else {
            register(studentId);
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    studentId + " borrowed " + book.getType() + "-" + book.getBookId() +
                    " from borrowing and returning librarian");
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
        removeBList(student.getStudentId());
        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                student.getStudentId() + " returned " + book.getType() + "-" +
                book.getBookId() + " to borrowing and returning librarian");
        if (book.isSmeared()) {
            logistics.repairBook(book, calendar);
        }
    }

    public void lostBook(Book book, Calendar calendar, Student student) {
        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                student.getStudentId() + " got punished by " +
                "borrowing and returning librarian");
        if (book.getType() == BookType.B) {
            removeBList(student.getStudentId());
        } else if (book.getType() == BookType.C) {
            removeCList(student.getStudentId(), book);
        }
    }

    public ArrayList<Book> arrange() {
        ArrayList<Book> res = new ArrayList<>(books);
        books.clear();
        return res;
    }

}
