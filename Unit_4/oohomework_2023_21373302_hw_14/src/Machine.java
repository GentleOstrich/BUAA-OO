import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class Machine {
    private ArrayList<Book> books;
    private Shelf shelf;
    private Logistics logistics;
    private String schoolName;
    private HashMap<String, ArrayList<String>> clist;
    private Librarian3 librarian3;

    public Machine(Shelf shelf, Logistics logistics, String schoolName,
                   HashMap<String, ArrayList<String>> clist) {
        this.books = new ArrayList<>();
        this.shelf = shelf;
        this.logistics = logistics;
        this.schoolName = schoolName;
        this.clist = clist;
    }

    public boolean queryBook(BookType bookType, String bookId,
                             Calendar calendar, String studentId) {
        //TODO 学生查询书籍信息时输出
        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                schoolName + "-" + studentId + " queried " +
                bookType + "-" + bookId +
                " from self-service machine");
        //TODO 自助机器返回被查询书籍信息时输出
        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                "self-service machine provided information of " +
                bookType + "-" + bookId);
        return shelf.hasBook(bookType, bookId);
    }

    public void register(String studentId, Book book) {
        addCList(studentId, book);
    }

    public void detain(Book book) {
        this.books.add(book);
    }

    public boolean borrowBook(String studentSchool, String studentId,
                              Book book, Calendar calendar) {
        BookType bookType = book.getType();
        String bookId = book.getBookId();
        if (checkHasC(studentId, book.getBookId())) {
            detain(book);
            //TODO 某服务部门拒绝将书借出给学生时输出
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    "self-service machine refused lending " +
                    book.getOwner() + "-" + bookType + "-" + bookId + " to " +
                    studentSchool + "-" + studentId);
            return false;
        } else {
            register(studentId, book);
            librarian3.removeCReservation(studentId, bookId);
            //TODO 某服务部门成功将书借出给学生时输出
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    "self-service machine lent " +
                    book.getOwner() + "-" + bookType + "-" + bookId + " to "
                    + studentSchool + "-" + studentId);
            //TODO 学生成功借书时输出
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    studentSchool + "-" + studentId + " borrowed " +
                    book.getOwner() + "-" + bookType + "-" + bookId +
                    " from self-service machine");
            return true;
        }
    }

    public void returnBook(Book book, Calendar calendar, Student student) {
        if (book.isSmeared()) {
            //TODO 学生缴纳赔偿金时输出
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    this.schoolName + "-" + student.getStudentId() +
                    " got punished by borrowing and returning librarian");
            //TODO 借还管理员收取学生罚款时输出
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    "borrowing and returning librarian received " +
                    this.schoolName + "-" + student.getStudentId() +
                    "'s fine");
        } else {
            detain(book);
        }
        removeCList(student.getStudentId(), book);
        //TODO 学生成功还书时输出
        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                this.schoolName + "-" + student.getStudentId() +
                " returned " + book.getOwner() + "-" + book.getType() + "-" +
                book.getBookId() + " to self-service machine");
        //TODO 某服务部门收集学生还书时输出
        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                "self-service machine collected " +
                book.getOwner() + "-" + book.getType() + "-" + book.getBookId() +
                " from " + this.schoolName + "-" + student.getStudentId());
        if (book.isSmeared()) {
            logistics.repairBook(book, calendar);
        }
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

    public void addCList(String studentId, Book book) {
        if (!clist.containsKey(studentId)) {
            clist.put(studentId, new ArrayList<>());
        }
        clist.get(studentId).add(book.getBookId());

    }

    public boolean checkHasC(String studentId, String bookId) {
        if (clist.containsKey(studentId) && clist.get(studentId).contains(bookId)) {
            return true;
        } else {
            return false;
        }
    }

    public void removeCList(String studentId, Book book) {
        clist.get(studentId).remove(book.getBookId());
    }

    public void setLibrarian3(Librarian3 librarian3) {
        this.librarian3 = librarian3;
    }
}
