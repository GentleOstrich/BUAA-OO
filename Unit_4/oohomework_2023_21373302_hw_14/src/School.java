import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.HashSet;

public class School {
    private String schoolName;
    private Machine machine;
    private Shelf shelf;
    private Librarian1 librarian1;
    private Librarian2 librarian2;
    private Librarian3 librarian3;
    private Department department;
    private HashMap<String, Student> students = new HashMap<>();
    private HashMap<String, Boolean> abooks;
    private HashMap<String, Boolean> bbooks;
    private HashMap<String, Boolean> cbooks;
    private HashMap<String, School> otherSchools;
    private ArrayList<Message> cannotSatisfyList;
    private ArrayList<Book> transInBooks;
    private Logistics logistics;
    private HashSet<String> blist; // students who have a B-book
    private HashMap<String, ArrayList<String>> clist; // students who have C-books
    private Shelf tempShelf;

    public School(String schoolName) {
        blist = new HashSet<>();
        clist = new HashMap<>();

        this.schoolName = schoolName;
        shelf = new Shelf();
        logistics = new Logistics(schoolName);
        machine = new Machine(shelf, logistics, schoolName, clist);
        librarian1 = new Librarian1(logistics, schoolName, blist, clist);
        department = new Department(schoolName, librarian1, machine);
        librarian3 = new Librarian3(students, schoolName, department, blist, clist);
        librarian2 = new Librarian2(librarian1, librarian3, machine, shelf, logistics, department);
        abooks = new HashMap<>();
        bbooks = new HashMap<>();
        cbooks = new HashMap<>();
        otherSchools = new HashMap<>();
        cannotSatisfyList = new ArrayList<>();
        transInBooks = new ArrayList<>();
        department.setLibrarian3(librarian3);
        machine.setLibrarian3(librarian3);
        tempShelf = new Shelf();
    }

    public void initialBooks(BookType bookType, String bookId,
                             int n, boolean ub) throws ParseException {
        if (bookType == BookType.A) {
            abooks.put(bookId, ub);
        } else if (bookType == BookType.B) {
            bbooks.put(bookId, ub);
        } else if (bookType == BookType.C) {
            cbooks.put(bookId, ub);
        }
        for (int i = 0; i < n; ++i) {
            shelf.addBook(new Book(bookType, bookId, ub, schoolName, State.NOT_BORROWED));
        }
    }

