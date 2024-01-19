# 面向对象第五次实验指导书

## 实验仓库

>公共信息发布区：[exp5_public](http://gitlab.oo.buaa.edu.cn/2023_public/experiment/exp5_public)。

>个人仓库：oo_homework_2023 / homework_2023_你的学号_exp_5。

>请同学们作答完成后，将json文件与代码**提交到个人仓库**，并将json文件内容**填入内容提交区**,再在本页面下方选择对应的 `commit`，最后**点击最下方的按钮进行提交**。详细的提交过程请参见*提交事项*章节。

## 实验目标

1. 了解 Java 的垃圾回收机制
2. 训练==根据 JML 补充代码==及==撰写 JML 的能力==
3. 尝试以补全的形式，==为一个方法撰写 JML==

## 背景引入

与 C++ 程序设计语言相比，Java 程序设计语言拥有一个独特的语言特性——自动垃圾回收机制 (Garbage Collection)。在 Java 和 C++ 中，新创建一个对象都需要使用 `new` 运算符。然而，在 C++ 中，程序员需要人工管理内存，对于不再使用的对象使用 `delete` 运算符显式地回收内存；在 Java 中，程序员无需人工管理内存，JVM 会自动触发垃圾回收，将没有被引用的对象占据的内存空间释放。

### 垃圾回收机制

 基本的 Java 垃圾回收机制如下：

首先，垃圾回收器会找出当前哪些对象是==正在使用中的==，并将其标记为==存活对象==；以及哪些对象是==没有被引用的==，并将其标记为==未引用对象==，这一步称为**标记** 。下图显示了一个标记前后的内存图的样式：

![img](https://oscimg.oschina.net/oscnet/1459c0fb17fd074a53898d29839ebfaf5ac.jpg)

其次，垃圾回收器会将当前所有==未引用对象删除==，也就是上图中橙色的部分。

![img](https://oscimg.oschina.net/oscnet/e6c1fbbf3a3d2d31365d5b74ae5d111333b.jpg)

最后，为了提升性能，在删除完未引用对象后，通常还会采取**压缩**操作，将内存中的存活对象==放置在一起==，以便后续能够更加高效快捷地分配新的对象。

![img](https://oscimg.oschina.net/oscnet/01b56829ee40fe72ebca25d554f1adcefce.jpg)

## 实验任务

本次实验我们将会采用==标记-压缩方法==，实现一个简单的 JVM 垃圾回收机制，模拟垃圾回收的工作流程。

仓库中会给出 src 文件夹，有如下几个类：

- `MyObject`
  - 模拟创建的对象
- `MyHeap`
  - 普通小顶堆
- `JvmHeap`
  - JVM 中的堆，继承自`MyHeap`。
- `MyJvm`
  - 模拟的 JVM，负责管理堆、创建对象、删除对象引用和垃圾回收
- `Main`
  - 模拟程序的输入输出，输入方式为先输入指令名称，换行后再输入参数。
  - 输入有以下几条指令：
    - `CreateObject` ：创建新的对象，换行后输入创建对象的个数
    - `SetUnreferenced` ：将对象设置为未引用，换行后输入删除引用的对象id，用空格分隔
    - `SnapShot` ：查看当前 JVM 中堆的快照

注意：JVM 中的“堆”实际上是一段内存。在本次实验中，为了方便数据操作，我们使用了小顶堆来实现这一概念。

**任务清单:**

任务分为`JML补全`和`代码补全`,其中要求为 MyJvm 类中的 ==createObject 方法较为完整地编写规格==

1. `JvmHeap` 类：按要求补全 [1] 中规格
2. `JvmHeap` 类：按要求补全 [2]-[3] 中代码
3. `MyJvm` 类：按要求补全 [4-1]-[4-8] 中规格

**提交事项：**

1. 需要填空的地方在程序中已用 [1]，[2] 等序号标注。

2. 对于 JML 中的临时变量的使用，按照 `i`，`j`，`k` 的次序依次使用。

3. 第一部分任务的答案放在 answer.json 文件中提交，**并同时**需要将作答内容全部写在内容提交区。提交示例如下：

   ```json
   {
     "1": "",
     "2": "",
     "3": "",
     "4-1": "",
     "4-2": "",
     "4-3": "",
     "4-4": "",
     "4-5": "",
     "4-6": "",
     "4-7": "",
     "4-8": ""
    }
   ```

   **注：各答案的结尾无需带分号**

4. 对于 [2] - [3] ，==直接在官方文件夹 src 中作更改==，并连同 answer.json 文件提交。提交目录应包括子目录 src 及 answer.json 文件，即 answer.json 文件不放在 src 子目录中。因此，你的仓库布局应当如下所示：

```
homework_2023_你的学号_exp_5 // 仓库目录
| - answer.json             // [1]-[4]的答案
| - src           // [2]-[3]同步补全代码
| - | - JvmHeap.java
| - | - Main.java
| - | - MyHeap.java
| - | - MyJvm.java
| - | - MyObject.java
```


   **注意：本次实验要求编译成功**

**无需关注的部分：**

以下部分均由课程组封装好，实验过程中无需关注：

1. Main.java 文件
2. `MyHeap` 类 `add`方法的具体实现，但要清楚成员变量 `size` 的变化时机
3. `MyJvm` 类的 `getSnapShot` 方法

**输入输出样例:**

输入

```
CreateObject
5
SetUnreferenced
1 4
SnapShot
CreateObject
10
SetUnreferenced
2 6 10 15
CreateObject
5
SnapShot
```

输出

```
Create 5 Objects.
Set id: 1 Unreferenced Object.
Set id: 4 Unreferenced Object.
Heap: 5
0 1 2 3 4 
the youngest one's id is 0

---------------------------------
Create 10 Objects.
Set id: 2 Unreferenced Object.
Set id: 6 Unreferenced Object.
Set id: 10 Unreferenced Object.
Set id: 15 Unreferenced Object.
Heap reaches its capacity,triggered Garbage Collection.
Create 5 Objects.

Heap: 15
0 3 5 7 8 9 11 12 13 14 15 16 17 18 19 
the youngest one's id is 0

---------------------------------
```

关于实验测试数据：

1. 不需要同学们考虑异常的情况
2. 不需要同学们考虑数据很大的情况
3. 不需要同学们考虑性能相关实现

## 提示

本题需要同学们在短时间内==完成Java垃圾回收机制的阅读理解、JML规格与Java代码的阅读以及对应题目的填写==。

Java垃圾回收机制是整个代码的关键；请确保==充分理解该机制==之后再进行填空。

关于JML与代码，如果感觉时间吃紧，可以试着==从方法名上猜测该方法的具体行为==，再与JML对照来加快代码阅读速度。此外，在填写代码时可以考虑如何使用代码中已经提供的方法。

此外，如果有一定的空余时间，建议测试一下你所补完的程序，在熟悉Java垃圾回收机制的同时检测代码中的错误。
