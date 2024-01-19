package main;

import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private int socialValue;
    private final HashMap<Person, Integer> acquaintance;
    private final ArrayList<Message> messages;
    private Person bestAcquaintance;
    //TODO
    private int money;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.socialValue = 0;
        this.acquaintance = new HashMap<>();
        this.messages = new ArrayList<>();
        this.bestAcquaintance = null;
        this.money = 0;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public boolean isLinked(Person person) {
        if (person.getId() == this.id) {
            return true;
        }
        return acquaintance.containsKey(person);
    }

    @Override
    public int queryValue(Person person) {
        if (acquaintance.containsKey(person)) {
            return acquaintance.get(person);
        }
        return 0;
    }

    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public List<Message> getReceivedMessages() {
        ArrayList<Message> receivedMessages = new ArrayList<>();
        for (int i = 0; i < messages.size() && i < 5; ++i) {
            receivedMessages.add(messages.get(i));
        }
        return receivedMessages;
    }

    @Override
    public void addMoney(int num) {
        //TODO
        this.money += num;
    }

    @Override
    public int getMoney() {
        //TODO
        return this.money;
    }

    @Override
    public int compareTo(Person p2) {
        return this.name.compareTo(p2.getName());
    }

    public void addAcquaintance(Person person, int value) {
        if (bestAcquaintance == null) {
            bestAcquaintance = person;
        } else {
            if (value > acquaintance.get(bestAcquaintance)) {
                bestAcquaintance = person;
            } else if (value == acquaintance.get(bestAcquaintance)) {
                if (person.getId() < bestAcquaintance.getId()) {
                    bestAcquaintance = person;
                }
            }
        }
        acquaintance.put(person, value);
    }

    public void removeAcquaintance(Person person) {
        acquaintance.remove(person);
        if (person == bestAcquaintance) {
            int minId = Integer.MAX_VALUE;
            int maxValue = 0;
            Person aim = null;
            for (Person person1 : acquaintance.keySet()) {
                if (acquaintance.get(person1) > maxValue) {
                    maxValue = acquaintance.get(person1);
                    minId = person1.getId();
                    aim = person1;
                } else if (acquaintance.get(person1) == maxValue) {
                    if (person1.getId() < minId) {
                        maxValue = acquaintance.get(person1);
                        minId = person1.getId();
                        aim = person1;
                    }
                }
            }
            bestAcquaintance = aim;
        }
    }

    public void changeValue(Person person, int value) {
        acquaintance.put(person, acquaintance.get(person) + value);
        if (value > 0) {
            if (acquaintance.get(person) > acquaintance.get(bestAcquaintance)) {
                bestAcquaintance = person;
            } else if (Objects.equals(acquaintance.get(person),
                    acquaintance.get(bestAcquaintance))) {
                if (person.getId() < bestAcquaintance.getId()) {
                    bestAcquaintance = person;
                }
            }
        } else if (value < 0) {
            if (person.getId() == bestAcquaintance.getId()) {
                int minId = Integer.MAX_VALUE;
                int maxValue = 0;
                Person aim = null;
                for (Person person1 : acquaintance.keySet()) {
                    if (acquaintance.get(person1) > maxValue) {
                        maxValue = acquaintance.get(person1);
                        minId = person1.getId();
                        aim = person1;
                    } else if (acquaintance.get(person1) == maxValue) {
                        if (person1.getId() < minId) {
                            maxValue = acquaintance.get(person1);
                            minId = person1.getId();
                            aim = person1;
                        }
                    }
                }
                bestAcquaintance = aim;
            }
        }
    }

    public Person getBestAcquaintance() {
        return bestAcquaintance;
    }

    public int getDegree() {
        return acquaintance.size();
    }

    public HashMap<Person, Integer> getAcquaintance() {
        return acquaintance;
    }

    public boolean equals(Person person) {
        if (person != null) {
            return person.getId() == id;
        }
        return false;
    }

    public String toString() {

        return String.valueOf(id);
    }

    public void setSocialValue(int socialValue) {
        this.socialValue = socialValue;
    }

    public void setBestAcquaintance(Person bestAcquaintance) {
        this.bestAcquaintance = bestAcquaintance;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
