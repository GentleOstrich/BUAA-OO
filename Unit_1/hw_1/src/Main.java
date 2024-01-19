import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Process process = new Process();
        String input = scanner.nextLine();
        String precessed = process.preProcess(input).replace("0^0", "1").replace("^+","^");
        Lexer lexer = new Lexer(precessed);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr(1);

        String res = expr.toString();
        process.setPos(0);
        String output = process.preProcess(res).replace("^", "**");
        System.out.println(output);
    }
}