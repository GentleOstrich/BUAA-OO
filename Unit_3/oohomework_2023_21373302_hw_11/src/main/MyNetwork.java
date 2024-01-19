package main;

import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;

import exceptions.MyEqualRelationException;
import exceptions.MyGroupIdNotFoundException;
import exceptions.MyEqualPersonIdException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyMessageIdNotFoundException;
import exceptions.MyRelationNotFoundException;
import exceptions.MyEqualMessageIdException;
import exceptions.MyEqualEmojiIdException;
import exceptions.MyAcquaintanceNotFoundException;
import exceptions.MyPathNotFoundException;
import exceptions.MyEqualGroupIdException;
import exceptions.MyEmojiIdNotFoundException;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people;
    private final HashMap<Integer, Group> groups;
    private final HashMap<Integer, Message> messages;
    private final HashMap<Person, Person> father;
    private int blockSum;
    private long findDepth;
    private int tripleSum;
    private final HashMap<Integer, Integer> couple;
    private HashMap<Person, Person> tempFather;
    //TODO
    private final HashMap<Integer, Integer> emoji;

    public MyNetwork() {
        people = new HashMap<>();
        father = new HashMap<>();
        groups = new HashMap<>();
        messages = new HashMap<>();
        blockSum = 0;
        tripleSum = 0;
        couple = new HashMap<>();
        emoji = new HashMap<>();
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
            return null; } }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            people.put(person.getId(), person);
            father.put(person, person);
            blockSum++; } }

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
                try {
                    updateCouple(id1, id2);
                } catch (AcquaintanceNotFoundException e) {
                    throw new RuntimeException(e);
                }
                updateGroup(id1, id2, value);
                merge(person1, person2);
                Person person3 = (((MyPerson) person1).getDegree() <
                        ((MyPerson) person2).getDegree()) ? person1 : person2;
                Person person4 = (((MyPerson) person1).getDegree() <
                        ((MyPerson) person2).getDegree()) ? person2 : person1;
                for (Person person : ((MyPerson) person3).getAcquaintance().keySet()) {
                    if (person.isLinked(person4) && person.getId() != person4.getId()) {
                        this.tripleSum++; } } } } }

    @Override
    public void modifyRelation(int id1, int id2, int value) throws PersonIdNotFoundException,
            EqualPersonIdException, RelationNotFoundException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (people.containsKey(id1) && !people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (people.containsKey(id1) && people.containsKey(id2) && id1 == id2) {
            throw new MyEqualPersonIdException(id1);
        } else if (people.containsKey(id1) && people.containsKey(id2) && id1 != id2 &&
                !people.get(id1).isLinked(people.get(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        } else if (people.containsKey(id1) && people.containsKey(id2) &&
                id1 != id2 && people.get(id1).isLinked(people.get(id2)) &&
                people.get(id1).queryValue(people.get(id2)) + value > 0) {
            ((MyPerson) people.get(id1)).changeValue(people.get(id2), value);
            ((MyPerson) people.get(id2)).changeValue(people.get(id1), value);
            try {
                updateCouple(id1, id2);
            } catch (AcquaintanceNotFoundException e) {
                throw new RuntimeException(e); }
            updateGroup(id1, id2, value);
        } else if (people.containsKey(id1) && people.containsKey(id2) &&
                id1 != id2 && people.get(id1).isLinked(people.get(id2)) &&
                people.get(id1).queryValue(people.get(id2)) + value <= 0) {
            Person person1 = getPerson(id1);
            Person person2 = getPerson(id2);
            Person person3 = (((MyPerson) person1).getDegree() < ((MyPerson) person2).getDegree()) ?
                    person1 : person2;
            Person person4 = (((MyPerson) person1).getDegree() < ((MyPerson) person2).getDegree()) ?
                    person2 : person1;
            for (Person person : ((MyPerson) person3).getAcquaintance().keySet()) {
                if (person.isLinked(person4) && person.getId() != person4.getId()) {
                    this.tripleSum--;
                } }
            updateGroup(id1, id2, -people.get(id1).queryValue(people.get(id2)));
            ((MyPerson) people.get(id1)).removeAcquaintance(people.get(id2));
            ((MyPerson) people.get(id2)).removeAcquaintance(people.get(id1));
            try {
                updateCouple(id1, id2);
            } catch (AcquaintanceNotFoundException e) {
                throw new RuntimeException(e); }
            tempFather = new HashMap<>();
            updateFather(id1, id2); // id1 != id2 is always true
            if (tempFather != null) {
                updateFather(id2, id1);
                father.putAll(tempFather);
                blockSum++; } } }

    private void updateGroup(int id1, int id2, int value) {
        for (int id : groups.keySet()) {
            if (groups.get(id).hasPerson(people.get(id1)) &&
                    groups.get(id).hasPerson(people.get(id2))) {
                ((MyGroup) groups.get(id)).setValueSum(groups.get(id).getValueSum() + 2 * value);
            } } }

    private void updateCouple(int id1, int id2) throws PersonIdNotFoundException,
            AcquaintanceNotFoundException {
        if (couple.containsKey(id1)) {
            couple.remove(couple.get(id1));
            couple.remove(id1); }
        if (couple.containsKey(id2)) {
            couple.remove(couple.get(id2));
            couple.remove(id2); }
        if (((MyPerson) people.get(id1)).getDegree() != 0 &&
                id1 == queryBestAcquaintance(queryBestAcquaintance(id1))) {
            couple.put(id1, queryBestAcquaintance(id1));
            couple.put(queryBestAcquaintance(id1), id1); }
        if (((MyPerson) people.get(id2)).getDegree() != 0 &&
                id2 == queryBestAcquaintance(queryBestAcquaintance(id2))) {
            couple.put(id2, queryBestAcquaintance(id2));
            couple.put(queryBestAcquaintance(id2), id2); } }

    private void updateFather(int id1, int id2) {
        //System.out.println(id1);
        ArrayList<Person> list = new ArrayList<>();
        list.add(people.get(id1));
        while (list.size() != 0) {
            Person person = list.get(0);
            if (person.getId() == id2) {
                tempFather = null;
                return; }
            tempFather.put(person, people.get(id1));
            for (Person person1 : ((MyPerson) person).getAcquaintance().keySet()) {
                if (person1.getId() == id2) {
                    tempFather = null;
                    return; }
                if (!tempFather.containsKey(person1)) {
                    list.add(person1);
                } }
            list.remove(person); } }

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
            return getPerson(id1).queryValue(getPerson(id2)); } }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            if (id1 == id2) {
                return true; }
            return find(people.get(id1)) == find(people.get(id2));  } }

    @Override
    public int queryBlockSum() {
        return blockSum;
    }

    @Override
    public int queryTripleSum() {
        return tripleSum;
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        } else {
            groups.put(group.getId(), group); } }

    @Override
    public Group getGroup(int id) {
        if (!groups.containsKey(id)) {
            return null;
        }
        return groups.get(id); }

    @Override
    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (groups.containsKey(id2) && !people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (groups.containsKey(id2) && people.containsKey(id1) &&
                groups.get(id2).hasPerson(people.get(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else {
            if (groups.get(id2).getSize() <= 1111) {
                groups.get(id2).addPerson(people.get(id1));
            } } }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return groups.get(id).getValueSum(); } }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return groups.get(id).getAgeVar(); } }

    @Override
    public void delFromGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (groups.containsKey(id2) && !people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (groups.containsKey(id2) && people.containsKey(id1) &&
                !groups.get(id2).hasPerson(people.get(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else {
            groups.get(id2).delPerson(people.get(id1)); } }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException,
            EqualPersonIdException, EmojiIdNotFoundException {
        if (messages.containsKey(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (!messages.containsKey(message.getId()) && (message instanceof EmojiMessage) &&
                !emoji.containsKey(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        } else if (!messages.containsKey(message.getId()) && message.getType() == 0 &&
                message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId()); // may be error
        }
        else {
            messages.put(message.getId(), message); } }

    @Override
    public Message getMessage(int id) {
        if (!messages.containsKey(id)) {
            return null; }
        return messages.get(id); }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        if (!messages.containsKey(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else if (messages.containsKey(id) && messages.get(id).getType() == 0 &&
                !messages.get(id).getPerson1().isLinked(messages.get(id).getPerson2())) {
            throw new MyRelationNotFoundException(messages.get(id).getPerson1().getId(),
                    messages.get(id).getPerson2().getId());
        } else if (messages.containsKey(id) && messages.get(id).getType() == 1 &&
                !messages.get(id).getGroup().hasPerson(messages.get(id).getPerson1())) {
            throw new MyPersonIdNotFoundException(messages.get(id).getPerson1().getId());
        } else if (messages.containsKey(id) && messages.get(id).getType() == 0 &&
                messages.get(id).getPerson1().isLinked(messages.get(id).getPerson2()) &&
                !messages.get(id).getPerson1().equals(messages.get(id).getPerson2())) {
            messages.get(id).getPerson1().addSocialValue(messages.get(id).getSocialValue());
            messages.get(id).getPerson2().addSocialValue(messages.get(id).getSocialValue());
            messages.get(id).getPerson2().getMessages().add(0, messages.get(id));
            //TODO
            if ((messages.get(id) instanceof RedEnvelopeMessage)) {
                messages.get(id).getPerson1().
                        addMoney(-((RedEnvelopeMessage) messages.get(id)).getMoney());
                messages.get(id).getPerson2().
                        addMoney(((RedEnvelopeMessage) messages.get(id)).getMoney());
            } else if ((messages.get(id) instanceof EmojiMessage)) {
                if (emoji.containsKey(((EmojiMessage) messages.get(id)).getEmojiId())) {
                    emoji.put(((EmojiMessage) messages.get(id)).getEmojiId(),
                            emoji.get(((EmojiMessage) messages.get(id)).getEmojiId()) + 1);
                } else {
                    emoji.put(((EmojiMessage) messages.get(id)).getEmojiId(), 1);
                } }
            messages.remove(id);
        } else if (messages.containsKey(id) && messages.get(id).getType() == 1 &&
                messages.get(id).getGroup().hasPerson(messages.get(id).getPerson1())) {
            for (int i : people.keySet()) {
                if (messages.get(id).getGroup().hasPerson(people.get(i))) {
                    people.get(i).addSocialValue(messages.get(id).getSocialValue());
                } }
            //TODO
            if ((messages.get(id) instanceof RedEnvelopeMessage)) {
                int money = ((RedEnvelopeMessage) messages.get(id)).getMoney()
                        / messages.get(id).getGroup().getSize();
                messages.get(id).getPerson1().addMoney(-money *
                        (messages.get(id).getGroup().getSize() - 1));
                for (int i : people.keySet()) {
                    if (messages.get(id).getGroup().hasPerson(people.get(i)) &&
                            people.get(i).getId() != messages.get(id).getPerson1().getId()) {
                        people.get(i).addMoney(money); } }
            } else if ((messages.get(id) instanceof EmojiMessage)) {
                if (emoji.containsKey(((EmojiMessage) messages.get(id)).getEmojiId())) {
                    emoji.put(((EmojiMessage) messages.get(id)).getEmojiId(),
                            emoji.get(((EmojiMessage) messages.get(id)).getEmojiId()) + 1);
                } else {
                    emoji.put(((EmojiMessage) messages.get(id)).getEmojiId(), 1); } }
            messages.remove(id); } }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return people.get(id).getSocialValue(); } }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return people.get(id).getReceivedMessages(); } }

    @Override
    public boolean containsEmojiId(int id) {
        return this.emoji.containsKey(id); }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        //TODO
        if (emoji.containsKey(id)) {
            throw new MyEqualEmojiIdException(id);
        } else {
            emoji.put(id, 0); } }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return people.get(id).getMoney(); } }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!emoji.containsKey(id)) {
            throw new MyEmojiIdNotFoundException(id);
        } else {
            return emoji.get(id); } }

    @Override
    public int deleteColdEmoji(int limit) {
        //TODO
        emoji.entrySet().removeIf(entry -> entry.getValue() < limit);
        messages.entrySet().removeIf(entry -> (entry.getValue() instanceof EmojiMessage) &&
                !emoji.containsKey(((EmojiMessage) entry.getValue()).getEmojiId()));
        return emoji.size(); }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        //TODO
        if (!people.containsKey(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else {
            people.get(personId).getMessages().
                    removeIf(message -> message instanceof NoticeMessage); } }

    @Override
    public int queryBestAcquaintance(int id) throws PersonIdNotFoundException,
            AcquaintanceNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else if (people.containsKey(id) && ((MyPerson) people.get(id)).getDegree() == 0) {
            throw new MyAcquaintanceNotFoundException(id);
        } else {
            return ((MyPerson) people.get(id)).getBestAcquaintance().getId(); } }

    @Override
    public int queryCoupleSum() {
        return couple.size() / 2; }
    @Override
    public int queryLeastMoments(int id) throws PersonIdNotFoundException, PathNotFoundException {
        //TODO
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            int minDis = new Dij().getDis(id, people);
            if (minDis == Integer.MAX_VALUE) {
                throw new MyPathNotFoundException(id);
            }
            return minDis; } }

    @Override
    public int deleteColdEmojiOKTest(int limit, ArrayList<HashMap<Integer, Integer>> beforeData,
                                     ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        //TODO
        return new MyOkTest().deleteEmojiOkTest(limit, beforeData, afterData, result); }

    private Person find(Person person) {
        Person ancestor = person;
        findDepth = 0;
        while (ancestor != father.get(ancestor)) {
            ancestor = father.get(ancestor);
            findDepth++; }
        Person now = person;
        Person temp;
        while (now != ancestor) {
            temp = father.get(person);
            father.put(person, ancestor);
            now = temp; }
        return ancestor; }

    private void merge(Person person1, Person person2) {
        if (person1.getId() == person2.getId()) {
            return; }
        Person ancestor1 = find(person1);
        long findDepth1 = findDepth;
        Person ancestor2 = find(person2);
        long findDepth2 = findDepth;
        if (ancestor1.getId() == ancestor2.getId()) {
            return; }
        if (findDepth1 > findDepth2) {
            father.put(ancestor2, ancestor1);
        } else {
            father.put(ancestor1, ancestor2);
        }
        blockSum--; }
}
