public class Book {
    private BookType bookType;
    private String bookId;
    private boolean isSmeared;
    private boolean isUb;
    private String owner;
    private State state;

    public Book(BookType bookType, String bookId, boolean isUb, String owner, State state) {
        this.bookType = bookType;
        this.isSmeared = false;
        this.bookId = bookId;
        this.isUb = isUb;
        this.owner = owner;
        this.state = state;
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

    public boolean isUb() {
        return isUb;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
