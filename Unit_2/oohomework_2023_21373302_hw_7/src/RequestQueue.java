import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.util.concurrent.CopyOnWriteArrayList;

public class RequestQueue {
    private boolean isEnd;
    private final CopyOnWriteArrayList<Request> requests;
    private int inputCount;

    public RequestQueue() {
        this.requests = new CopyOnWriteArrayList<>();
        this.isEnd = false;
        this.inputCount = 0;
    }

    public synchronized void addRequest(Request request) {
        this.requests.add(request);
        this.notifyAll();
    }

    public synchronized Request getRequest() {
        if (this.requests.isEmpty()) {
            try {
                this.wait(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (requests.isEmpty()) {
            return null;
        }
        Request request = this.requests.get(0);
        requests.remove(0);
        notifyAll();
        return request;
    }

    public synchronized PersonRequest lookRequest() {
        if (this.requests.isEmpty() && this.inputCount == 0) {
            try {
                this.wait(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (requests.isEmpty()) {
            return null;
        }
        Request request = this.requests.get(0);
        return (PersonRequest) request;
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

    public CopyOnWriteArrayList<Request> getRequests() {
        synchronized (requests) {
            return requests;
        }
    }

    public int size() {
        synchronized (requests) {
            return this.requests.size();
        }
    }

    public synchronized boolean isInputing() {
        notifyAll();
        return this.inputCount != 0;
    }

    public synchronized void changeInputCount(boolean m) {
        if (m) {
            this.inputCount++;
        } else {
            this.inputCount--;
        }
        notifyAll();
    }

    public int getInputCount() {
        return inputCount;
    }
}
