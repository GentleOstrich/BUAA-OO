package main;

import java.util.HashMap;
import java.util.Objects;

public class MyOkTest {
    private int exceptionTest(HashMap<Integer,
            HashMap<Integer, Integer>> beforeData, HashMap<Integer,
            HashMap<Integer, Integer>> afterData) {
        if (beforeData.size() != afterData.size()) {
            return -1;
        } else {
            for (int id1 : beforeData.keySet()) {
                if (!afterData.containsKey(id1) ||
                        beforeData.get(id1).size() != afterData.get(id1).size()) {
                    return -1;
                } else {
                    for (int id2 : beforeData.get(id1).keySet()) {
                        if (!afterData.get(id1).containsKey(id2) ||
                                !Objects.equals(afterData.get(id1).get(id2),
                                        beforeData.get(id1).get(id2))) {
                            return -1;
                        }
                    }
                }
            }
            return 0;
        }
    }

    private int conditionTest1(int id1, int id2, int value, HashMap<Integer,
            HashMap<Integer, Integer>> beforeData, HashMap<Integer,
            HashMap<Integer, Integer>> afterData) {
        if (!afterData.get(id1).containsKey(id2) || !afterData.get(id2).containsKey(id1)) {
            return 4;
        }
        if (afterData.get(id1).get(id2) != beforeData.get(id1).get(id2) + value) {
            return 5;
        }
        if (afterData.get(id2).get(id1) != beforeData.get(id2).get(id1) + value) {
            return 6;
        }
        if (afterData.get(id1).size() != beforeData.get(id1).size()) {
            return 7;
        }
        if (afterData.get(id2).size() != beforeData.get(id2).size()) {
            return 8;
        }
        for (int innerId : beforeData.get(id1).keySet()) {
            if (!afterData.get(id1).containsKey(innerId)) {
                return 9;
            }
        }
        for (int innerId : beforeData.get(id2).keySet()) {
            if (!afterData.get(id2).containsKey(innerId)) {
                return 10;
            }
        }
        for (int innerId : afterData.get(id1).keySet()) {
            if (innerId != id2) {
                if (!Objects.equals(afterData.get(id1).get(innerId),
                        beforeData.get(id1).get(innerId))) {
                    return 11;
                }
            }
        }
        for (int innerId : afterData.get(id2).keySet()) {
            if (innerId != id1) {
                if (!Objects.equals(afterData.get(id2).get(innerId),
                        beforeData.get(id2).get(innerId))) {
                    return 12;
                }
            }
        }
        return 0;
    }

    private int conditionTest2(int id1, int id2, HashMap<Integer,
            HashMap<Integer, Integer>> beforeData, HashMap<Integer,
            HashMap<Integer, Integer>> afterData) {
        if (afterData.get(id1).containsKey(id2) || afterData.get(id2).containsKey(id1)) {
            return 15;
        }
        if (beforeData.get(id1).size() != afterData.get(id1).size() + 1) {
            return 16;
        }
        if (beforeData.get(id2).size() != afterData.get(id2).size() + 1) {
            return 17;
        }
        // how to return 18 adn 19
        for (int innerId : afterData.get(id1).keySet()) {
            if (!beforeData.get(id1).containsKey(innerId) ||
                    !Objects.equals(beforeData.get(id1).get(innerId),
                            afterData.get(id1).get(innerId))) {
                return 20;
            }
        }
        for (int innerId : afterData.get(id2).keySet()) {
            if (!beforeData.get(id2).containsKey(innerId) ||
                    !Objects.equals(beforeData.get(id2).get(innerId),
                            afterData.get(id2).get(innerId))) {
                return 21;
            }
        }
        return 0;
    }

    public int modifyRelationOkTest(int id1, int id2, int value, HashMap<Integer,
            HashMap<Integer, Integer>> beforeData, HashMap<Integer,
            HashMap<Integer, Integer>> afterData) {
        if (!beforeData.containsKey(id1) || !beforeData.containsKey(id2) ||
                id1 == id2 || !beforeData.get(id1).containsKey(id2)) {
            return exceptionTest(beforeData, afterData);
        }
        if (beforeData.size() != afterData.size()) {
            return 1;
        }
        for (int oldId : beforeData.keySet()) {
            if (!afterData.containsKey(oldId)) {
                return 2;
            }
        }
        for (int oldId : beforeData.keySet()) {
            if (oldId != id1 && oldId != id2) {
                if (beforeData.get(oldId).size() != afterData.get(oldId).size()) {
                    return 3;
                }
                for (int innerId : beforeData.get(oldId).keySet()) {
                    if (!afterData.get(oldId).containsKey(innerId) ||
                            !Objects.equals(beforeData.get(oldId).get(innerId),
                                    afterData.get(oldId).get(innerId))) {
                        return 3;
                    }
                }
            }
        }
        if (beforeData.containsKey(id1) && beforeData.containsKey(id2) &&
                id1 != id2 && beforeData.get(id1).containsKey(id2) &&
                beforeData.get(id1).get(id2) + value > 0) {
            return conditionTest1(id1, id2, value, beforeData, afterData);
            // how to return 13 and 14
        }
        if (beforeData.containsKey(id1) && beforeData.containsKey(id2) &&
                id1 != id2 && beforeData.get(id1).containsKey(id2) &&
                beforeData.get(id1).get(id2) + value <= 0) {
            return conditionTest2(id1, id2, beforeData, afterData);
        }
        return 0;
    }
}
