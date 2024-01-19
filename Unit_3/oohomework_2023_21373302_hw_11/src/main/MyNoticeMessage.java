package main;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

public class MyNoticeMessage extends MyMessage implements NoticeMessage {
    private String noticeString;
    /*@ ensures type == 0;
      @ ensures group == null;
      @ ensures id == messageId;
      @ ensures person1 == messagePerson1;
      @ ensures person2 == messagePerson2;
      @ ensures string == noticeString;
      @*/

    public MyNoticeMessage(int messageId, String noticeString,
                           Person messagePerson1, Person messagePerson2) {
        super(messageId, noticeString.length(), messagePerson1, messagePerson2);
        this.noticeString = noticeString;
    }

    /*@ ensures type == 1;
      @ ensures person2 == null;
      @ ensures id == messageId;
      @ ensures person1 == messagePerson1;
      @ ensures group == messageGroup;
      @ ensures string == noticeString;
      @*/
    public MyNoticeMessage(int messageId, String noticeString,
                           Person messagePerson1, Group messageGroup) {
        super(messageId, noticeString.length(), messagePerson1, messageGroup);
        this.noticeString = noticeString;
    }

    @Override
    public int getType() {
        return super.getType();
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public int getSocialValue() {
        return super.getSocialValue();
    }

    @Override
    public Person getPerson1() {
        return super.getPerson1();
    }

    @Override
    public Person getPerson2() {
        return super.getPerson2();
    }

    @Override
    public Group getGroup() {
        return super.getGroup();
    }

    @Override
    public String getString() {
        return this.noticeString;
    }
}
