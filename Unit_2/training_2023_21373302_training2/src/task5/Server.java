package task5;

import java.util.ArrayList;

public class Server implements Observerable {
    private final ArrayList<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObserver(String msg) {
        System.out.println("server:" + msg);
        for (Observer observer : this.observers) {
            observer.update(msg);
        }
    }
}
