import java.util.Calendar;
import java.util.HashMap;

public class Controller {
    private static Controller INSTANCE = new Controller();
    private Machine machine;
    private Shelf shelf;
    private Librarian1 librarian1;
    private Librarian2 librarian2;
    private Librarian3 librarian3;
    private final HashMap<String, Student> students = new HashMap<>();

    public static Controller getInstance() {
        return INSTANCE;
    }

    public void initial() {
        shelf = new Shelf();
        Logistics logistics = new Logistics();
        machine = new Machine(shelf, logistics);
        librarian1 = new Librarian1(logistics);
        librarian3 = new Librarian3(students);
        librarian2 = new Librarian2(librarian1, librarian3, machine, shelf, logistics);
    }

    public void initialBooks(BookType bookType, String bookId, int n) {
        for (int i = 0; i < n; ++i) {
            shelf.addBook(new Book(bookType, bookId));
        }
    }

    public void borrowed(Message message) {
        String bookId = message.getBookId();
        BookType bookType = message.getBookType();
        Calendar calendar = message.getCalendar();
        String studentId = message.getStudentId();
        if (!students.containsKey(studentId)) {
            students.put(studentId, new Student(studentId));
        }
        if (machine.queryBook(bookType, bookId, calendar, studentId)) {
            Book book = shelf.getBook(bookType, bookId);
            if (book.getType() == BookType.A) {
                shelf.addBook(book);
            } else if (book.getType() == BookType.B) {
                if (librarian1.borrowBook(studentId, book, calendar)) {
                    students.get(studentId).addBook(book);
                    librarian3.removeBReservation(studentId);
                    //借到一本 B 类书籍的副本，则该同学此前对于任何 B 类书籍的预定，将从此刻起被取消
                }
            } else if (book.getType() == BookType.C) {
                if (machine.borrowBook(studentId, book, calendar)) {
                    students.get(studentId).addBook(book);
                }
            }
        } else {
            librarian3.addReservation(studentId, calendar, bookType, bookId);
        }
    }

    public void smeared(Message message) {
        String bookId = message.getBookId();
        BookType bookType = message.getBookType();
        String studentId = message.getStudentId();
        if (students.containsKey(studentId)) {
            Student student = students.get(studentId);
            student.getBookSmeared(bookType, bookId);
        } else {
            System.out.println("SMEARED: Getting student failed");
            System.exit(-1);
        }
    }

    public void lost(Message message) {
        String bookId = message.getBookId();
        BookType bookType = message.getBookType();
        Calendar calendar = message.getCalendar();
        String studentId = message.getStudentId();
        if (students.containsKey(studentId)) {
            Student student = students.get(studentId);
            Book book = student.removeBook(bookType, bookId);
            librarian1.lostBook(book, calendar, student);
        } else {
            System.out.println("LOST: Getting student failed");
            System.exit(-1);
        }
    }

    public void returned(Message message) {
        String bookId = message.getBookId();
        BookType bookType = message.getBookType();
        Calendar calendar = message.getCalendar();
        String studentId = message.getStudentId();
        if (students.containsKey(studentId)) {
            Student student = students.get(studentId);
            Book book = student.removeBook(bookType, bookId);
            if (book.getType() == BookType.B) {
                librarian1.returnBook(book, calendar, student);
            } else if (book.getType() == BookType.C) {
                machine.returnBook(book, calendar, student);
            }
        } else {
            System.out.println("RETURNED: Getting student failed");
            System.exit(-1);
        }
    }

    public void arrange(Calendar calendar) {
        librarian2.arrange(calendar);
    }

}
