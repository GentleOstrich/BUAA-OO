package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MyOkTest {

    public int deleteEmojiOkTest(int limit, ArrayList<HashMap<Integer, Integer>> beforeData,
                                 ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        // ensure1
        for (int i : beforeData.get(0).keySet()) {
            if (beforeData.get(0).get(i) >= limit) {
                if (!afterData.get(0).containsKey(i)) {
                    return 1;
                } } }
        // ensure2
        for (int i : afterData.get(0).keySet()) {
            if (!beforeData.get(0).containsKey(i) ||
                    !Objects.equals(beforeData.get(0).get(i), afterData.get(0).get(i))) {
                return 2; } }
        // ensure3
        int cnt = 0;
        for (int i : beforeData.get(0).keySet()) {
            if (beforeData.get(0).get(i) >= limit) {
                cnt++; } }
        if (afterData.get(0).size() != cnt) {
            return 3; }
        // ensure4 ---> nop ???
        for (int i :afterData.get(0).keySet()) {
            if (afterData.get(0).get(i) == null) {
                return 4; } }
        // ensure5
        for (int i : beforeData.get(1).keySet()) {
            if (beforeData.get(1).get(i) != null &&
                    afterData.get(0).containsKey(beforeData.get(1).get(i))) {
                if (!afterData.get(1).containsKey(i) ||
                        !Objects.equals(afterData.get(1).get(i), beforeData.get(1).get(i))) {
                    return 5; } } }
        // ensure6
        for (int i : beforeData.get(1).keySet()) {
            if (beforeData.get(1).get(i) == null) {
                if (!afterData.get(1).containsKey(i) ||
                        afterData.get(1).get(i) != null) {
                    return 6; } } }
        // ensure7
        cnt = 0;
        for (int i : beforeData.get(1).keySet()) {
            if (beforeData.get(1).get(i) == null) {
                cnt++;
            } else {
                if (afterData.get(0).containsKey(beforeData.get(1).get(i))) {
                    cnt++;
                } } }
        if (afterData.get(1).size() != cnt) {
            return 7; }
        // ensure8
        if (result != afterData.get(0).size()) {
            return 8; }
        return 0;
    }

}
