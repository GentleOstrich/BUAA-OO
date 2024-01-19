import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PreProcess preprocess = new PreProcess();
        ArrayList<Diy> diyList = new ArrayList<>();
        // 读入自定义函数
        int n = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < n; ++i) {
            if (scanner.hasNext()) {
                String diyString = scanner.nextLine();
                diyString = preprocess.preProcess(diyString);
                Diy diy = new Diy(diyString);
                diyList.add(diy);
            }
        }

        // 读入待展开表达式
        String input = scanner.nextLine();

        // 展开自定义函数
        ExpandDiy diyExpander = new ExpandDiy(diyList, input);
        String expanded = diyExpander.diyExpanded();
        String processed = preprocess.preProcess(expanded).replace("0^0", "1").replace("^+", "^");
        //System.out.println(processed);


        // 开始解析
        Lexer lexer = new Lexer(processed);
        Parser parser = new Parser(lexer);
        Expr unmergedExpr = parser.parseExpr();
        //System.out.println(unmergedExpr);

        Expr mergedExpr = unmergedExpr.mergeExpr().mergeExpr().mergeExpr().mergeExpr();
        String mergedString = preprocess.preProcess(mergedExpr.toString()).replace("^", "**");


        System.out.println(mergedString);
    }
}