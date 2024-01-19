package task4;

public class Producer implements Runnable {
    private final Plate plate;

    public Producer(Plate plate) {
        this.plate = plate;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            synchronized (plate) {
                if (plate.getCnt() > 0) {
                    try {
                        plate.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("Producer put:" + i);
                plate.getGoods()[plate.getCnt()] = new Good(i);
                plate.setCnt(plate.getCnt() + 1);
                plate.notifyAll();
            }
        }
    }
}
