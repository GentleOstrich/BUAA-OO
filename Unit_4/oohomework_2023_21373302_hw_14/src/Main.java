import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Scanner;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws ParseException {
        Routine routine = new Routine();
        Scanner scanner = new Scanner(System.in);
        ArrayList<School> schools = new ArrayList<>();
        //输入学校个数
        int t = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < t; ++i) {
            String schoolName = scanner.next();
            School school = new School(schoolName);
            schools.add(school);
            int n = scanner.nextInt();
            scanner.nextLine();
            for (int j = 0; j < n; ++j) {
                String info = scanner.next();
                BookType bookType = Objects.equals(info.split("-")[0], "A") ? BookType.A :
                        Objects.equals(info.split("-")[0], "B") ? BookType.B :
                        Objects.equals(info.split("-")[0], "C") ? BookType.C : null;
                String bookId = info.split("-")[1];
                int num = scanner.nextInt();
                boolean ub = !scanner.next().equals("N");
                school.initialBooks(bookType, bookId, num, ub);
            }
        }
        for (School school : schools) {
            for (School otherSchool : schools) {
                if (!school.getSchoolName().equals(otherSchool.getSchoolName())) {
                    school.addOtherSchool(otherSchool, otherSchool.getSchoolName());
                }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2023-01-01");
        Calendar lastCalendar = Calendar.getInstance();
        lastCalendar.setTime(date);
        Calendar nextArrangeCalendar = Calendar.getInstance();
        nextArrangeCalendar.setTime(date);
        System.out.println("[" + nextArrangeCalendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (nextArrangeCalendar.get(Calendar.MONTH) + 1)) + "-" +
                String.format("%02d", nextArrangeCalendar.get(Calendar.DATE)) + "] " +
                "arranging librarian arranged all the books");
        nextArrangeCalendar.add(Calendar.DATE, 3);
        int m = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < m; ++i) {
            String msg = scanner.nextLine();
            Message message = new Message(msg, true);
            School fromSchool = null;
            for (School school : schools) {
                if (school.getSchoolName().equals(message.getSchoolName())) {
                    fromSchool = school;
                    break;
                }
            }
            Calendar nowCalendar = message.getCalendar();
            routine.routine1(nowCalendar, lastCalendar, schools);
            routine.routine2(nowCalendar, nextArrangeCalendar,
                    lastCalendar, schools, message, fromSchool);
        }
        routine.routine3(schools, lastCalendar);
    }
}
