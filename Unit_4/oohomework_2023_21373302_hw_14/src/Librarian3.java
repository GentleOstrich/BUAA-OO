import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

public class Librarian3 {
    private HashMap<String, HashMap<String, Integer>> count;
    private ArrayList<Pair<String, Book>> reservation;
    private HashMap<String, Student> students;
    private ArrayList<Item> toPurchase;
    private String schoolName;
    private Department department;
    private HashSet<String> blist;
    private HashMap<String, ArrayList<String>> clist;

    public Librarian3(HashMap<String, Student> students,
                      String schoolName, Department department, HashSet<String> blist,
                      HashMap<String, ArrayList<String>> clist) {
        this.count = new HashMap<>();
        this.reservation = new ArrayList<>();
        this.toPurchase = new ArrayList<>();
        this.students = students;
        this.schoolName = schoolName;
        this.department = department;
        this.blist = blist;
        this.clist = clist;
    }

    private boolean hasReserved(String studentId,
                                BookType bookType, String bookId) {
        //任何时候同一人对于相同书号图书的预定 **仅有第一次被接受**
        for (Pair<String, Book> pair : reservation) {
            if (pair.getKey().equals(studentId) &&
                    Objects.equals(pair.getValue().getBookId(), bookId) &&
                    pair.getValue().getType() == bookType) {
                return true;
            }
        }
        return false;
    }

    public void addToPurchase(BookType bookType, String bookId) {
        for (Item item : this.toPurchase) {
            if (bookType == item.getBookType() && Objects.equals(bookId, item.getBookId())) {
                item.setCount(item.getCount() + 1);
                return;
            }
        }
        toPurchase.add(new Item(bookType, bookId));
    }

