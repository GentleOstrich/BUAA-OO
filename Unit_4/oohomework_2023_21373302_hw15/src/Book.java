import java.util.Calendar;

public class Book {
    private BookType bookType;
    private String bookId;
    private boolean isSmeared;
    private boolean isUb;
    private String owner;
    private State state;
    private Calendar borrowedTime;

    public Book(BookType bookType, String bookId, boolean isUb,
                String owner, State state, Calendar borrowedTime) {
        this.bookType = bookType;
        this.isSmeared = false;
        this.bookId = bookId;
        this.isUb = isUb;
        this.owner = owner;
        this.state = state;
        this.borrowedTime = borrowedTime;
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

    public boolean isTimeOut(Calendar returnedTime) {
        if (returnedTime.after(borrowedTime)) {
            int time = returnedTime.get(Calendar.DAY_OF_YEAR)
                    - borrowedTime.get(Calendar.DAY_OF_YEAR);

            if (this.bookType == BookType.B && time > 30) {
                return true;
            } else if (this.bookType == BookType.C && time > 60) {
                return true;
            }
        } else {
            System.out.println("TIME ERROR");
        }
        return false;
    }

    public void setBorrowedTime(Calendar borrowedTime) {

        this.borrowedTime = Calendar.getInstance();

        this.borrowedTime.set(borrowedTime.get(Calendar.YEAR),
                borrowedTime.get(Calendar.MONTH), borrowedTime.get(Calendar.DATE),
                borrowedTime.get(Calendar.HOUR), borrowedTime.get(Calendar.MINUTE),
                borrowedTime.get(Calendar.SECOND));
    }
}
