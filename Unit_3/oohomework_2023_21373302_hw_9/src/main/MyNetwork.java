package main;

import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;

import java.util.HashMap;
import java.util.Objects;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people;
    private final HashMap<Person, Person> father;
    private int blockSum;
    private long findDepth;
    private int tripleSum;

    public MyNetwork() {
        people = new HashMap<>();
        father = new HashMap<>();
        blockSum = 0;
        tripleSum = 0;
    }

    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        if (contains(id)) {
            return people.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            people.put(person.getId(), person);
            father.put(person, person);
            blockSum++;
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        } else {
            Person person1 = getPerson(id1);
            Person person2 = getPerson(id2);
            if (person2 != null && person1 != null) {
                ((MyPerson) person1).addAcquaintance(person2, value);
                ((MyPerson) person2).addAcquaintance(person1, value);
                merge(person1, person2);
                Person person3 = (((MyPerson) person1).getD() < ((MyPerson) person2).getD()) ?
                        person1 : person2;
                Person person4 = (((MyPerson) person1).getD() < ((MyPerson) person2).getD()) ?
                        person2 : person1;
                for (Person person : ((MyPerson) person3).getAcquaintance().keySet()) {
                    if (person.isLinked(person4) && person.getId() != person4.getId()) {
                        this.tripleSum++;
                    }
                }
            }
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        } else {
            return getPerson(id1).queryValue(getPerson(id2));
        }
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            if (id1 == id2) {
                return true;
            }
            return find(people.get(id1)) == find(people.get(id2));
        }
    }

    @Override
    public int queryBlockSum() {
        return blockSum;
    }

    @Override
    public int queryTripleSum() {
        return tripleSum;
    }

    @Override
    public boolean queryTripleSumOKTest(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                        HashMap<Integer, HashMap<Integer, Integer>> afterData,
                                        int result) {
        if (beforeData.size() != afterData.size()) {
            return false;
        }
        for (Integer id : beforeData.keySet()) {
            boolean flag = true;
            if (!afterData.containsKey(id)) {
                flag = false;
            } else {
                if (beforeData.get(id).size() != afterData.get(id).size()) {
                    return false;
                }
                for (Integer id1 : beforeData.get(id).keySet()) {
                    if (!afterData.get(id).containsKey(id1)) {
                        flag = false;
                        break;
                    } else {
                        if (!Objects.equals(beforeData.get(id).get(id1),
                                afterData.get(id).get(id1))) {
                            flag = false;
                            break;
                        }
                    } } }
            if (!flag) {
                return false;
            }
        }
        int cnt = 0;
        for (int i = 0; i < beforeData.keySet().toArray().length; ++i) {
            for (int j = i + 1; j < beforeData.keySet().toArray().length; ++j) {
                for (int k = j + 1; k < beforeData.keySet().toArray().length; ++k) {
                    int ii = (int) beforeData.keySet().toArray()[i];
                    int jj = (int) beforeData.keySet().toArray()[j];
                    int kk = (int) beforeData.keySet().toArray()[k];
                    if (beforeData.get(ii).containsKey(jj) && beforeData.get(ii).containsKey(kk) &&
                        beforeData.get(jj).containsKey(ii) && beforeData.get(jj).containsKey(kk) &&
                        beforeData.get(kk).containsKey(ii) && beforeData.get(kk).containsKey(jj)) {
                        cnt++;
                    }

                }
            }
        }
        return cnt == result;
    }

    public Person find(Person person) {
        Person ancestor = person;
        findDepth = 0;
        while (ancestor != father.get(ancestor)) {
            ancestor = father.get(ancestor);
            findDepth++;
        }
        Person now = person;
        Person temp;
        while (now != ancestor) {
            temp = father.get(person);
            father.put(person, ancestor);
            now = temp;
        }
        return ancestor;
    }

    public void merge(Person person1, Person person2) {
        if (person1.getId() == person2.getId()) {
            return;
        }
        Person ancestor1 = find(person1);
        long findDepth1 = findDepth;
        Person ancestor2 = find(person2);
        long findDepth2 = findDepth;
        if (ancestor1.getId() == ancestor2.getId()) {
            return;
        }
        if (findDepth1 > findDepth2) {
            father.put(ancestor2, ancestor1);
        } else {
            father.put(ancestor1, ancestor2);
        }
        blockSum--;
    }

}
