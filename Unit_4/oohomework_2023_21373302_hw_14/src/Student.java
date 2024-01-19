import java.util.Calendar;
import java.util.HashMap;

public class Student {
    private String studentId;
    private HashMap<String, Book> abooks;
    private HashMap<String, Book> bbooks;
    private HashMap<String, Book> cbooks;

    public Student(String studentId) {
        this.studentId = studentId;
        this.abooks = new HashMap<>();
        this.bbooks = new HashMap<>();
        this.cbooks = new HashMap<>();
    }

    public void getBookSmeared(BookType bookType, String bookID) {
        if (bookType == BookType.A) {
            abooks.get(bookID).setSmeared(true);
        } else if (bookType == BookType.B) {
            bbooks.get(bookID).setSmeared(true);
        } else if (bookType == BookType.C) {
            cbooks.get(bookID).setSmeared(true);
        }
    }

    public void addBook(Book book, Calendar calendar) {
        book.setState(State.BORROWED);

        System.out.println("(State) " + "[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                book.getType() + "-" + book.getBookId() +
                " transfers from " + State.NOT_BORROWED + " to " + State.BORROWED);

        if (book.getType() == BookType.A) {
            if (!abooks.containsKey(book.getBookId())) {
                abooks.put(book.getBookId(), book);
            } else {
                System.out.println("ERROR while adding book");
            }
        } else if (book.getType() == BookType.B) {
            if (!bbooks.containsKey(book.getBookId())) {
                bbooks.put(book.getBookId(), book);
            } else {
                System.out.println("ERROR while adding book");
            }
        } else if (book.getType() == BookType.C) {
            if (!cbooks.containsKey(book.getBookId())) {
                cbooks.put(book.getBookId(), book);
            } else {
                System.out.println("ERROR while adding book");
            }
        }
    }

    public Book removeBook(BookType bookType, String bookId, Calendar calendar) {
        //System.out.println(bookType + "-" + bookId);
        Book book = null;
        if (bookType == BookType.A) {
            if (abooks.containsKey(bookId)) {
                book = abooks.get(bookId);
                abooks.remove(bookId);
            } else {
                System.out.println("ERROR while removing book");
            }
        } else if (bookType == BookType.B) {
            if (bbooks.containsKey(bookId)) {
                book = bbooks.get(bookId);
                bbooks.remove(bookId);
            } else {
                System.out.println("ERROR while removing book");
            }
        } else if (bookType == BookType.C) {
            if (cbooks.containsKey(bookId)) {
                book = cbooks.get(bookId);
                cbooks.remove(bookId);
            } else {
                System.out.println("ERROR while removing book");
            }
        }
        assert book != null;
        System.out.println("(State) " + "[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                book.getType() + "-" + book.getBookId() +
                " transfers from " + State.BORROWED + " to " + State.NOT_BORROWED);
        book.setState(State.NOT_BORROWED);
        return book;
    }

    public String getStudentId() {
        return studentId;
    }
}
