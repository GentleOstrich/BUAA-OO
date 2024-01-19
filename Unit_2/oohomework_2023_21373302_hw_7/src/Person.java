public class Person extends com.oocourse.elevator3.PersonRequest {
    public Person(int fromFloor, int toFloor, int personId, boolean needTrans) {
        super(fromFloor, toFloor, personId);
        this.needTrans = needTrans;
    }

    private boolean needTrans;

    public boolean getNeedTrans() {
        return needTrans;
    }

    public void setNeedTrans(boolean needTrans) {
        this.needTrans = needTrans;
    }
}
