import com.oocourse.elevator1.PersonRequest;
import com.oocourse.elevator1.TimableOutput;

public class Elevator implements Runnable {
    private State state;
    private final int id;
    private final RequestQueue outside;
    private final RequestQueue inside;
    private boolean finish;
    private int floor;
    private int direction;

    public Elevator(int id) {
        this.state = State.FIND;
        this.id = id;
        this.outside = new RequestQueue();
        this.inside = new RequestQueue();
        this.finish = false;
        this.floor = 1;
        this.direction = 0;
    }

    @Override
    public void run() {
        operate();
    }

    private void operate() {
        while (true) {
            if (this.state == State.FIND) {
                this.finish = false;
                find();
                if (finish) {
                    break;
                }
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
        }
    }

    private void find() {
        if (this.outside.isEnd() && this.outside.isEmpty() && this.inside.isEmpty()) {
            finish = true;
        } else {
            if (this.inside.isEmpty()) {
                PersonRequest request = this.outside.lookRequest();
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
            Thread.sleep(400);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.floor += this.direction;
        this.state = State.ARRIVE;
    }

    private void arrive() {
        TimableOutput.println("ARRIVE-" + this.floor + "-" + this.id);
        for (PersonRequest request : this.outside.getRequests()) {
            if (request.getFromFloor() == this.floor) {
                this.state = State.OPEN;
                return;
            }
        }
        for (PersonRequest request : this.inside.getRequests()) {
            if (request.getToFloor() == this.floor) {
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
        for (PersonRequest request : this.inside.getRequests()) {
            if (request.getToFloor() == this.floor) {
                TimableOutput.println("OUT-" +
                        request.getPersonId() + "-" + this.floor + "-" + this.id);
                this.inside.getRequests().remove(request);
            }
        }
        for (PersonRequest request : this.outside.getRequests()) {

            if (this.inside.size() < 6) {
                if (request.getFromFloor() == this.floor) {
                    TimableOutput.println("IN-" +
                            request.getPersonId() + "-" + this.floor + "-" + this.id);
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
}
