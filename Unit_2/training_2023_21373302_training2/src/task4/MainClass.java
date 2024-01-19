package task4;

public class MainClass {
    public static void main(String[] args) {
        Plate plate = new Plate();
        Producer producer = new Producer(plate);
        Consumer consumer = new Consumer(plate);
        new Thread(producer).start();
        new Thread(consumer).start();
    }
}
