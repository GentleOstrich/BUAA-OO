package task2;

public class Process1 extends Thread {
    static int staticVar = 0;
    int classVar = 0;

    @Override
    public void run() {
        add();
        add();
        System.out.println(Thread.currentThread().getName() +
                " staticVar = " +staticVar);
        System.out.println(Thread.currentThread().getName() +
                " classVar = " + this.classVar);
    }

    private void add() {
        int localVar = 0;
        staticVar++;
        classVar++;
        localVar++;
        System.out.println(Thread.currentThread().getName() +
                " localVar = " +localVar);
    }
}
