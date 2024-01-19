import java.util.ArrayList;

public class ExpandDiy {
    private final ArrayList<Diy> diyList;
    private String input;
    private int pos;

    public ExpandDiy(ArrayList<Diy> diyList, String input) {
        this.diyList = diyList;
        this.input = input;
        this.pos = 0;
    }

    public String diyExpanded() {
        while (input.contains("f") || input.contains("g") || input.contains("h")) {
            StringBuilder sb = new StringBuilder();
            pos = 0;
            while (pos < this.input.length()) {
                if (input.charAt(pos) != 'f' && input.charAt(pos) != 'g'
                        && input.charAt(pos) != 'h') {
                    sb.append(input.charAt(pos));
                } else {
                    char name = input.charAt(pos);
                    pos += 2;
                    //sb.append(this.expand((String.valueOf(name))));
                    sb.append("(").append(this.expand((String.valueOf(name)))).append(")");
                }
                ++pos;
            }
            this.input = sb.toString();
        }
        return input;
    }

    public String expand(String name) {
        int brackets = 1;
        for (Diy diy : diyList) {
            if (diy.getName().equals(name)) {
                Diy diyCopy = diy.diyCopy();
                StringBuilder sonSb = new StringBuilder();
                while (brackets != 0) {
                    if (input.charAt(pos) == ')') {
                        brackets--;
                        if (brackets != 0) {
                            sonSb.append(")");
                        } else {
                            break;
                        }
                    } else if (input.charAt(pos) == '(') {
                        sonSb.append("(");
                        brackets++;
                    } else if (input.charAt(pos) == 'f' || input.charAt(pos) == 'g'
                            || input.charAt(pos) == 'h') {
                        char name1 = input.charAt(pos);
                        pos += 2;
                        //sonSb.append(this.expand((String.valueOf(name1))));
                        sonSb.append("(").append(this.expand((String.valueOf(name1)))).append(")");
                    } else if (input.charAt(pos) != ',') {
                        sonSb.append(input.charAt(pos));
                    }
                    if (input.charAt(pos) == ',') {
                        diyCopy.changArg(sonSb.toString());
                        sonSb = new StringBuilder();
                    }
                    ++pos;
                }
                if (!sonSb.toString().equals("")) {
                    diyCopy.changArg(sonSb.toString());
                }
                return diyCopy.toString();
            }
        }
        return "";
    }
}
