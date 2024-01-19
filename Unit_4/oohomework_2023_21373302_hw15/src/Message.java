import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Message {
    private Calendar calendar;
    private String studentId;
    private Operation operation;
    private BookType bookType;
    private String bookId;
    private String schoolName;
    private Boolean isValid;

    public Message(String msg, boolean isValid) throws ParseException {

        this.isValid = isValid;

        String[] arr = msg.split(" ");

        String[] d = arr[0].split("\\]|\\[|-");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(d[1] + "-" + d[2] + "-" + d[3]);
        //System.out.println("date : " + sdf.format(date));

        this.calendar = Calendar.getInstance();
        calendar.setTime(date);

        schoolName = arr[1].split("-")[0];
        studentId = arr[1].split("-")[1];

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

    public String getSchoolName() {
        return schoolName;
    }

    public Boolean isValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return Objects.equals(studentId, message.getStudentId()) &&
                operation == message.getOperation() &&
                bookType == message.bookType &&
                Objects.equals(bookId, message.getBookId()) &&
                Objects.equals(schoolName, message.getSchoolName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(calendar, studentId, operation, bookType, bookId, schoolName, isValid);
    }
}
