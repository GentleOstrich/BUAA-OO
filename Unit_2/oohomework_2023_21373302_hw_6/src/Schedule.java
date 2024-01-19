import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.MaintainRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.util.ArrayList;

public class Schedule implements Runnable {
    private final RequestQueue waitQueue;
    private int which;
    private final ArrayList<Elevator> elevators;

    public Schedule(RequestQueue waitQueue) {
        this.waitQueue = waitQueue;
        this.elevators = new ArrayList<>();
        this.which = 0;
    }

    @Override
    public void run() {
        operate();
    }

    private void operate() {
        while (true) {
            if (waitQueue.isEmpty() && (waitQueue.isEnd() && !waitQueue.isMaintainCount())) {
                boolean finish = true;
                for (Elevator elevator : elevators) {
                    if (elevator.isBeginMaintain() &&
                            (!elevator.getInside().isEmpty() || !elevator.getOutside().isEmpty())) {
                        finish = false;
                        break;
                    }
                }
                if (finish) {
                    //TimableOutput.println("SCHEDULE " + "is finished");
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
                if (((PersonRequest) request).getFromFloor() == 0) {
                    continue;
                }
                moveRequest((PersonRequest) request);
                //System.out.println("A PersonRequest:    " + request);
            } else if (request instanceof ElevatorRequest) {
                Elevator elevator = new Elevator(((ElevatorRequest) request).getElevatorId(),
                        ((ElevatorRequest) request).getFloor(),
                        ((ElevatorRequest) request).getCapacity(),
                        ((ElevatorRequest) request).getSpeed(), this.waitQueue);
                new Thread(elevator).start();
                this.elevators.add(elevator);
                //TimableOutput.println(this.elevators);
                // an ElevatorRequest
                // your code here
                //System.out.println("An ElevatorRequest: " + request);
            } else if (request instanceof MaintainRequest) {
                this.waitQueue.setMaintainCount(true);
                int eid = ((MaintainRequest) request).getElevatorId();
                for (Elevator elevator : this.elevators) {
                    if (eid == elevator.getId()) {
                        elevator.setBeginMaintain(true);
                        elevator.getOutside().setMaintainCount(true);
                        break;
                    }
                }
            }
        }
    }

    private Elevator bring(int direction, int from) {
        Elevator aimElevator = null;
        int minDistance = Integer.MAX_VALUE;
        int distance;
        for (int i = 0; i < this.elevators.size(); ++i) {
            Elevator elevator = this.elevators.get((i + which) % this.elevators.size());
            if (!elevator.isBeginMaintain() && !elevator.isFinish()) {
                if (elevator.getInside().getRequests().size() < elevator.getCapacity()) {
                    distance = (elevator.getFloor() - from > 0) ?
                            (elevator.getFloor() - from) : -(elevator.getFloor() - from);
                    if (elevator.getDirection() == direction) {
                        if (direction == 1 && from > elevator.getFloor()) {
                            if (distance < minDistance) {
                                aimElevator = elevator;
                                minDistance = distance;
                            }
                        } else if (direction == -1 && from < elevator.getFloor()) {
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

    private Elevator shortest(int from) {
        int distance;
        int minDistance = Integer.MAX_VALUE;
        Elevator aimElevator = null;
        for (int i = 0; i < this.elevators.size(); ++i) {
            Elevator elevator = this.elevators.get((i + this.which) % this.elevators.size());
            if (!elevator.isBeginMaintain() && !elevator.isFinish()) {
                if (elevator.getInside().size() < elevator.getCapacity()) {
                    distance = (elevator.getFloor() - from > 0) ?
                            (elevator.getFloor() - from) : -(elevator.getFloor() - from);
                    if (elevator.getOutside().isEmpty() && elevator.getInside().isEmpty()) {
                        if (distance < minDistance) {
                            aimElevator = elevator;
                            minDistance = distance;
                        }
                    }
                }
            }
        }
        return aimElevator;
    }

    private void moveRequest(PersonRequest request) {
        Elevator aimElevator;
        int from = request.getFromFloor();
        int to = request.getToFloor();
        int direction = (from < to) ? 1 : -1;
        aimElevator = bring(direction, from);
        if (aimElevator == null) {
            aimElevator = shortest(from);
        }
        if (aimElevator == null) {
            aimElevator = this.elevators.get(this.which);
            while (aimElevator.isBeginMaintain() || aimElevator.isFinish()) {
                this.which = (this.which + 1) % this.elevators.size();
                aimElevator = this.elevators.get(this.which);
            }
            this.which = (this.which + 1) % this.elevators.size();
        }
        //TimableOutput.println("======ELEVATOR " +
        // aimElevator.getId() + "======" + request + "======");
        aimElevator.getOutside().addRequest(request);
    }

    public void addElevator(Elevator elevator) {
        this.elevators.add(elevator);
    }
}
