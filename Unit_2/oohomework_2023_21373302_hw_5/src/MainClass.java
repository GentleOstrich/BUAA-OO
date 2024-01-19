import com.oocourse.elevator1.TimableOutput;

public class MainClass {
    public static void main(String[] args) {

        TimableOutput.initStartTimestamp();

        RequestQueue waitingQueue = new RequestQueue();

        InputThread inputThread = new InputThread(waitingQueue);

        Schedule schedule = new Schedule(waitingQueue);

        for (int i = 1; i <= 6; i++) {
            Elevator elevator = new Elevator(i);
            schedule.addElevator(elevator);
            new Thread(elevator).start();
        }

        new Thread(schedule).start();

        new Thread(inputThread).start();

    }
}
