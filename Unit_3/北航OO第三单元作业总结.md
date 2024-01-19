# BUAA-OO-UNIT3

## 测试过程

### 黑箱测试、白箱测试的理解

所谓“箱子”，即为我们编写的程序。“白”以为着可见性，“黑”意味着不可见性。

* 白箱测试：即为结构测试。检验软件编码过程中发生的错误。程序员可以通过查看程序内部代码，即打开“箱子”，去检查这些错误。
* 黑箱测试：即为功能测试。这时的程序内部是不可见的，即无需考虑“箱子”的内部。我们只需要给“箱子”一个输入，观察其输出，判断其每个功能是否正确。

### 单元测试、功能测试、集成测试、压力测试、回归测试

**功能测试**下属数个测试，其中**单元测试**、**集成测试**、**回归测试**属于功能测试。

非功能测试也下属数个测试，其中**压力测试**属于非功能测试。

**单元测试**：对软件**中最小的可测试单元**进行检查和验证。在我们的作业中，可以指一个类，甚至是类里的某个方法。单元测试是在软件开发过程中要进行的**最低级别**的测试活动

**集成测试**：在单元测试的基础上，将不同单元模块“组装”起来，进行测试。因为某个单元可能单独测试是正确的，当多个单元之间产生联系、互相影响时，单靠单元测试就无法保证正确性了。一些问题只有在全局上才能反映出来。本次作业中，删边带来的并查集改变等问题涉及集成测试。

**回归测试**：修改了旧代码回，重新进行测试，以确认修改没有带来新的bug。

**压力测试**：不是在内存、CPU可用性、磁盘空间等电脑资源充足的条件进行测试，二十在资源相对匮乏的情况下进行测试。在本次作业中体现为对程序性能的要求，需要我们对算法进行优化。

## 架构设计、图模型构建、维护策略

### 架构设计

根据JML写代码，架构是课程组提供的，通过网络中人的熟人情况构建了一张图。为提高性能，需要自己实现相对高效的算法，功能必须符合JML提出的要求。

### 图模型构建

作业中每个人可视作图中的节点，用HashMap存储其熟人和熟人的value。熟人的value可视作图中边权。需要使用并查集以提高判断两个节点之间是否有通路的性能。

### 维护策略

第一次作业中使用了并查集，并对三角形数量、block数量进行动态维护。本次作业中不涉及删边，维护较为容易。

并查集简介：其实就是一棵树，将一个连通图的任一节点作为树根，其他节点作为树枝、叶子。

往网络中添加新人时，生成一个新的并查集，并block数量加一。

添加关系时，变量两个熟人是否有共同的熟人，如有，三角形数量加一。通过并查集看这两个熟人是否有共同的祖先，如果有，block数量减一，并且修改并查集。

并查集我使用HashMap存储，key是节点id，value是节点的父亲。

```java
private final HashMap<Person, Person> father;
```

并查集操作有两个方法find()和merge()，find()用来找节点的祖先，merge()用来合并两个并查集。

```java
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
```

```java
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
    	//将深度大的插入到深度小的树中，有利于find()操作
        if (findDepth1 > findDepth2) {
            father.put(ancestor2, ancestor1);
        } else {
            father.put(ancestor1, ancestor2);
        }
    	//如果能成功合并，那么block数量减一
        blockSum--;
    }
```

第二次作业中涉及删边，删边时要对并查集进行修改，同时对三角形数量、block数量进行修改。

删边时，要考虑是否修改并查集。我的做法时尝试生成两个并查集，用一个人的id广度优先遍历他的熟人，去生成一个并查集，如果这个过程中遇到了另一个人，那么就说明他俩还是连同的，放弃生成两个并查集，block不变。否则，再用另外一个人的id生成另一个并查集，同时block加一。

```java
private void updateFather(int id1, int id2) {
        ArrayList<Person> list = new ArrayList<>();
        list.add(people.get(id1));
        while (list.size() != 0) {
            Person person = list.get(0);
            if (person.getId() == id2) {
                tempFather = null;
                return;
            }
            tempFather.put(person, people.get(id1));
            for (Person person1 : ((MyPerson) person).getAcquaintance().keySet()) {
                if (person1.getId() == id2) {
                    tempFather = null;
                    return;
                }
                if (!tempFather.containsKey(person1)) {
                    list.add(person1);
                }
            }
            list.remove(person);
        }
    }
```

还要动态维护couple，当新增关系或者删除关系时，都要进行考虑。。我使用HashMap存储couple，如果id1和id2互为最好的熟人，那么就将<id1,id2>和<id2,id1>存入。在删除关系是，首先若HashMap中涉及id1或id2，那么就把涉及的都remove，之后再判断现在的熟人情况。

```java
private void updateCouple(int id1, int id2) throws PersonIdNotFoundException,
            AcquaintanceNotFoundException {
        if (couple.containsKey(id1)) {
            couple.remove(couple.get(id1));
            couple.remove(id1);
        }
        if (couple.containsKey(id2)) {
            couple.remove(couple.get(id2));
            couple.remove(id2);
        }

        if (((MyPerson) people.get(id1)).getDegree() != 0 &&
                id1 == queryBestAcquaintance(queryBestAcquaintance(id1))) {
            couple.put(id1, queryBestAcquaintance(id1));
            couple.put(queryBestAcquaintance(id1), id1);
        }

        if (((MyPerson) people.get(id2)).getDegree() != 0 &&
                id2 == queryBestAcquaintance(queryBestAcquaintance(id2))) {
            couple.put(id2, queryBestAcquaintance(id2));
            couple.put(queryBestAcquaintance(id2), id2);
        }
    }
```

三角形也是遍历两个的熟人，如果有共同的，那么三角形数量减一。

## 性能问题、规格与实现分离

### 性能问题

如果按照JML规格写代码，大量的O(n2)和部分O(n3)操作势必会导致强测TLE。所以必须并查集，动态维护等更加高效的方法，以及第三次作业中对dijkstra算法进行修改。

三次作业，强测无bug。

第一次作业中，互测被刀，原因是异常次数记录错误，修改一句就解决了。

### 规格与实现分离

规格可以看作是不会高效算法的甲方根据自己的需要写出的最朴素的伪代码。但这个规格代码覆盖全面，能百分百反应甲方的所有要求。如果我们完完全全根据规格代码写，是可以实现要求的所有功能。但由于规格代码过于朴素，性能很低，是不实用的。所以实现要满足规格的所有要求，并且追求更加高效、更加实用。

## OK测试检验代码实现与规格的一致性作用、改进建议

### OK测试检验代码实现与规格的一致性作用

OK测试检验代码就可以理解为上文所说的，逐字逐句，完完全全根据规格代码写出来代码。由于规格代码保证正确性，所以OK测试代码可以用于检验实现的正确性。

### 改进建议

编写OK测试代码主要考察学生对于规格代码的理解，如果能仔细阅读并理解规格代码的内容，编写OK测试代码就较为容易。相对于其他问题，理解规格代码难度不大，无改进建议。

## 学习体会

学会了怎么阅读JML规格代码，理解了规格与实现之间的区别，能够按照规格写出符合要求的代码，通过同学交流以及自己学习，掌握了一些更为高效的算法等。