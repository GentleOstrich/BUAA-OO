package task3;

public class MainClass {
    public static void main(String[] args) {
        Somethings somethings = new Somethings();
        Process1 process1 = new Process1(somethings);
        process1.start();
        Process2 process2 = new Process2(somethings);
        process2.start();
    }
}
