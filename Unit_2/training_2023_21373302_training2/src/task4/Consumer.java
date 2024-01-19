package task4;

public class Consumer implements Runnable {
    private final Plate plate;

    public Consumer(Plate plate) {
        this.plate = plate;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            synchronized (plate) {
                if (plate.getCnt() == 0) {
                    try {
                        plate.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                Good good = plate.getGoods()[plate.getCnt() - 1];
                plate.setCnt(plate.getCnt() - 1);
                System.out.println("Consumer get:" + good.getId());
                plate.notifyAll();
            }
        }
    }
}
