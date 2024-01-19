import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class Department {
    private ArrayList<Book> books;
    private ArrayList<Message> messages;
    private String schoolName;
    private Librarian1 librarian1;
    private Librarian3 librarian3;
    private Machine machine;

    public Department(String schoolName, Librarian1 librarian1, Machine machine) {
        books = new ArrayList<>();
        messages = new ArrayList<>();
        this.schoolName = schoolName;
        this.librarian1 = librarian1;
        this.machine = machine;
    }

    public void addBook(Book book, Calendar calendar) {
        books.add(book);
        //TODO 图书管理处接收从其他学校运输回的图书时输出
        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                book.getOwner() + "-" + book.getType() + "-" + book.getBookId() +
                " got received by purchasing department in " + this.schoolName);
    }

    public void distribute(Calendar calendar, HashMap<String, Student> students) {
        for (Message message : this.messages) {
            if (!message.isValid()) {
                Iterator<Book> it = books.iterator();
                while (it.hasNext()) {
                    Book book = it.next();
                    BookType bookType = message.getBookType();
                    String bookId = message.getBookId();
                    if (book.getType() == bookType && bookId.equals(book.getBookId())) {
                        String studentSchool = message.getSchoolName();
                        String studentId = message.getStudentId();
                        // 借出要登记  还要取消预订
                        if (bookType == BookType.B) {
                            librarian1.register(studentId);
                            librarian3.removeBReservation(studentId);
                        } else if (bookType == BookType.C) {
                            machine.register(studentId, book);
                            librarian3.removeCReservation(studentId, bookId);
                        }
                        //TODO 某服务部门成功将书借出给学生时输出
                        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                                "purchasing department lent " +
                                book.getOwner() + "-" + book.getType() + "-" + book.getBookId() +
                                " to " + studentSchool + "-" + studentId);
                        //TODO 学生成功借书时输出
                        students.get(studentId).getOrderedBook(book, calendar);
                        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                                this.schoolName + "-" + studentId + " borrowed " +
                                book.getOwner() + "-" + book.getType() + "-" + book.getBookId() +
                                " from purchasing department");

                        it.remove();
                        break;
                    }
                }
            }
        }
        this.messages.clear();
    }

    public void addMsg(Message message) {
        this.messages.add(message);
    }

    public ArrayList<Book> arrange() {
        ArrayList<Book> res = new ArrayList<>(books);
        books.clear();
        return res;
    }

    public void purchaseBooks(Book book, Calendar calendar) {
        this.books.add(book);
    }

    public void setLibrarian3(Librarian3 librarian3) {
        this.librarian3 = librarian3;
    }
}
