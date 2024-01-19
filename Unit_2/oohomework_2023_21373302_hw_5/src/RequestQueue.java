import com.oocourse.elevator1.PersonRequest;

import java.util.concurrent.CopyOnWriteArrayList;

public class RequestQueue {
    private boolean isEnd;
    private final CopyOnWriteArrayList<PersonRequest> requests;

    public RequestQueue() {
        this.requests = new CopyOnWriteArrayList<>();
        this.isEnd = false;
    }

    public synchronized void addRequest(PersonRequest request) {
        this.requests.add(request);
        this.notifyAll();
    }

    public synchronized PersonRequest getRequest() {
        if (this.requests.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (requests.isEmpty()) {
            return null;
        }
        PersonRequest request = this.requests.get(0);
        requests.remove(0);
        notifyAll();
        return request;
    }

    public synchronized PersonRequest lookRequest() {
        if (this.requests.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (requests.isEmpty()) {
            return null;
        }
        PersonRequest request = this.requests.get(0);
        notifyAll();
        return request;
    }

    public synchronized void setEnd(boolean end) {
        this.isEnd = end;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return this.isEnd;
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return requests.isEmpty();
    }

    public CopyOnWriteArrayList<PersonRequest> getRequests() {
        synchronized (requests) {
            return requests;
        }
    }

    public int size() {
        synchronized (requests) {
            return this.requests.size();
        }
    }

}
