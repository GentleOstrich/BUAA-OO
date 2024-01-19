import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.util.concurrent.CopyOnWriteArrayList;

public class RequestQueue {
    private boolean isEnd;
    private final CopyOnWriteArrayList<Request> requests;
    private int maintainCount;

    public RequestQueue() {
        this.requests = new CopyOnWriteArrayList<>();
        this.isEnd = false;
        this.maintainCount = 0;
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
        if (this.requests.isEmpty() && this.maintainCount == 0) {
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

    public synchronized boolean isMaintainCount() {
        notifyAll();
        //System.out.println("==========================="+this.maintainCount);
        return this.maintainCount != 0;
    }

    public synchronized void setMaintainCount(boolean m) {
        if (m) {
            this.maintainCount++;
        } else {
            this.maintainCount--;
        }
        //System.out.println("----------------------------"+this.maintainCount);
        notifyAll();
    }
}
