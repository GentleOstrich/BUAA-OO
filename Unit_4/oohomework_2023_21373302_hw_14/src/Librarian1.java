import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;

public class Librarian1 {
    private ArrayList<Book> books;
    private Logistics logistics;
    private String schoolName;
    private HashSet<String> blist;
    private HashMap<String, ArrayList<String>> clist;

    public Librarian1(Logistics logistics, String schoolName,
                      HashSet<String> blist, HashMap<String, ArrayList<String>> clist) {
        this.books = new ArrayList<>();
        this.logistics = logistics;
        this.schoolName = schoolName;
        this.blist = blist;
        this.clist = clist;
    }

    public void register(String studentId) {
        addBList(studentId);
    }

    private void detain(Book book) {
        this.books.add(book);
    }

    public boolean borrowBook(String studentId, Book book, Calendar calendar) {
        BookType bookType = book.getType();
        String bookId = book.getBookId();
        if (checkHasB(studentId)) {
            detain(book);
            //TODO 某服务部门拒绝将书借出给学生时输出
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    "borrowing and returning librarian refused lending " +
                    book.getOwner() + "-" + bookType + "-" + bookId +
                    " to " + schoolName + "-" + studentId);
            return false;
        } else {
            register(studentId);
            //TODO 某服务部门成功将书借出给学生时输出
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    "borrowing and returning librarian lent " +
                    book.getOwner() + "-" + bookType + "-" + bookId +
                    " to " + schoolName + "-" + studentId);
            //TODO 学生成功借书时输出
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    schoolName + "-" + studentId + " borrowed " +
                    book.getOwner() + "-" + book.getType() + "-" + book.getBookId() +
                    " from borrowing and returning librarian");
            return true;
        }
    }

    public void returnBook(Book book, Calendar calendar, Student student) {
        if (book.isSmeared()) {
            //TODO 学生缴纳赔偿金时输出
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    schoolName + "-" + student.getStudentId() +
                    " got punished by borrowing and returning librarian");
            //TODO 借还管理员收取学生罚款时输出
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    "borrowing and returning librarian received " +
                    schoolName + "-" + student.getStudentId() +
                    "'s fine");
        } else {
            detain(book);
        }
        removeBList(student.getStudentId());
        //TODO 学生成功还书时输出
        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                schoolName + "-" + student.getStudentId() +
                " returned " + book.getOwner() + "-" + book.getType() + "-" +
                book.getBookId() + " to borrowing and returning librarian");
        //TODO 某服务部门收集学生还书时输出
        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                "borrowing and returning librarian collected " +
                book.getOwner() + "-" + book.getType() + "-" +
                book.getBookId() + " from " +
                schoolName + "-" + student.getStudentId());
        if (book.isSmeared()) {
            logistics.repairBook(book, calendar);
        }
    }

    public void lostBook(Book book, Calendar calendar, Student student) {
        //TODO 学生缴纳赔偿金时输出
        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                schoolName + "-" + student.getStudentId() +
                " got punished by borrowing and returning librarian");
        //TODO 借还管理员收取学生罚款时输出
        System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                "borrowing and returning librarian received " +
                schoolName + "-" + student.getStudentId() +
                "'s fine");
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

    public void addBList(String studentId) {
        blist.add(studentId);
    }

    public boolean checkHasB(String studentId) {
        return blist.contains(studentId);
    }

    public void removeBList(String studentId) {
        blist.remove(studentId);
    }

    public void removeCList(String studentId, Book book) {
        clist.get(studentId).remove(book.getBookId());
    }

}
