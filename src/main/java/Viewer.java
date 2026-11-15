/**
 * Betrachter: schaut periodisch in den Puffer, ohne ihn zu verändern oder zu blockieren.
 */
public class Viewer extends Thread {

    private static final int INTERVAL_MS = 300;

    private final Buffer buffer;

    public Viewer(String name, Buffer buffer) {
        super(name);
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            Integer value = buffer.peek();   // Momentaufnahme, ohne den Monitor zu belegen
            System.out.println(getName() + " sieht " + (value == null ? "leer" : value));
            try {
                Thread.sleep(INTERVAL_MS);
            } catch (InterruptedException e) {
                break;   // unterbrochen während sleep() -> geordnet beenden
            }
        }
        System.out.println(getName() + " beendet");
    }
}
