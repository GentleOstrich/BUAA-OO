import com.oocourse.elevator2.TimableOutput;

public class MainClass {
    public static void main(String[] args) {

        TimableOutput.initStartTimestamp();

        RequestQueue waitingQueue = new RequestQueue();

        Schedule schedule = new Schedule(waitingQueue);

        InputThread inputThread = new InputThread(waitingQueue);

        for (int i = 1; i <= 6; i++) {
            Elevator elevator = new Elevator(i, 1, 6, 0.4, waitingQueue);
            schedule.addElevator(elevator);
            new Thread(elevator).start();
        }

        new Thread(schedule).start();

        new Thread(inputThread).start();

    }
}
