package main;

import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Person, Integer> acquaintance;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
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
    public int compareTo(Person p2) {
        return this.name.compareTo(p2.getName());
    }

    public void addAcquaintance(Person person, int value) {
        this.acquaintance.put(person, value);
    }

    public int getD() {
        return acquaintance.size();
    }

    public HashMap<Person, Integer> getAcquaintance() {
        return acquaintance;
    }

}
