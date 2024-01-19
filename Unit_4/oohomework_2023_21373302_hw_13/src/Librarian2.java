import java.util.ArrayList;
import java.util.Calendar;

public class Librarian2 {
    private Librarian1 librarian1;
    private Librarian3 librarian3;
    private Machine machine;
    private Shelf shelf;
    private Logistics logistics;

    public Librarian2(Librarian1 librarian1, Librarian3 librarian3,
                      Machine machine, Shelf shelf, Logistics logistics) {
        this.librarian1 = librarian1;
        this.librarian3 = librarian3;
        this.machine = machine;
        this.shelf = shelf;
        this.logistics = logistics;
    }

    public void arrange(Calendar calendar) {
        ArrayList<Book> books = new ArrayList<>();
        books.addAll(librarian1.arrange());
        books.addAll(machine.arrange());
        books.addAll(logistics.arrange());
        //test
        //for (Book book : books) {
        //    System.out.println(book);
        //}
        librarian3.distribute(books, calendar);
        for (Book book : books) {
            shelf.addBook(book);
            //System.out.println(book);
        }
    }
}
