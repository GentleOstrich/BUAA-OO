import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;
import com.oocourse.elevator3.TimableOutput;

public class Elevator implements Runnable {
    private final RequestQueue outside;
    private final RequestQueue inside;
    private final RequestQueue waiting;
    private final Floors floors;
    private State state;
    private final int id;
    private int floor;
    private boolean flag;
    private final int capacity;
    private final double speed;
    private final int access;
    private boolean finish;
    private int direction;
    private volatile boolean maintaining;

    public Elevator(int id, int floor, int capacity, double speed,
                    int access, RequestQueue waiting, Floors floors) {
        this.outside = new RequestQueue();
        this.inside = new RequestQueue();
        this.waiting = waiting;
        this.floors = floors;
        this.state = State.FIND;
        this.id = id;
        this.floor = floor;
        this.flag = false;
        this.capacity = capacity;
        this.speed = speed;
        this.access = access;
        this.finish = false;
        this.direction = 0;
        this.maintaining = false;
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
            if (this.maintaining) {
                TimableOutput.println("MAINTAIN_ABLE-" + this.id);
                this.waiting.changeInputCount(false);
                this.maintaining = false;
            }
        } else {
            if (this.inside.isEmpty()) {
                PersonRequest request = this.outside.lookRequest();
                if (this.maintaining) {
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
        if (this.maintaining) {
            maintain();
            return;
        }
        if (isAccess(this.floor)) {
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
            for (Request request : this.inside.getRequests()) {
                if (this.direction == 1) {
                    if (((PersonRequest) request).getToFloor() <
                            nextAccess(this.floor + 1, this.direction) &&
                            !isAccess(((PersonRequest) request).getToFloor())) {
                        this.state = State.OPEN;
                        return;
                    }
                } else if (this.direction == -1) {
                    if (((PersonRequest) request).getToFloor() >
                            nextAccess(this.floor - 1, this.direction) &&
                            !isAccess(((PersonRequest) request).getToFloor())) {
                        this.state = State.OPEN;
                        return;
                    }
                }
            }
        }
        this.state = State.MOVE;
    }

    private void open() {
        this.flag = false;
        for (Request request : this.outside.getRequests()) {
            if (((PersonRequest) request).getFromFloor() == this.floor) {
                this.state = State.OPEN;
                this.flag = true;
                break;
            }
        }
        for (Request request : this.inside.getRequests()) {
            if (((PersonRequest) request).getToFloor() == this.floor) {
                this.flag = false;
                break;
            }
            if (this.direction == 1) {
                if (((PersonRequest) request).getToFloor() <
                        nextAccess(this.floor + 1, this.direction) &&
                        !isAccess(((PersonRequest) request).getToFloor())) {
                    this.flag = false;
                    break;
                }
            } else if (this.direction == -1) {
                if (((PersonRequest) request).getToFloor() >
                        nextAccess(this.floor - 1, this.direction) &&
                        !isAccess(((PersonRequest) request).getToFloor())) {
                    this.flag = false;
                    break;
                }
            }
        }
        //System.out.println(flag);
        this.floors.startMOpen(this.floor, this.flag);

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
            } else {
                if (this.direction == 1) {
                    if (((PersonRequest) request).getToFloor() <
                            nextAccess(this.floor + 1, this.direction) &&
                            !isAccess(((PersonRequest) request).getToFloor())) {
                        TimableOutput.println("OUT-" + ((PersonRequest) request).getPersonId()
                                + "-" + this.floor + "-" + this.id);
                        this.inside.getRequests().remove(request);
                        if (this.floor != ((Person) request).getToFloor()) {
                            Person request1 = new Person(this.floor,
                                    ((Person) request).getToFloor(),
                                    ((Person) request).getPersonId(),
                                    ((Person) request).getNeedTrans());
                            this.waiting.addRequest(request1);
                            //TimableOutput.println("======ELEVATOR " + this.id+ "======KICK "
                            // + request1.getPersonId() + " OFF======AT " + this.floor);
                        }
                    }
                } else if (this.direction == -1) {
                    if (((PersonRequest) request).getToFloor() >
                            nextAccess(this.floor - 1, this.direction) &&
                            !isAccess(((PersonRequest) request).getToFloor())) {
                        TimableOutput.println("OUT-" + ((PersonRequest) request).getPersonId() +
                                "-" + this.floor + "-" + this.id);
                        this.inside.getRequests().remove(request);
                        if (this.floor != ((Person) request).getToFloor()) {
                            Person request1 = new Person(this.floor,
                                    ((Person) request).getToFloor(),
                                    ((Person) request).getPersonId(),
                                    ((Person) request).getNeedTrans());
                            this.waiting.addRequest(request1);
                            //TimableOutput.println("======ELEVATOR " + this.id + "======KICK "
                            // + request1.getPersonId() + " OFF======AT" + this.floor);
                        }
                    }
                }
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
        //System.out.println(flag);
        this.floors.removeMElevator(this.floor, this.flag);
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
                Person request1 = new Person(this.floor,
                        ((Person) request).getToFloor(),
                        ((Person) request).getPersonId(), ((Person) request).getNeedTrans());
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
        this.waiting.changeInputCount(false);
        this.maintaining = false;
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

    public boolean isMaintaining() {
        return maintaining;
    }

    public void setMaintaining(boolean maintaining) {
        this.maintaining = maintaining;
    }

    public boolean isFinish() {
        return finish;
    }

    public String toString() {
        return "Elevator " + this.id;
    }

    public int getAccess() {
        return access;
    }

    public boolean isAccess(int floor) {
        return (this.access & (1 << (floor - 1))) != 0;
    }

    public int nextAccess(int floor, int direction) {
        int i = floor;
        if (direction == 1) {
            while (i <= 11) {
                if (isAccess(i)) {
                    break;
                }
                i++;
            }
        } else if (direction == -1) {
            while (i >= 0) {
                if (isAccess(i)) {
                    break;
                }
                i--;
            }
        }
        return i;
    }

    public boolean able(int from, int direction, int to) {
        if (direction == 1) {
            if (nextAccess(from + 1, direction) <= to) {
                return true;
            }
        } else if (direction == -1) {
            if (nextAccess(from - 1, direction) >= to) {
                return true;
            }
        }
        return false;
    }
}