    public boolean addReservation(String studentId, Calendar calendar,
                                  BookType bookType, String bookId) {
        if ((bookType == BookType.B && checkHasB(studentId)) ||
                (bookType == BookType.C && checkHasC(studentId, bookId))) {
            return false;
        }
        if (hasReserved(studentId, bookType, bookId)) {
            return false;
        }
        boolean add = false;
        if (!count.containsKey(studentId)) {
            count.put(studentId, new HashMap<>());
        }
        String s = String.valueOf(calendar.get(Calendar.YEAR) +
                calendar.get(Calendar.MONTH) + calendar.get(Calendar.DATE));
        if (!count.get(studentId).containsKey(s)) {
            count.get(studentId).put(s, 0);
        }
        if (count.get(studentId).get(s) < 3) { //一天之内同一人仅允许预定最多三本书的副本
            count.get(studentId).put(s, count.get(studentId).get(s) + 1);
            add = true;
        }
        if (add) {
            //TODO 学生登记预定时输出
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    schoolName + "-" + studentId + " ordered " +
                    schoolName + "-" + bookType + "-" + bookId +
                    " from ordering librarian");
            reservation.add(new Pair<>(studentId, new Book(bookType, bookId, true, null, null)));
            //TODO 预定管理员登记学生的预定时输出
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    "ordering librarian recorded " +
                    schoolName + "-" + studentId + "'s order of " +
                    schoolName + "-" + bookType + "-" + bookId);
            return true;
        }
        return false;
    }

    public void distribute(ArrayList<Book> books, Calendar calendar) {
        Iterator<Pair<String, Book>> it1 = reservation.iterator();
        ArrayList<String> obtainBStudentId = new ArrayList<>();
        HashMap<String, ArrayList<String>> obtainCStudentId = new HashMap<>();

        while (it1.hasNext()) {
            Pair<String, Book> temp = it1.next();
            Iterator<Book> it2 = books.iterator();
            while (it2.hasNext()) {
                Book book1 = temp.getValue();
                Book book2 = it2.next();
                if (book1.getType() == book2.getType() &&
                        Objects.equals(book1.getBookId(), book2.getBookId())) {
                    String studentId = temp.getKey();
                    if (book2.getType() == BookType.B && obtainBStudentId.contains(studentId)) {
                        continue;
                    } else if (book2.getType() == BookType.C &&
                            obtainCStudentId.containsKey(studentId) &&
                            obtainCStudentId.get(studentId).contains(book2.getBookId())) {
                        continue;
                    }
                    it1.remove();
                    it2.remove();

                    //TODO 某服务部门成功将书借出给学生时输出
                    System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                            String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                            String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                            "ordering librarian lent " +
                            book2.getOwner() + "-" + book2.getType() + "-" + book2.getBookId() +
                            " to " + this.schoolName + "-" + studentId);
                    students.get(studentId).addBook(book2, calendar);
                    //TODO 学生成功借书时输出
                    System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                            String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                            String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                            this.schoolName + "-" + studentId + " borrowed " +
                            book2.getOwner() + "-" + book2.getType() + "-" + book2.getBookId() +
                            " from ordering librarian");

                    if (book2.getType() == BookType.B) {
                        obtainBStudentId.add(studentId); //借到一本 B 类书籍的副本，则该同学此前对于任何 B 类书籍的预定，将稍后起被取消
                        addBList(studentId);
                    } else if (book2.getType() == BookType.C) {
                        if (!obtainCStudentId.containsKey(studentId)) {
                            obtainCStudentId.put(studentId, new ArrayList<>());
                        }
                        obtainCStudentId.get(studentId).add(book2.getBookId());
                        addCList(studentId, book2);
                    }
                    break;
                }
            }
        }
        for (String studentId : obtainBStudentId) {
            removeBReservation(studentId);
        }
        for (String studentId : obtainCStudentId.keySet()) {
            for (String bookId : obtainCStudentId.get(studentId)) {
                removeCReservation(studentId, bookId);
            }
        }
    }

    public void removeBReservation(String studentId) {
        Iterator<Pair<String, Book>> it3 = reservation.iterator();
        while (it3.hasNext()) {
            Pair<String, Book> temp1 = it3.next();
            if (temp1.getKey().equals(studentId) && temp1.getValue().getType() == BookType.B) {
                it3.remove();
                Iterator<Item> it = toPurchase.iterator();
                while (it.hasNext()) {
                    Item item = it.next();
                    if (item.getBookType() == temp1.getValue().getType() &&
                            Objects.equals(item.getBookId(), temp1.getValue().getBookId())) {
                        int num = item.getCount() - 1;
                        if (num <= 0) {
                            it.remove();
                        } else {
                            item.setCount(num);
                        }
                        break;
                    }
                }
            }
        }
    }

    public void removeCReservation(String studentId, String bookId) {
        if (clist.containsKey(studentId)) {
            ArrayList<String> list = clist.get(studentId);
            Iterator<String> it = list.iterator();
            while (it.hasNext()) {
                String bookName = it.next();
                if (bookName.equals(bookId)) {
                    it.remove();
                }
            }
        }
        Iterator<Item> it = toPurchase.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            if (item.getBookType() == BookType.C &&
                    Objects.equals(item.getBookId(), bookId)) {
                int num = item.getCount() - 1;
                if (num <= 0) {
                    it.remove();
                } else {
                    item.setCount(num);
                }
                break;
            }
        }
    }

    public void purchaseBook(Calendar calendar) {
        for (Item item : toPurchase) {
            BookType bookType = item.getBookType();
            String bookId = item.getBookId();
            int n = Math.max(item.getCount(), 3);
            for (int i = 0; i < n; ++i) {
                Book book = new Book(bookType, bookId,
                        true, schoolName, State.NOT_BORROWED);
                department.purchaseBooks(book, calendar);
            }
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    schoolName + "-" + bookType + "-" + bookId + " " +
                    "got purchased by purchasing department in " + schoolName);
        }
        toPurchase.clear();
    }

    public ArrayList<Item> getToPurchase() {
        return toPurchase;
    }

    public boolean checkHasB(String studentId) {
        return blist.contains(studentId);
    }

    public boolean checkHasC(String studentId, String bookId) {
        return clist.containsKey(studentId) && clist.get(studentId).contains(bookId);
    }

    public void addBList(String studentId) {
        blist.add(studentId);
    }

    public void addCList(String studentId, Book book) {
        if (!clist.containsKey(studentId)) {
            clist.put(studentId, new ArrayList<>());
        }
        clist.get(studentId).add(book.getBookId());

    }

}
