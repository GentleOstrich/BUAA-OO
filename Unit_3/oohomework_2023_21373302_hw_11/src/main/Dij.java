package main;

import com.oocourse.spec3.main.Person;
import javafx.util.Pair;

import java.util.Objects;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.HashMap;

public class Dij {
    public int getDis(int id1, HashMap<Integer, Person> people) {
        Comparator<Pair<Integer, Integer>> cmp = Comparator.comparingInt(Pair::getValue);
        HashMap<Integer, Integer> dis1 = new HashMap<>(); //最短路径
        HashMap<Integer, Integer> dis2 = new HashMap<>(); //次短路经
        HashMap<Integer, Integer> ori1 = new HashMap<>(); //最短路径的出点
        HashMap<Integer, Integer> ori2 = new HashMap<>(); //次短路径的出点
        PriorityQueue<Pair<Integer, Integer>> queue = new PriorityQueue<>(cmp);
        HashSet<Integer> vis = new HashSet<>();
        vis.add(id1);
        for (Person person : ((MyPerson) people.get(id1)).getAcquaintance().keySet()) {
            int id = person.getId();
            dis1.put(id, people.get(id1).queryValue(person));
            ori1.put(id, id);
            queue.offer(new Pair<>(id, dis1.get(id)));
        }
        while (!queue.isEmpty()) {
            Pair<Integer, Integer> u = queue.poll();
            if (vis.contains(u.getKey())) {
                continue;
            }
            vis.add(u.getKey());
            MyPerson up = (MyPerson) people.get(u.getKey());
            for (Person person : up.getAcquaintance().keySet()) {
                int newDis = dis1.get(u.getKey()) + up.queryValue(person);
                if (!dis1.containsKey(person.getId()) || newDis < dis1.get(person.getId())) {
                    if (ori1.containsKey(person.getId()) &&
                            !Objects.equals(ori1.get(person.getId()), ori1.get(up.getId()))) {
                        dis2.put(person.getId(), dis1.get(person.getId()));
                        ori2.put(person.getId(), ori1.get(person.getId()));
                    }
                    dis1.put(person.getId(), newDis);
                    ori1.put(person.getId(), ori1.get(up.getId()));
                } else if (!dis2.containsKey(person.getId()) || newDis < dis2.get(person.getId())) {
                    if (ori1.containsKey(person.getId()) &&
                            !Objects.equals(ori1.get(person.getId()), ori1.get(up.getId()))) {
                        dis2.put(person.getId(), newDis);
                        ori2.put(person.getId(), ori1.get(up.getId()));
                    }
                }
                if (dis2.containsKey(u.getKey())) {
                    newDis = dis2.get(u.getKey()) + up.queryValue(person);
                    if (!dis1.containsKey(person.getId()) || newDis < dis1.get(person.getId())) {
                        if (ori1.containsKey(person.getId()) &&
                                !Objects.equals(ori1.get(person.getId()), ori2.get(up.getId()))) {
                            dis2.put(person.getId(), dis1.get(person.getId()));
                            ori2.put(person.getId(), ori1.get(person.getId()));
                        }
                        dis1.put(person.getId(), newDis);
                        ori1.put(person.getId(), ori2.get(up.getId()));
                    } else if (!dis2.containsKey(person.getId()) ||
                            newDis < dis2.get(person.getId())) {
                        if (ori1.containsKey(person.getId()) &&
                                !Objects.equals(ori1.get(person.getId()), ori2.get(up.getId()))) {
                            dis2.put(person.getId(), newDis);
                            ori2.put(person.getId(), ori2.get(up.getId()));
                        }
                    }
                }
                queue.offer(new Pair<>(person.getId(), dis1.get(person.getId())));
            }
        }
        int ret = Integer.MAX_VALUE;
        for (int i : people.keySet()) {
            int n = dis1.getOrDefault(i, Integer.MAX_VALUE)
                    + dis2.getOrDefault(i, Integer.MAX_VALUE);
            if (n > 0 && n < ret && i != id1) {
                ret = n;
            }
        }
        return ret;
    }
}
