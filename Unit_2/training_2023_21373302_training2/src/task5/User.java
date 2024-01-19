package task5;

public class User implements Observer {
    String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public void update(String msg) {
        System.out.println(this.name + ":" + msg);
    }
}
