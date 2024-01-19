package task3;

public class Process2 extends Thread {
    private final Somethings somethings;

    public Process2(Somethings somethings) {
        this.somethings = somethings;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (somethings) {
                if (somethings.getNumber() > 5) {
                    return;
                }
                if (somethings.getNumber() % 2 == 1) {
                    somethings.setNumber(somethings.getNumber() + 1);
                }
                somethings.notifyAll();
            }
            try {
                sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (somethings) {
                try {
                    somethings.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
