import java.util.Random;

/**
 * Verbraucher: entnimmt so lange Werte aus dem Puffer, bis er per interrupt() beendet wird.
 */
public class Consumer extends Thread {

    private static final int MIN_PAUSE_MS = 200;
    private static final int MAX_PAUSE_MS = 1000;

    private final Buffer buffer;
    private final Random random = new Random();

    public Consumer(String name, Buffer buffer) {
        super(name);
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                buffer.get();
                Thread.sleep(random.nextInt(MIN_PAUSE_MS, MAX_PAUSE_MS));
            } catch (InterruptedException e) {
                break;   // unterbrochen in wait() oder sleep() -> geordnet beenden
            }
        }
        System.out.println(getName() + " beendet");
    }
}
