## hw_1

多变量多项式的括号展开

扩展BNF描述

加减乘除乘方括号，展开括号

基本概念：

带符号，支持前导0，十进制



符号交给谁保存以及括号展开的方式

括号展开的本质是展开term中的expr

term: -a*-b*-(a*-b+b)*

term之间用+号相连就好了，把符号的储存交给factor

-a*-b*(-a*-b-b)



造一个个新的term，里面包含括号外的全体factor和括号里的每个term里的每个factor

-(x-y)**3-x**2*(-x+0) - (+y)*y*y**1 + -3*x *y*(+x-y)





-x**2*(-x+0)



```java
process.setPos(0);
String unMergedString = process.preProcess(unmergedExpr.toString()).replace("^", "**");
System.out.println(unMergedString);
```

