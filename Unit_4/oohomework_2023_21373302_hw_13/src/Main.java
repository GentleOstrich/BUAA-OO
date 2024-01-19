import java.util.Calendar;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Controller.getInstance().initial();

        int n = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < n; ++i) {
            String info = scanner.next();
            BookType bookType = Objects.equals(info.split("-")[0], "A") ? BookType.A :
                                Objects.equals(info.split("-")[0], "B") ? BookType.B :
                                Objects.equals(info.split("-")[0], "C") ? BookType.C : null;
            String bookId = info.split("-")[1];
            int num = scanner.nextInt();
            Controller.getInstance().initialBooks(bookType, bookId, num);
        }

        Calendar nextArrangeCalendar = Calendar.getInstance();
        nextArrangeCalendar.set(Calendar.YEAR, 2023);
        nextArrangeCalendar.set(Calendar.MONTH, Calendar.JANUARY);
        nextArrangeCalendar.set(Calendar.DATE, 4);

        int m = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < m; ++i) {

            String msg = scanner.nextLine();
            Message message = new Message(msg);

            Calendar nowCalendar = message.getCalendar();
            if (nowCalendar.after(nextArrangeCalendar)) {
                Controller.getInstance().arrange(nextArrangeCalendar);
                while (nowCalendar.after(nextArrangeCalendar)) {
                    nextArrangeCalendar.add(Calendar.DATE, 3);
                }
            }


            if (message.getOperation() == Operation.BORROWED) {
                Controller.getInstance().borrowed(message);
            } else if (message.getOperation() == Operation.SMEARED) {
                Controller.getInstance().smeared(message);
            } else if (message.getOperation() == Operation.LOST) {
                Controller.getInstance().lost(message);
            } else if (message.getOperation() == Operation.RETURNED) {
                Controller.getInstance().returned(message);
            }


        }

    }
}
