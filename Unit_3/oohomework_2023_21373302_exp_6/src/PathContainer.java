import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PathContainer {
    //@ public instance model non_null Path[] pList;
    //@ public instance model non_null int[] pidList;
    private HashMap<Path, Integer> pathList;        // 描述id到path之间的映射关系
    private HashMap<Integer, Path> pathIdList;      // 描述path到id之间的映射关系，两个map加在一起对应上面两个数组

    private int idCounter;

    //@ public instance model non_null int[] nodes;
    //@ public instance model non_null int[] nodeToCount;
    private HashMap<Integer, Integer> globalNodesCount;     // 用一个HashMap实现规格中的nodes, nodeToCount两个数组

    public PathContainer() {
        pathList = new HashMap<>();
        pathIdList = new HashMap<>();
        globalNodesCount = new HashMap<>();
        idCounter = 0;
    }

    //@ ensures \result == pList.length;
    public /*@ pure @*/ int size() {      
        return pathList.size();
    }

    /*@ requires path != null;
      @ assignable \nothing;
      @ ensures \result == (\exists int i; 0 <= i && i < pList.length;
      @                     pList[i].equals(path));
      @*/
    public /*@ pure @*/ boolean containsPath(Path path) {
        if (path == null) {
            System.err.println("path in containsPath(path) is null !");
            return false;
        }
        return (pathList.get(path) != null);
    }

    /*@ ensures \result == (\exists int i; 0 <= i && i < pidList.length;
      @                      pidList[i] == pathId);
      @*/
    public /*@ pure @*/ boolean containsPathId(int pathId) {
        return (pathIdList.get(pathId) != null);
    }

    /*@ public normal_behavior
      @ requires containsPathId(pathId);
      @ assignable \nothing;
      @ ensures (\exists int i; 0 <= i && i < pList.length; pidList[i] == pathId && \result == pList[i]);
      @ also
      @ public exceptional_behavior
      @ requires !containsPathId(pathId);
      @ assignable \nothing;
      @ signals_only PathIdNotFoundException;
      @*/
    public /*@ pure @*/ Path getPathById(int pathId) throws PathIdNotFoundException {
        if (containsPathId(pathId)) {
            return pathIdList.get(pathId);
        }
        throw new PathIdNotFoundException(pathId);
    }

    /*@ public normal_behavior
      @ requires path != null && path.isValid() && containsPath(path);
      @ assignable \nothing;
      @ ensures (\exists int i; 0 <= i && i < pList.length; pList[i].equals(path) && pidList[i] == \result);
      @ also
      @ public exceptional_behavior
      @ requires path == null || !path.isValid() || !containsPath(path);
      @ signals_only PathNotFoundException;
      @*/
    public /*@ pure @*/ int getPathId(Path path) throws PathNotFoundException {
        if (path != null && path.isValid() && containsPath(path)) {
            return pathList.get(path);
        } else {
            throw new PathNotFoundException(path);
        }
    }

    //@ ensures \result == (\exists int i; 0 <= i < nodes.length; nodes[i] == node);
    public /*@ pure @*/ boolean containsNode(int node) {
        return globalNodesCount.containsKey(node);
    }

    /*@ normal_behavior
      @ requires containsNode(node);
      @ ensures (\exists int i; 0 <= i < nodes.length; nodes[i] == node && \result == nodeToCount[i]);
      @ also
      @ normal_behavior
      @ requires !containsNode(node);
      @ ensures \result == 0;
      @*/
    public /*@ pure @*/ int getNodeCount(int node) {
        return globalNodesCount.getOrDefault(node, 0);
    }

    /*@ normal_behavior
      @ requires path != null && path.isValid() && !containsPath(path);
      @ assignable pList, pidList, nodes, nodeToCount;
      @ ensures \result == \old(pList.length);
      @ ensures (\exists int i; 0 <= i && i < pList.length; pList[i].equals(path) &&
      @           \old(pList.length) == pidList[i]);
      @ ensures  pList.length == (\old(pList.length) + 1) &&
      @          pidList.length == (\old(pidList.length) + 1);
      @ ensures (\forall int i; 0 <= i && i < \old(pList.length);
      @           (\exists int j; 0 <= j && j < pList.length;
      @             \old(pList[i]).equals(pList[j]) && \old(pidList[i]) == pidList[j]));
      @ ensures (\forall int i; path.containsNode(i) || \old(this.containsNode(i));
      @          this.getNodeCount(i) == \old(this.getNodeCount(i)) + path.getNodeCount(i));
      @ also
      @ normal_behavior
      @ requires path == null || path.isValid() == false || containsPath(path);
      @ assignable \nothing;
      @ ensures \result == 0;
      @*/
    public int addPath(Path path) {
        if (path != null && path.isValid() && !containsPath(path)) {
            pathList.put(path, idCounter);
            pathIdList.put(idCounter, path);
            for (Integer node : path) {
                Integer prev = globalNodesCount.get(node);
                if (prev == null) {
                    globalNodesCount.put(node, 1);
                } else {
                    globalNodesCount.put(node, prev + 1);
                }
            }
            return idCounter++;
        }
        return 0;
    }

    /*@ public normal_behavior
      @ requires containsPathId(pathId);
      @ assignable pList, pidList, nodes, nodeToCount;
      @ ensures pList.length == (\old(pList.length) - 1) &&
      @          pidList.length == (\old(pidList.length) - 1);
      @ ensures (\forall int i; 0 <= i && i < \old(pList.length) && \old(pidList[i]) != pathId;
      @          (\exists int j; 0 <= j && j < pList.length;
      @             \old(pList[i]).equals(pList[j]) && pidList[i] == pidList[j]));
      @ ensures (\forall int i; 0 <= i && i < pidList.length; pidList[i] != pathId);
      @ ensures (\forall int i; 0 <= i && i < pList.length; !pList[i].equals(\old(getPathById(pathId))));
      @ ensures (\forall int i; \old(getPathById(pathId).containsNode(i)); this.getNodeCount(i) ==
      @             \old(this.getNodeCount(i)) - \old(getPathById(pathId).getNodeCount(i)));
      @ also
      @ public exceptional_behavior
      @ assignable \nothing;
      @ signals (PathIdNotFoundException e) !containsPathId(pathId);
      @*/
    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (containsPathId(pathId)) {
            Path path = pathIdList.remove(pathId);
            pathList.remove(path);
            for (Integer node : path) {
                Integer prev = globalNodesCount.get(node);
                globalNodesCount.put(node, prev - 1);
            }
        } else {
            throw new PathIdNotFoundException(pathId);
        }
    }


    /*@
      @ TODO 1: 编写规格
      @*/

    /*@ public normal_behavior

      @ assignable pList, pidList, nodes, nodeToCount;

      @ ensures (\forall int i; 0 <= i && i < \old(pList.length && \old(pList[i]).isUglyPath);
      @          !(\exists int j; 0 <= j && j < pList.length; \old(pList[i]).equals(pList[j]) || \old(pidList[i]) == pidList[j]);

      @ ensures (\forall int i; 0 <= i && i < \old(pList.length && !\old(pList[i]).isUglyPath);
      @          (\exists int j; 0 <= j && j < pList.length; \old(pList[i]).equals(pList[j]) || \old(pidList[i]) == pidList[j]);

      @ ensures (\forall int i; 0 <= i && i < \old(pList.length && \old(pList[i]).isUglyPath;
      @          (\exists int j; 0 <= j && j < pList.length; \old(pList[i]).extractLoopPath.equals(pList[j]));

      @ ensures (\forall int i, j; 0 <= i < j < pList.length; !pList[i].equals(pList[j]) && pList[i] != pList[j]);

      @ ensures pList.length == pidList.length

      @ ensures \old(pList).length <= pList.length && \old(pidList).length <= pidList.length

      @ ensures (\forall int i; 0 <= i < nodes.length; \old(nodeToCount[i]) - \sum (int c;
                    (\forall int j; 0 <= j < \old(pList).length;
                        (!\old(pList[j]).isUglyPath ==> c = 0;
                         \old(pList[j]).isUglyPath && !\old(pList[j]).extractLoopPath.containsNode(nodes[i]) ==> c = \old(pList[j]).getNodeCount(i);
                         \old(pList[j]).isUglyPath && \old(pList[j]).extractLoopPath.containsNode(nodes[i]) &&
                            (\exist k; 0 <= k < \old(pList).length; \old(pList[j]).extractLoopPath.equals(pList[k])) ==> c = \old(pList[j]).getNodeCount(i);
                         \old(pList[j]).isUglyPath && \old(pList[j]).extractLoopPath.containsNode(nodes[i]) && !(\exist k; 0 <= k < \old(pList).length;
                            \old(pList[j]).extractLoopPath.equals(pList[k])) ==> c = (\old(pList[j]).getNodeCount(i) - (pList[j]).getNodeCount(i));
                            c) = nodeToCount[i]);

      @ also
      @ public exceptional_behavior
      @ assignable \nothing;
      @ signals (LoopDuplicateException e) (/exists int i, j;
                                                0 <= i < j < i < \old(pList.length) && \old(pList[i]).isUglyPath && \old(pList[j]).isUglyPath && \old(pidList[i] != \old(pidList[j];
                                                    \old(pList[i]).extractLoopPath == \old(pList[j]).extractLoopPath));
      @*/

    public boolean beautifyPathsOk(
            HashMap<Path, Integer> beforePathList, HashMap<Path, Integer> afterPathList,
            HashMap<Integer, Path> beforePathIdList,  HashMap<Integer, Path> afterPathIdList,
            HashMap<Integer, Integer> beforeNodeCount,  HashMap<Integer, Integer> afterNodeCount) {
        
        ArrayList<Path> pList = new ArrayList<>();              // 对应 pList
        ArrayList<Path> oldPList = new ArrayList<>();           // 对应 \old(pList)
        ArrayList<Integer> pidList = new ArrayList<>();         // 对应 pidList
        ArrayList<Integer> oldPidList = new ArrayList<>();      // 对应 \old(pidList)
        ArrayList<Integer> nodes = new ArrayList<>();           // 对应 nodes
        ArrayList<Integer> oldNodes = new ArrayList<>();        // 对应 \old(nodes)
        ArrayList<Integer> nodeToCount = new ArrayList<>();     // 对应 nodeToCount
        ArrayList<Integer> oldNodeToCount = new ArrayList<>();  // 对应 \old(nodeToCount)

        // 构造 oldPList 和 oldPidList
        for (Map.Entry<Path, Integer> e : beforePathList.entrySet()) {
            if (!beforePathIdList.get(e.getValue()).equals(e.getKey())) {
                return false;
            }
            oldPList.add(e.getKey());
            oldPidList.add(e.getValue());
        }

        // 构造 pList 和 pIdList
        for (Map.Entry<Path, Integer> e : afterPathList.entrySet()) {
            if (!afterPathIdList.get(e.getValue()).equals(e.getKey())) {
                return false;
            }
            pList.add(e.getKey());
            pidList.add(e.getValue());
        }

        // 构造 nodes 和 oldNodes
        nodes.addAll(afterNodeCount.keySet());
        oldNodes.addAll(beforeNodeCount.keySet());

        // 构造 nodeToCount 和 oldNodeToCount
        nodeToCount.addAll(afterNodeCount.values());
        oldNodeToCount.addAll(beforeNodeCount.values());

        // TODO 2: 根据规格编写 ok 方法
        // ensure1
        for (Path path : oldPList) {
            if (path.isUglyPath()) {
                if (pList.contains(path) || pidList.contains(oldPidList.get(oldPList.indexOf(path)))) {
                    return false;
                }
            }
        }
        // ensure2
        for (Path path : oldPList) {
            if (!path.isUglyPath()) {
                if (!pList.contains(path) || !pidList.contains(oldPidList.get(oldPList.indexOf(path)))) {
                    return false;
                }
            }
        }
        // ensure3
        for (Path path : oldPList) {
            if (path.isUglyPath()) {
                Path bp = path.extractLoopPath();
                if (!pList.contains(bp)) {
                    return false;
                }
            }
        }
        // ensure4
        for (int i = 0; i < pList.size() - 1; ++i) {
            for (int j = i + 1; j < pList.size(); ++j) {
                if (pList.get(i).equals(pList.get(j))) {
                    return false;
                }
            }
        }
        // ensure5
        if (pList.size() != pidList.size()) {
            return false;
        }
        // ensure6
        if (pList.size() > oldPList.size()) {
            return false;
        }
        // ensure7
        for (int i = 0; i < oldNodes.size(); ++i) {
            int sum = 0;
            for (int j = 0; j < oldPidList.size(); ++j) {
                if (oldPList.get(j).isUglyPath() && !oldPList.get(j).extractLoopPath().containsNode(nodes.get(i))) {
                    sum += oldPList.get(j).getNodeCount(nodes.get(i));
                } else if (oldPList.get(j).isUglyPath() && oldPList.get(j).extractLoopPath().containsNode(nodes.get(i))) {
                    sum += oldPList.get(j).getNodeCount(nodes.get(i)) - oldPList.get(j).extractLoopPath().getNodeCount(nodes.get(i));
                }
            }
            if (oldNodeToCount.get(i) == sum) {
                if (nodes.contains(i)) {
                    return false;
                }
            }
            if (oldNodeToCount.get(i) + sum != nodeToCount.get(i)) {
                return false;
            }
        }
        return true;
    }

    public void beautifyPaths() throws LoopDuplicateException {
        // TODO 3: 编写业务代码
        ArrayList<Path> uglyPaths = new ArrayList<>();
        ArrayList<Path> beautifiedPaths = new ArrayList<>();
        for (Path path : pathList.keySet()) {
            if (path.isUglyPath()) {
                uglyPaths.add(path);
                beautifiedPaths.add(path.extractLoopPath());
            }
        }
        for (int i = 0; i < uglyPaths.size(); i++) {
            for (int j = i + 1; j < uglyPaths.size(); j++) {
                if (uglyPaths.get(i).equals(uglyPaths.get(j)) &&
                        beautifiedPaths.get(i).equals(beautifiedPaths.get(j))) {
                    throw new LoopDuplicateException();
                }
            }
        }
        for (int i = 0; i < uglyPaths.size(); i++) {
            Path up = uglyPaths.get(i);
            try {
                removePathById(pathList.get(up));
            } catch (PathIdNotFoundException e) {
                throw new RuntimeException(e);
            }
            addPath(beautifiedPaths.get(i));
        }
    }


    /*@ public normal_behavior
      @ assignable \nothing;
      @ ensures (\exists int[] arr;
      @            (\forall int i, j; 0 <= i && i < j && j < arr.length; arr[i] != arr[j]);
      @            (\forall int i; 0 <= i && i < arr.length;
      @                 (\exists Path p; this.containsPath(p); p.containsNode(arr[i]))) &&
      @            (\forall Path p; this.containsPath(p);
      @                 (\forall int node; p.containsNode(node);
      @                     (\exists int i; 0 <= i && i < arr.length; node == arr[i]))) &&
      @            (\result == arr.length));
      @*/
    public /*@ pure @*/ int getDistinctNodeCount() {
        int count = 0;
        for (Map.Entry<Integer, Integer> entry : globalNodesCount.entrySet()) {
            if (entry.getValue() > 0) {
                count++;
            }
        }
        return count;
    }
}
