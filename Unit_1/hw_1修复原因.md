我总结测试点不通过原因在于：符号的存储不合适，在开括号时符号分配极为混乱，导致最终答案错误。

修复前，我采用的是表达式乘表达式，项乘项。项乘项时，需要考虑每个因子的符号，之后在把因子的符号给到项目，但因子也有表达式，之后输出时表达式通过项的符号输出正负号，然后再依次输出表达式中的各个因子……所以，开括号时，表达式，项，因子都需要进行符号更替。

* 这种方法是不简洁且出错率高的，对于符号管理很混乱，尤其是在开括号时。经过长时间的修复后，我没能使用该方法通过修复测试。

* 由于本人领悟能力差，做得很慢，所以对修复前的输出未进行合并。

基于以上两个原因，与同学交流并深入思考后，我决定换一种储存形式：

* 项里存储项的系数及系数的符号，表达式因子和正常因子保存各自的符号，在拆括号时，将表达式因子的符号给到其下每个term的系数符号。这样，我保证了在拆括号时符号的正确性。

* 此外，我放弃Arraylist<因子>，使用用Hashmap<因子，因子的系数>的形式保存因子，这有利于进行合并。

修复后，符号处理正确，测试就都通过了。



（附：Term类中括号展开的方法：返回一个表达式，我对这个方法很满意）

```java
public Expr expand() {
    boolean hasExpr = false;
    Expr expr = new Expr(); // create
    for (Factor factor : this.factors.keySet()) {
        if (factor instanceof Expr) {
            hasExpr = true;
            if (factor.getSign() == -1) { // if expr is negative then invert every term's sign
                for (Term term : ((Expr) factor).getTerms()) {
                    term.setCoefficientSign(term.getCoefficientSign() * -1);
                }
                factor.setSign(1);
            }
            // factor is (-a*-b - b)
            // 造一个个新的term，里面包含括号里的每个term里的每个factor和括号外的全体factor和
            for (Term term1 : ((Expr) factor).getTerms()) {
                Term term = new Term();
                term.setCoefficient(term.coefficient.multiply(term1.getCoef()).
                        multiply(this.coefficient));
                term.setCoefficientSign(term.coefficientSign * term1.coefficientSign *
                        this.coefficientSign);
                if (!term1.getFactors().isEmpty()) {
                    for (Factor factor1 : term1.factors.keySet()) {
                        term.addFactor(factor1, term1.getFactors().get(factor1));
                    }
                }
                for (Factor factor1 : this.factors.keySet()) {
                    if (!factor1.equals(factor)) {
                        term.addFactor(factor1, this.factors.get(factor1));
                    }
                }
                expr.getTerms().addAll(term.expand().getTerms());
                // 此处使用递归我认为是很巧妙的一个点
            }
        }
        if (hasExpr) {
            break;
        }
    }
    if (!hasExpr) {
        Term term = new Term();
        term.setCoefficient(this.coefficient);
        term.setCoefficientSign(this.coefficientSign);
        for (Factor factor1 : this.factors.keySet()) {
            term.addFactor(factor1, this.getFactors().get(factor1));
        }
        expr.addTerm(term);
    }
    return expr;
}
```

