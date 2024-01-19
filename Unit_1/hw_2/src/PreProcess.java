public class PreProcess {
    public String preProcess(String unprocessed) {
        String processed = unprocessed.replace("\t", "").replace(" ", "");
        while (processed.contains("--")) {
            processed = processed.replace("--", "+");
        }
        while (processed.contains("++")) {
            processed = processed.replace("++", "+");
        }
        while (processed.contains("+-")) {
            processed = processed.replace("+-", "-");
        }
        while (processed.contains("-+")) {
            processed = processed.replace("-+", "-");
        }
        while (processed.contains("**")) {
            processed = processed.replace("**", "^");
        }
        StringBuilder sb = new StringBuilder();
        int pos = 0;
        while (pos < processed.length()) {
            if (processed.charAt(pos) == '0') {
                if (pos < processed.length() - 1 && pos > 0 &&
                        !Character.isDigit(processed.charAt(pos - 1)) &&
                        Character.isDigit(processed.charAt(pos + 1))) {
                    while (pos < processed.length() - 1 && processed.charAt(pos) == '0' &&
                            Character.isDigit(processed.charAt(pos + 1))) {
                        ++pos;
                    }
                }
            }
            sb.append(processed.charAt(pos));
            ++pos;
        }
        return sb.toString().replace(" ", "").replace("**", "^").
                replace("++", "+").replace("--", "+").
                replace("+-", "-").replace("-+", "-");
    }
}