    public void borrowed(Message message) {
        String bookId = message.getBookId();
        BookType bookType = message.getBookType();
        Calendar calendar = message.getCalendar();
        String studentId = message.getStudentId();
        String studentSchool = message.getSchoolName();

        if (!students.containsKey(studentId)) {
            students.put(studentId, new Student(studentId));
        }
        if (machine.queryBook(bookType, bookId, calendar, studentId)) {
            Book book = shelf.getBook(bookType, bookId);
            if (book.getType() == BookType.A) {
                shelf.addBook(book);
            } else if (book.getType() == BookType.B) {
                if (librarian1.borrowBook(studentId, book, calendar)) {
                    students.get(studentId).addBook(book, calendar);
                    librarian3.removeBReservation(studentId);
                    //借到一本 B 类书籍的副本，则该同学此前对于任何 B 类书籍的预定，将从此刻起被取消
                }
            } else if (book.getType() == BookType.C) {
                if (machine.borrowBook(studentSchool, studentId, book, calendar)) {
                    students.get(studentId).addBook(book, calendar);
                }
            }
        } else {
            //现在借不了：图书馆里压根就没有这本书 书被借没了 晚上再处理你
            if (bookType != BookType.A) {
                department.addMsg(message);
                this.addCannotSolvedMsg(message);
                for (String schoolName : otherSchools.keySet()) {
                    otherSchools.get(schoolName).addCannotSolvedMsg(message);
                }
            }
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
            Book book = student.removeBook(bookType, bookId, calendar);
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
            Book book = student.removeBook(bookType, bookId, calendar);
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
        ArrayList<Book> list = librarian2.arrange(calendar);
        for (Book book : list) {
            if (book.getType() == BookType.A && !abooks.containsKey(book.getBookId())) {
                abooks.put(book.getBookId(), true);
            } else if (book.getType() == BookType.B && !bbooks.containsKey(book.getBookId())) {
                bbooks.put(book.getBookId(), true);
            } else if (book.getType() == BookType.C && !cbooks.containsKey(book.getBookId())) {
                cbooks.put(book.getBookId(), true);
            }
        }
    }

    public String getSchoolName() {
        return schoolName;
    }

    private boolean ownBook(BookType bookType, String bookId) {
        if (bookType == BookType.A) {
            return abooks.containsKey(bookId);
        } else if (bookType == BookType.B) {
            return bbooks.containsKey(bookId);
        } else if (bookType == BookType.C) {
            return cbooks.containsKey(bookId);
        }
        return false;
    }

    public void solveCannotSatisfyMsg() {
        for (Message message : cannotSatisfyList) {
            if (message.isValid()) {
                String bookId = message.getBookId();
                BookType bookType = message.getBookType();
                String studentId = message.getStudentId();
                String schoolName = message.getSchoolName();
                School otherSchool = otherSchools.get(schoolName);
                if (schoolName.equals(this.schoolName)) {
                    otherSchool = this;
                }
                if (bookType == BookType.B && otherSchool.checkHasB(studentId)) {
                    continue;
                } else if (bookType == BookType.C && otherSchool.checkHasC(studentId, bookId)) {
                    continue;
                }
                if (this.shelf.hasBook(bookType, bookId)) {
                    Book book = shelf.getBook(bookType, bookId);
                    if (book.isUb()) {
                        boolean flag = false;
                        for (Message message1 : cannotSatisfyList) {
                            if (!message1.isValid() && message1.equals(message)) {
                                flag = true;
                                break;
                            } else if (message.getBookType() == BookType.B &&
                                    message1.getBookType() == BookType.B &&
                                    !message1.isValid() &&
                                    Objects.equals(message1.getSchoolName(),
                                            message.getSchoolName()) &&
                                    Objects.equals(message1.getStudentId(),
                                            message.getStudentId())) {
                                flag = true;
                                break;
                            }
                        }
                        message.setValid(false);
                        if (!flag) {
                            tempShelf.addBook(book);
                        } else {
                            //System.out.println(bookType + "-" +bookId);
                            shelf.addBook(book);
                        }
                    } else {
                        message.setValid(true);
                        shelf.addBook(book);
                    }
                }
            }
        }
    }

    public void transBooks(Calendar calendar) {
        for (Message message : cannotSatisfyList) {
            String bookId = message.getBookId();
            BookType bookType = message.getBookType();
            String schoolName = message.getSchoolName();
            School otherSchool = otherSchools.get(schoolName);
            if (!message.isValid() && tempShelf.hasBook(bookType, bookId)) {
                Book book = tempShelf.getBook(bookType, bookId);
                otherSchool.addTransInBook(book);
                System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                        String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                        String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                        book.getOwner() + "-" + book.getType() + "-" + book.getBookId() +
                        " got transported by purchasing department in " + this.schoolName);
            }
        }
        tempShelf.clear();
        ArrayList<Book> books = new ArrayList<>();
        books.addAll(librarian1.getOtherBooks());
        books.addAll(machine.getOtherBooks());
        books.addAll(logistics.getOtherBooks());
        for (Book book : books) {
            String otherSchoolName = book.getOwner();
            School otherSchool = otherSchools.get(otherSchoolName);
            otherSchool.addTransInBook(book);
            //TODO 图书管理处开始将图书运往其它学校时输出
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    book.getOwner() + "-" + book.getType() + "-" + book.getBookId() +
                    " got transported by purchasing department in " + this.schoolName);
        }
    }

    public void orderOrPurchase() {
        for (Message message : cannotSatisfyList) {
            if (message.isValid() && Objects.equals(message.getSchoolName(), schoolName)) {
                String bookId = message.getBookId();
                BookType bookType = message.getBookType();
                Calendar calendar = message.getCalendar();
                String studentId = message.getStudentId();
                if (bookType == BookType.B && checkHasB(studentId)) {
                    continue;
                }
                if (bookType == BookType.C && checkHasC(studentId, bookId)) {
                    continue;
                }
                if (ownBook(bookType, bookId)) {
                    //如果图书馆有这本书，那么就预定它
                    librarian3.addReservation(studentId, calendar, bookType, bookId);
                } else {
                    //否则，需要部门去购买
                    if (librarian3.addReservation(studentId, calendar, bookType, bookId)) {
                        librarian3.addToPurchase(bookType, bookId);
                    }
                }
            }
        }
    }

    public void receiveBooks(Calendar calendar) {
        for (Book book : this.transInBooks) {
            department.addBook(book, calendar);
        }
        this.transInBooks.clear();
    }

    public void distribute(Calendar calendar) {
        //发出校级借书
        department.distribute(calendar, students);
    }

    public void addOtherSchool(School school, String schoolName) {
        this.otherSchools.put(schoolName, school);
    }

    public void addCannotSolvedMsg(Message message) {
        cannotSatisfyList.add(message);
    }

    public void addTransInBook(Book book) {
        this.transInBooks.add(book);
    }

    public void clearCannotBorrowMsg() {
        this.cannotSatisfyList.clear();
    }

    public void purchaseBook(Calendar calendar) {

        for (Item item : this.librarian3.getToPurchase()) {
            BookType bookType = item.getBookType();
            String bookId = item.getBookId();
            if (bookType == BookType.A) {
                this.abooks.put(bookId, true);
            } else if (bookType == BookType.B) {
                this.bbooks.put(bookId, true);
            } else if (bookType == BookType.C) {
                this.cbooks.put(bookId, true);
            }
        }

        this.librarian3.purchaseBook(calendar);
    }

    public boolean checkHasB(String studentId) {
        return librarian1.checkHasB(studentId);
    }

    public boolean checkHasC(String studentId, String bookId) {
        return machine.checkHasC(studentId, bookId);
    }

}
