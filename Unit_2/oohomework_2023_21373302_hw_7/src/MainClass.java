import com.oocourse.elevator3.TimableOutput;

public class MainClass {
    public static void main(String[] args) {

        TimableOutput.initStartTimestamp();

        RequestQueue waitingQueue = new RequestQueue();

        Floors floors = new Floors();

        Schedule schedule = new Schedule(waitingQueue, floors);

        InputThread inputThread = new InputThread(waitingQueue);



        for (int i = 1; i <= 6; i++) {
            Elevator elevator = new Elevator(i, 1, 6, 0.4, 2047, waitingQueue, floors);
            schedule.addElevator(elevator);
            new Thread(elevator).start();
        }

        new Thread(schedule).start();

        new Thread(inputThread).start();

    }
}
