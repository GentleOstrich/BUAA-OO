import java.util.ArrayList;
import java.util.HashMap;

public class Shelf {
    private HashMap<String, ArrayList<Book>> abooks;
    private HashMap<String, ArrayList<Book>> bbooks;
    private HashMap<String, ArrayList<Book>> cbooks;

    public Shelf() {
        this.abooks = new HashMap<>();
        this.bbooks = new HashMap<>();
        this.cbooks = new HashMap<>();
    }

    public void addBook(Book book) {
        if (book.getType() == BookType.A) {
            if (!this.abooks.containsKey(book.getBookId())) {
                this.abooks.put(book.getBookId(), new ArrayList<>());
            }
            this.abooks.get(book.getBookId()).add(book);
        } else if (book.getType() == BookType.B) {
            if (!this.bbooks.containsKey(book.getBookId())) {
                this.bbooks.put(book.getBookId(), new ArrayList<>());
            }
            this.bbooks.get(book.getBookId()).add(book);
        } else if (book.getType() == BookType.C) {
            if (!this.cbooks.containsKey(book.getBookId())) {
                this.cbooks.put(book.getBookId(), new ArrayList<>());
            }
            this.cbooks.get(book.getBookId()).add(book);
        }
    }

    public Book getBook(BookType bookType, String id) {
        // if there is no book on the shelf, return null;
        Book book = null;
        if (bookType == BookType.A && abooks.containsKey(id) && abooks.get(id).size() > 0) {
            book = abooks.get(id).get(0);
            abooks.get(id).remove(0);
        } else if (bookType == BookType.B && bbooks.containsKey(id) && bbooks.get(id).size() > 0) {
            book = bbooks.get(id).get(0);
            bbooks.get(id).remove(0);
        } else if (bookType == BookType.C && cbooks.containsKey(id) && cbooks.get(id).size() > 0) {
            book = cbooks.get(id).get(0);
            cbooks.get(id).remove(0);
        }
        return book;
    }

    public boolean hasBook(BookType bookType, String id) {
        // if there is no book on the shelf, return null;
        if (bookType == BookType.A && abooks.containsKey(id)) {
            return this.abooks.get(id).size() > 0;
        } else if (bookType == BookType.B && bbooks.containsKey(id)) {
            return this.bbooks.get(id).size() > 0;
        } else if (bookType == BookType.C && cbooks.containsKey(id)) {
            return this.cbooks.get(id).size() > 0;
        }
        return false;
    }

    public void clear() {
        this.abooks.clear();
        this.bbooks.clear();
        this.cbooks.clear();
    }
}
