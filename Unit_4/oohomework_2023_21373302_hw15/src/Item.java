public class Item {
    private BookType bookType;
    private String bookId;
    private int count;

    public Item(BookType bookType, String bookId) {
        this.bookType = bookType;
        this.bookId = bookId;
        this.count = 1;
    }

    public BookType getBookType() {
        return bookType;
    }

    public void setBookType(BookType bookType) {
        this.bookType = bookType;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
