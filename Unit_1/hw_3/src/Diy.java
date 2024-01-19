import java.util.ArrayList;

public class Diy {
    private String name;
    private ArrayList<String> args;
    private ArrayList<String> definition;
    private String originalDiy;
    private int nextArgIndex;
    private Parser parser;
    private Lexer lexer;

    public Diy(String diy) {
        this.originalDiy = diy;
        this.definition = new ArrayList<>();
        this.args = new ArrayList<>();
        this.nextArgIndex = 0;

        String[] split = diy.split("=");
        String left = split[0];
        this.name = String.valueOf(left.charAt(0));
        String[] args = left.split("(f+\\(+)|(g+\\(+)|(h+\\(+)|\\)|,");
        for (int i = 1; i < args.length; ++i) {
            if (args[i].equals("x") || args[i].equals("y") || args[i].equals("z")) {
                this.args.add(args[i]);
            }
        }


        this.constructDefinition(split[1]);
    }

    public void constructDefinition(String right) {
        this.definition = new ArrayList<>();
        for (int i = 0; i < right.length(); ++i) {
            if (String.valueOf(right.charAt(i)).equals("d")) {
                this.definition.add(String.valueOf(right.charAt(i)));
                i++;
                this.definition.add(String.valueOf(right.charAt(i)));
            } else {
                if (this.args.contains(String.valueOf(right.charAt(i)))) {
                    int index = args.indexOf(String.valueOf(right.charAt(i)));
                    this.definition.add("arg" + String.valueOf(index));
                } else {
                    this.definition.add(String.valueOf(right.charAt(i)));
                }
            }
        }
    }

    public Diy diyCopy() {
        return new Diy(this.originalDiy);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : this.definition) {
            if (s.equals("arg0")) {
                sb.append("(").append(this.args.get(0)).append(")");
            } else if (s.equals("arg1")) {
                sb.append("(").append(this.args.get(1)).append(")");
            } else if (s.equals("arg2")) {
                sb.append("(").append(this.args.get(2)).append(")");
            } else {
                sb.append(s);
            }
        }
        return sb.toString();
    }

    public void changArg(String arg) {
        this.args.set(this.nextArgIndex, arg);
        this.nextArgIndex++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getArgs() {
        return args;
    }

    public void setArgs(ArrayList<String> args) {
        this.args = args;
    }

    public ArrayList<String> getDefinition() {
        return definition;
    }

    public void setDefinition(ArrayList<String> definition) {
        this.definition = definition;
    }

    public String getOriginalDiy() {
        return originalDiy;
    }

    public void setOriginalDiy(String originalDiy) {
        this.originalDiy = originalDiy;
    }

    public int getNextArgIndex() {
        return nextArgIndex;
    }

    public void setNextArgIndex(int nextArgIndex) {
        this.nextArgIndex = nextArgIndex;
    }
}

