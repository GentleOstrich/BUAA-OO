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
                String[] split = diyString.split("=");

                String right = preprocess.preProcess(split[1]);

                ExpandDiy diyExpander = new ExpandDiy(diyList, preprocess.preProcess(right));
                right = preprocess.preProcess(diyExpander.diyExpanded()).
                        replace("0^0", "1").replace("^+", "^");

                Lexer lexer = new Lexer(right);
                Parser parser = new Parser(lexer);


                String processed = preprocess.preProcess(parser.parseExpr().toString());


                Diy diy = new Diy(split[0] + "=" + processed);
                diyList.add(diy);
            }
        }

        // 读入待展开表达式
        String input = scanner.nextLine();

        // 展开自定义函数
        ExpandDiy diyExpander = new ExpandDiy(diyList, preprocess.preProcess(input));
        String expanded = diyExpander.diyExpanded();
        String processed = preprocess.preProcess(expanded).replace("0^0", "1").replace("^+", "^");
        //System.out.println(processed);


        // 开始解析
        Lexer lexer = new Lexer(processed);
        Parser parser = new Parser(lexer);
        Expr unmergedExpr = parser.parseExpr();
        //System.out.println(unmergedExpr);

        Expr mergedExpr = unmergedExpr.mergeExpr();
        String mergedString = preprocess.preProcess(mergedExpr.toString()).replace("^", "**");


        System.out.println(mergedString);
    }
}