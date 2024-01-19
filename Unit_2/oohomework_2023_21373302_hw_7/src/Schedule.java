import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.MaintainRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;
import com.oocourse.elevator3.TimableOutput;

import java.util.ArrayList;

public class Schedule implements Runnable {
    private final RequestQueue waitQueue;
    private final Floors floors;
    private int which;
    private final ArrayList<Elevator> elevators;

    public Schedule(RequestQueue waitQueue, Floors floors) {
        this.waitQueue = waitQueue;
        this.floors = floors;
        this.elevators = new ArrayList<>();
        this.which = 0;
    }

    @Override
    public void run() {
        operate();
    }

    private void operate() {
        while (true) {
            //TimableOutput.println(waitQueue.getInputCount());
            if (waitQueue.isEmpty() && (waitQueue.isEnd() && !waitQueue.isInputing())) {
                //TimableOutput.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                boolean finish = true;
                for (Elevator elevator : elevators) {
                    if (elevator.isMaintaining() &&
                            (!elevator.getInside().isEmpty() || !elevator.getOutside().isEmpty())) {
                        finish = false;
                        break;
                    }
                }
                if (finish) {
                    //TimableOutput.println("======SCHEDULE " + "is finished======");
                    for (Elevator elevator : elevators) {
                        elevator.getOutside().setEnd(true);
                    }
                    break;
                }
            }
            //TimableOutput.println("====schedule getting====");
            Request request = this.waitQueue.getRequest();
            //if (request == null) {
            //    TimableOutput.println("====null====");
            //} else {
            //    TimableOutput.println("====" + request + "====");
            // }
            if (request == null) {
                continue;
            }
            if (request instanceof PersonRequest) {
                Person person;
                if (!(request instanceof Person)) {
                    person = new Person(((PersonRequest) request).getFromFloor(),
                            ((PersonRequest) request).getToFloor(),
                            ((PersonRequest) request).getPersonId(), false);
                } else {
                    person = new Person(((PersonRequest) request).getFromFloor(),
                            ((PersonRequest) request).getToFloor(),
                            ((PersonRequest) request).getPersonId(),
                            ((Person) request).getNeedTrans());
                }
                moveRequest(person);
                //System.out.println("=====" + requestPlus.getPersonId() + "=====");

            } else if (request instanceof ElevatorRequest) {
                Elevator elevator = new Elevator(((ElevatorRequest) request).getElevatorId(),
                        ((ElevatorRequest) request).getFloor(),
                        ((ElevatorRequest) request).getCapacity(),
                        ((ElevatorRequest) request).getSpeed(),
                        ((ElevatorRequest) request).getAccess(), this.waitQueue, this.floors);
                new Thread(elevator).start();
                this.elevators.add(elevator);
            } else if (request instanceof MaintainRequest) {
                this.waitQueue.changeInputCount(true);
                int eid = ((MaintainRequest) request).getElevatorId();
                for (Elevator elevator : this.elevators) {
                    if (eid == elevator.getId()) {
                        elevator.setMaintaining(true);
                        elevator.getOutside().changeInputCount(true);
                        break;
                    }
                }
            }
        }
    }

    private Elevator bring(int direction, int from, int to) {
        Elevator aimElevator = null;
        int minDistance = Integer.MAX_VALUE;
        int distance;
        for (int i = 0; i < this.elevators.size(); ++i) {
            Elevator elevator = this.elevators.get((i + which) % this.elevators.size());
            if (!elevator.isMaintaining() && !elevator.isFinish()) {
                if (elevator.getInside().getRequests().size() < elevator.getCapacity()) {
                    distance = (elevator.getFloor() - from > 0) ?
                            (elevator.getFloor() - from) : -(elevator.getFloor() - from);
                    if (elevator.getDirection() == direction) {
                        if (direction == 1 && from > elevator.getFloor() &&
                                elevator.isAccess(from) && elevator.able(from, direction, to)) {
                            if (distance < minDistance) {
                                aimElevator = elevator;
                                minDistance = distance;
                            }
                        } else if (direction == -1 && from < elevator.getFloor() &&
                                elevator.isAccess(from) && elevator.able(from, direction, to)) {
                            if (distance < minDistance) {
                                aimElevator = elevator;
                                minDistance = distance;
                            }
                        }
                    }
                }
            }
        }
        return aimElevator;
    }

    private Elevator shortest(int from, int direction, int to) {
        int distance;
        int minDistance = Integer.MAX_VALUE;
        Elevator aimElevator = null;
        for (int i = 0; i < this.elevators.size(); ++i) {
            Elevator elevator = this.elevators.get((i + this.which) % this.elevators.size());
            if (!elevator.isMaintaining() && !elevator.isFinish()) {
                if (elevator.getInside().size() < elevator.getCapacity()) {
                    distance = (elevator.getFloor() - from > 0) ?
                            (elevator.getFloor() - from) : -(elevator.getFloor() - from);
                    if (elevator.getOutside().isEmpty() && elevator.getInside().isEmpty()) {
                        if (distance < minDistance && elevator.isAccess(from)
                                && elevator.able(from, direction, to)) {
                            aimElevator = elevator;
                            minDistance = distance;
                        }
                    }
                }
            }
        }
        return aimElevator;
    }

    private void moveRequest(Person request) {
        Elevator aimElevator;
        int from = request.getFromFloor();
        int to = request.getToFloor();
        int direction = (from < to) ? 1 : -1;
        aimElevator = bring(direction, from, to);
        if (aimElevator == null) {
            aimElevator = shortest(from, direction, to);
        }
        if (aimElevator == null) {
            aimElevator = this.elevators.get(this.which);
            while (aimElevator.isMaintaining() ||
                    aimElevator.isFinish() || !aimElevator.isAccess(from) ||
                    !aimElevator.able(from, direction, to)) {
                this.which = (this.which + 1) % this.elevators.size();
                aimElevator = this.elevators.get(this.which);
            }
            this.which = (this.which + 1) % this.elevators.size();
        }
        if (!aimElevator.isAccess(request.getToFloor())) {
            if (!request.getNeedTrans()) {
                this.waitQueue.changeInputCount(true);
            }
            request.setNeedTrans(true);
        }
        if (request.getNeedTrans() && aimElevator.isAccess(request.getToFloor()) &&
                aimElevator.able(from, direction, to)) {
            this.waitQueue.changeInputCount(false);
            request.setNeedTrans(false);
        }
        TimableOutput.println("======ELEVATOR " + aimElevator.getId() + "======" + request + "======");
        aimElevator.getOutside().addRequest(request);
    }

    public void addElevator(Elevator elevator) {
        this.elevators.add(elevator);
    }
}
