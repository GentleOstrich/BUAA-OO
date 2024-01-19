import java.util.Calendar;

public class Message {
    private Calendar calendar;
    private String studentId;
    private Operation operation;
    private BookType bookType;
    private String bookId;

    public Message(String msg) {
        String[] arr = msg.split(" ");

        String[] date = arr[0].split("\\]|\\[|-");
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(date[1]));
        calendar.set(Calendar.MONTH, Integer.parseInt(date[2]) - 1);
        calendar.set(Calendar.DATE, Integer.parseInt(date[3]));


        studentId = arr[1];

        switch (arr[2]) {
            case "borrowed":
                operation = Operation.BORROWED;
                break;
            case "smeared":
                operation = Operation.SMEARED;
                break;
            case "lost":
                operation = Operation.LOST;
                break;
            case "returned":
                operation = Operation.RETURNED;
                break;
            default:
                break;
        }

        bookType = parseBookType(arr[3]);
        bookId = parseBookId(arr[3]);

    }

    private BookType parseBookType(String info) {
        switch (info.split("-")[0]) {
            case "A":
                return BookType.A;
            case "B":
                return BookType.B;
            case "C":
                return BookType.C;
            default:
                break;
        }
        return null;
    }

    private String parseBookId(String info) {
        return info.split("-")[1];
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public String getStudentId() {
        return studentId;
    }

    public Operation getOperation() {
        return operation;
    }

    public BookType getBookType() {
        return bookType;
    }

    public String getBookId() {
        return bookId;
    }
}
