import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class Librarian3 extends Admin {
    private HashMap<String, HashMap<String, Integer>> count;
    private ArrayList<Pair<String, Book>> reservation;
    private HashMap<String, Student> students;

    public Librarian3(HashMap<String, Student> students) {
        this.count = new HashMap<>();
        this.reservation = new ArrayList<>();
        this.students = students;
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

    public void addReservation(String studentId,
                               Calendar calendar, BookType bookType, String bookId) {
        if ((bookType == BookType.B && checkHasB(studentId)) ||
                (bookType == BookType.C && checkHasC(studentId, bookId))) {
            return;
        }
        if (hasReserved(studentId, bookType, bookId)) {
            return;
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
            reservation.add(new Pair<>(studentId, new Book(bookType, bookId)));
            System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                    studentId + " ordered " + bookType + "-" + bookId +
                    " from ordering librarian");
        }
    }

    public void distribute(ArrayList<Book> books, Calendar calendar) {
        Iterator<Pair<String, Book>> it1 = reservation.iterator();
        ArrayList<String> obtainBStudentId = new ArrayList<>();
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
                    }
                    it1.remove();
                    it2.remove();
                    students.get(studentId).addBook(book2);
                    System.out.println("[" + calendar.get(Calendar.YEAR) + "-" +
                            String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" +
                            String.format("%02d", calendar.get(Calendar.DATE)) + "] " +
                            studentId + " borrowed " + book2.getType() + "-" +
                            book2.getBookId() + " from ordering librarian");
                    if (book2.getType() == BookType.B) {
                        obtainBStudentId.add(studentId); //借到一本 B 类书籍的副本，则该同学此前对于任何 B 类书籍的预定，将稍后起被取消
                        addBList(studentId);
                    } else if (book2.getType() == BookType.C) {
                        addCList(studentId, book2);
                    }
                    break;
                }
            }
        }
        for (String studentId : obtainBStudentId) {
            removeBReservation(studentId);
        }
    }

    public void removeBReservation(String studentId) {
        Iterator<Pair<String, Book>> it3 = reservation.iterator();
        while (it3.hasNext()) {
            Pair<String, Book> temp1 = it3.next();
            if (temp1.getKey().equals(studentId) && temp1.getValue().getType() == BookType.B) {
                it3.remove();
            }
        }
    }

}
