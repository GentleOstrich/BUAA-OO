import java.util.ArrayList;
import java.util.Calendar;

public class Routine {
    public void routine1(Calendar nowCalendar, Calendar lastCalendar, ArrayList<School> schools) {
        //第二天要做的事
        if (nowCalendar.after(lastCalendar)) {
            for (School school : schools) {
                school.solveCannotSatisfyMsg();
            }
            //处理预定或购买
            for (School school : schools) {
                school.orderOrPurchase();
            }
            // 把书传出去
            for (School school : schools) {
                school.transBooks(lastCalendar);
            }
            //清空图书馆未满足的消息
            for (School school : schools) {
                school.clearCannotBorrowMsg();
            }
            //上一个日期的下一天接到所有图书
            lastCalendar.add(Calendar.DATE, 1);
            for (School school : schools) {
                school.receiveBooks(lastCalendar);
            }
            for (School school : schools) {
                school.distribute(lastCalendar);
            }
        }
    }

    public void routine2(Calendar nowCalendar, Calendar nextArrangeCalendar, Calendar lastCalendar,
                         ArrayList<School> schools, Message message, School fromSchool) {
        if (!nowCalendar.before(nextArrangeCalendar)) {
            //完成新书购置
            for (School school : schools) {
                school.purchaseBook(nextArrangeCalendar);
            }
            System.out.println("[" + nextArrangeCalendar.get(Calendar.YEAR) + "-" +
                    String.format("%02d", (nextArrangeCalendar.get(Calendar.MONTH) + 1)) + "-" +
                    String.format("%02d", nextArrangeCalendar.get(Calendar.DATE)) + "] " +
                    "arranging librarian arranged all the books");
            for (School school : schools) {
                school.arrange(nextArrangeCalendar);
            }
            nextArrangeCalendar.add(Calendar.DATE, 3);
            while (!nowCalendar.before(nextArrangeCalendar)) {
                System.out.println("[" + nextArrangeCalendar.get(Calendar.YEAR) + "-" +
                        String.format("%02d", (nextArrangeCalendar.get(Calendar.MONTH) + 1)) +
                        "-" + String.format("%02d", nextArrangeCalendar.get(Calendar.DATE)) +
                        "] arranging librarian arranged all the books");
                nextArrangeCalendar.add(Calendar.DATE, 3);
            }
        }
        assert fromSchool != null;
        if (message.getOperation() == Operation.BORROWED) {
            fromSchool.borrowed(message);
        } else if (message.getOperation() == Operation.SMEARED) {
            fromSchool.smeared(message);
        } else if (message.getOperation() == Operation.LOST) {
            fromSchool.lost(message);
        } else if (message.getOperation() == Operation.RETURNED) {
            fromSchool.returned(message);
        }
        lastCalendar.set(nowCalendar.get(Calendar.YEAR),
                nowCalendar.get(Calendar.MONTH), nowCalendar.get(Calendar.DATE));
    }

    public void routine3(ArrayList<School> schools, Calendar lastCalendar) {
        for (School school : schools) {
            school.solveCannotSatisfyMsg();
        }
        //处理预定或购买
        for (School school : schools) {
            school.orderOrPurchase();
        }
        // 把书传出去
        for (School school : schools) {
            school.transBooks(lastCalendar);
        }
        //清空图书馆未满足的消息
        for (School school : schools) {
            school.clearCannotBorrowMsg();
        }
    }
}
