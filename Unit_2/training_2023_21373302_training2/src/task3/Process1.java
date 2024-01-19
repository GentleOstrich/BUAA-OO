package task3;

public class Process1 extends Thread {
    private final Somethings somethings;

    public Process1(Somethings somethings) {
        this.somethings = somethings;
    }

    @Override
    public void run() {
        synchronized (somethings) {
            if (somethings.getNumber() > 5) {
                return;
            }
            try {
                somethings.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (somethings.getNumber() % 2 == 0) {
                somethings.setNumber(somethings.getNumber() + 1);
            }
            somethings.notifyAll();
        }
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
