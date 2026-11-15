import java.util.Random;

/**
 * Erzeuger: legt so lange Zufallswerte im Puffer ab, bis er per interrupt() beendet wird.
 */
public class Producer extends Thread {

    private static final int MAX_VALUE = 100;
    private static final int MIN_PAUSE_MS = 200;
    private static final int MAX_PAUSE_MS = 1000;

    private final Buffer buffer;
    private final Random random = new Random();

    public Producer(String name, Buffer buffer) {
        super(name);
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                buffer.put(random.nextInt(MAX_VALUE));
                Thread.sleep(random.nextInt(MIN_PAUSE_MS, MAX_PAUSE_MS));
            } catch (InterruptedException e) {
                break;   // unterbrochen in wait() oder sleep() -> geordnet beenden
            }
        }
        System.out.println(getName() + " beendet");
    }
}
