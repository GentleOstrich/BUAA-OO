public class Floors {
    private final int[] mfloors;
    private final int[] nfloors;

    public Floors() {
        this.mfloors = new int[12];
        this.nfloors = new int[12];
    }

    public synchronized void startMOpen(int floor, boolean flag) {
        if (flag) {
            while (this.mfloors[floor] >= 4 || this.nfloors[floor] >= 2) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            this.mfloors[floor]++;
            this.nfloors[floor]++;
        } else {
            while (this.mfloors[floor] >= 4) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            this.mfloors[floor]++;
        }
    }

    public synchronized void removeMElevator(int floor, boolean flag) {
        if (flag) {
            this.mfloors[floor]--;
            this.nfloors[floor]--;
        } else {
            this.mfloors[floor]--;
        }
        notifyAll();
    }
    
}
