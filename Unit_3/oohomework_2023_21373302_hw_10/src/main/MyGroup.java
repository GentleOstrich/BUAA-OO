package main;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;

public class MyGroup implements Group {
    private final int id;
    private final HashMap<Integer, Person> people;
    private int ageSum;
    private long agePowSum;
    private int valueSum;

    public MyGroup(int id) {
        this.id = id;
        this.people = new HashMap<>();
        this.ageSum = 0;
        this.agePowSum = 0;
        this.valueSum = 0;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void addPerson(Person person) {
        if (!hasPerson(person)) {
            for (int id : people.keySet()) {
                if (people.get(id).isLinked(person)) {
                    valueSum += 2 * people.get(id).queryValue(person);
                }
            }
            ageSum += person.getAge();
            agePowSum += (long) person.getAge() * person.getAge();
            people.put(person.getId(), person);
        }
    }

    @Override
    public boolean hasPerson(Person person) {
        return this.people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        return valueSum;
    }

    public void setValueSum(int val) {
        this.valueSum = val;
    }

    @Override
    public int getAgeMean() {
        if (people.isEmpty()) {
            return 0;
        }
        return ageSum / people.size();
    }

    @Override
    public int getAgeVar() {
        if (people.isEmpty()) {
            return 0;
        }
        return (int) ((agePowSum - 2 * (ageSum / people.size()) * ageSum + people.size() *
                (ageSum / people.size()) * (ageSum / people.size())) / people.size());
    }

    @Override
    public void delPerson(Person person) {
        if (hasPerson(person)) {
            people.remove(person.getId());
            ageSum -= person.getAge();
            agePowSum -= (long) person.getAge() * person.getAge();
            for (int id : people.keySet()) {
                if (people.get(id).isLinked(person)) {
                    valueSum -= 2 * people.get(id).queryValue(person);
                }
            }
        }
    }

    @Override
    public int getSize() {
        return people.size();
    }

    public boolean equals(Group group) {
        return id == group.getId();
    }
}
