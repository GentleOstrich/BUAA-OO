import com.oocourse.elevator1.PersonRequest;

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
        moveRequest();
    }

    private Elevator bring(int direction, int from) {
        Elevator aimElevator = null;
        int minDistance = Integer.MAX_VALUE;
        int distance;
        for (int i = 0; i < this.elevators.size(); ++i) {
            Elevator elevator = this.elevators.get((i + which) % this.elevators.size());
            if (elevator.getInside().getRequests().size() < 6) {
                distance = (elevator.getFloor() - from > 0) ?
                        (elevator.getFloor() - from) : -(elevator.getFloor() - from);
                if (elevator.getDirection() == direction) {
                    if (direction == 1) {
                        if (from > elevator.getFloor()) {
                            if (distance < minDistance) {
                                aimElevator = elevator;
                                minDistance = distance;
                            }
                        }
                    } else if (direction == -1) {
                        if (from < elevator.getFloor()) {
                            if (distance < minDistance) {
                                aimElevator = elevator;
                                minDistance = distance;
                            }
                        }
                    }
                }
            }
        }
        if (aimElevator != null) {
            this.which = (this.which + 1) % this.elevators.size();
        }
        return aimElevator;
    }

    private Elevator shortest(int from) {
        int distance;
        int minDistance = Integer.MAX_VALUE;
        Elevator aimElevator = null;
        for (int i = 0; i < 6; ++i) {
            Elevator elevator = this.elevators.get((i + this.which) % this.elevators.size());
            if (elevator.getInside().size() < 6) {
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
        if (aimElevator != null) {
            this.which = (this.which + 1) % this.elevators.size();
        }
        return aimElevator;
    }

    private void moveRequest() {
        while (true) {
            if (waitQueue.isEmpty() && waitQueue.isEnd()) {
                for (Elevator elevator : elevators) {
                    elevator.getOutside().setEnd(true);
                }
                break;
            }
            PersonRequest request = this.waitQueue.getRequest();
            if (request == null) {
                continue;
            }
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
                this.which = (this.which + 1) % this.elevators.size();
            }
            aimElevator.getOutside().addRequest(request);
        }

    }

    public void addElevator(Elevator elevator) {
        this.elevators.add(elevator);
    }
}
