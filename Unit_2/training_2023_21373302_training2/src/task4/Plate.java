package task4;


public class Plate {

    private final Good[] goods = new Good[1];
    private int cnt = 0;

    public int getCnt() {
        return this.cnt;
    }

    public Good[] getGoods() {
        return goods;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
