import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;
import com.oocourse.elevator2.TimableOutput;

public class Elevator implements Runnable {
    private State state;
    private final int id;
    private int floor;
    private final int capacity;
    private final double speed;
    private final RequestQueue outside;
    private final RequestQueue inside;
    private final RequestQueue waiting;
    private boolean finish;
    private int direction;
    private volatile boolean beginMaintain;

    public Elevator(int id, int floor, int capacity, double speed, RequestQueue waiting) {
        this.state = State.FIND;
        this.id = id;
        this.floor = floor;
        this.capacity = capacity;
        this.speed = speed;
        this.outside = new RequestQueue();
        this.inside = new RequestQueue();
        this.waiting = waiting;
        this.finish = false;
        this.direction = 0;
        this.beginMaintain = false;
    }

    @Override
    public void run() {
        operate();
    }

    private void operate() {
        do {
            this.finish = false;
            if (this.state == State.FIND) {
                find();
            } else if (this.state == State.MOVE) {
                move();
            } else if (this.state == State.OPEN) {
                open();
            } else if (this.state == State.INOUT) {
                inout();
            } else if (this.state == State.CLOSE) {
                close();
            } else if (this.state == State.ARRIVE) {
                arrive();
            }
        } while (!this.finish);
        //TimableOutput.println("ELEVATOR " + this.id + " is finished");
    }

    private void find() {
        if (this.outside.isEnd() && this.outside.isEmpty() && this.inside.isEmpty()) {
            this.finish = true;
            if (this.beginMaintain) {
                TimableOutput.println("MAINTAIN_ABLE-" + this.id);
                this.waiting.setMaintainCount(false);
                this.beginMaintain = false;
            }
        } else {
            if (this.inside.isEmpty()) {
                PersonRequest request = this.outside.lookRequest();
                if (this.beginMaintain) {
                    maintain();
                    return;
                }
                if (request == null) {
                    return;
                }
                if (request.getFromFloor() > this.floor) {
                    this.direction = 1;
                    this.state = State.MOVE;
                } else if (request.getFromFloor() < this.floor) {
                    this.direction = -1;
                    this.state = State.MOVE;
                } else {
                    this.direction = 0;
                    this.state = State.OPEN;
                }
            } else { // 电梯中有人
                PersonRequest request = this.inside.lookRequest();
                if (request.getToFloor() > this.floor) {
                    this.direction = 1;
                    this.state = State.MOVE;
                } else if (request.getToFloor() < this.floor) {
                    this.direction = -1;
                    this.state = State.MOVE;
                } else {
                    this.direction = 0;
                    this.state = State.OPEN;
                }
            }
        }
    }

    private void move() {
        try {
            Thread.sleep((long) (speed * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.floor += this.direction;
        this.state = State.ARRIVE;
    }

    private void arrive() {
        TimableOutput.println("ARRIVE-" + this.floor + "-" + this.id);
        if (this.beginMaintain) {
            maintain();
            return;
        }
        for (Request request : this.outside.getRequests()) {
            if (((PersonRequest) request).getFromFloor() == this.floor) {
                this.state = State.OPEN;
                return;
            }
        }
        for (Request request : this.inside.getRequests()) {
            if (((PersonRequest) request).getToFloor() == this.floor) {
                this.state = State.OPEN;
                return;
            }
        }
        this.state = State.MOVE;
    }

    private void open() {
        TimableOutput.println("OPEN-" + this.floor + "-" + this.id);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.state = State.INOUT;
    }

    private void inout() {
        for (Request request : this.inside.getRequests()) {
            if (((PersonRequest) request).getToFloor() == this.floor) {
                TimableOutput.println("OUT-" +
                        ((PersonRequest) request).getPersonId() + "-" + this.floor + "-" + this.id);
                this.inside.getRequests().remove(request);
            }
        }
        for (Request request : this.outside.getRequests()) {
            if (this.inside.size() < this.capacity) {
                if (((PersonRequest) request).getFromFloor() == this.floor) {
                    TimableOutput.println("IN-" +
                            ((PersonRequest) request).getPersonId() +
                            "-" + this.floor + "-" + this.id);
                    this.outside.getRequests().remove(request);
                    this.inside.addRequest(request);
                }
            } else {
                break;
            }
        }
        this.state = State.CLOSE;
    }

    private void close() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        TimableOutput.println("CLOSE-" + this.floor + "-" + this.id);
        this.state = State.FIND;
    }

    private void maintain() {
        boolean flag = !this.inside.isEmpty();
        if (flag) {
            TimableOutput.println("OPEN-" + this.floor + "-" + this.id);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (Request request : this.inside.getRequests()) {
            TimableOutput.println("OUT-" +
                    ((PersonRequest) request).getPersonId() + "-" + this.floor + "-" + this.id);
            this.inside.getRequests().remove(request);
            if (this.floor != ((PersonRequest) request).getToFloor()) {
                PersonRequest request1 = new PersonRequest(this.floor,
                        ((PersonRequest) request).getToFloor(),
                        ((PersonRequest) request).getPersonId());
                this.waiting.addRequest(request1);
            }
        }
        for (Request request : this.outside.getRequests()) {
            this.outside.getRequests().remove(request);
            this.waiting.addRequest(request);
        }
        if (flag) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            TimableOutput.println("CLOSE-" + this.floor + "-" + this.id);
        }
        TimableOutput.println("MAINTAIN_ABLE-" + this.id);
        this.waiting.setMaintainCount(false);
        this.beginMaintain = false;
        this.finish = true;
    }

    public int getFloor() {
        return this.floor;
    }

    public RequestQueue getOutside() {
        return outside;
    }

    public RequestQueue getInside() {
        return inside;
    }

    public int getDirection() {
        return direction;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean isBeginMaintain() {
        return beginMaintain;
    }

    public void setBeginMaintain(boolean beginMaintain) {
        this.beginMaintain = beginMaintain;
    }

    public boolean isFinish() {
        return finish;
    }

    public String toString() {
        return "Elevator " + this.id;
    }
}
