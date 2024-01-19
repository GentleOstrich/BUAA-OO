public class Book {
    private BookType bookType;
    private String bookId;
    private boolean isSmeared;

    public Book(BookType bookType, String bookId) {
        this.bookType = bookType;
        this.isSmeared = false;
        this.bookId = bookId;
    }

    public void setSmeared(boolean smeared) {
        isSmeared = smeared;
    }

    public BookType getType() {
        return this.bookType;
    }

    public String getBookId() {
        return this.bookId;
    }

    public boolean isSmeared() {
        return this.isSmeared;
    }

    public String toString() {
        return (bookType +  "-" + bookId);
    }

}
