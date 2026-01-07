import java.util.LinkedList;
import java.util.Queue;

class ParkingSystem {
    private static final Queue<String> queue = new LinkedList<>();
    private static final int capacity = 5;

    public static final Runnable Producer = new Runnable() {
        public void run() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() < capacity) {
                        String car = "Car" + (queue.size() + 1);
                        queue.add(car);
                        System.out.println("Added " + car + " to Parking...");
                        try {
                            Thread.sleep(500); 
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Parking Area Filled...");
                    queue.notifyAll();
                    try {
                        queue.wait(); 
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    public static final Runnable Consumer = new Runnable() {
        public void run() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() < capacity) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    while (!queue.isEmpty()) {
                        String car = queue.remove();
                        System.out.println("Removed " + car + " from Parking...");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Parking Area Empty...");
                    queue.notifyAll();
                }
            }
        }
    };
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Main Thread Executes...");
        Thread pt = new Thread(ParkingSystem.Producer, "Producer");
        Thread ct = new Thread(ParkingSystem.Consumer, "Consumer");
        pt.start();
        ct.start();
        System.out.println("Main Thread Ends...");
    }
}
