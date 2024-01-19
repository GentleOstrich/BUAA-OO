package task1;

public class MainClass {
    public static void main(String[] args) {
        Process1 process1 = new Process1();
        process1.start();
        Process2 process2 = new Process2();
        new Thread(process2).start();
        /* to complete(9), make process start*/
    }
}
