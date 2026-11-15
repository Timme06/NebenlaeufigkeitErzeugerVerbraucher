import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testet das Zusammenspiel: Alle Threads müssen sich nach interrupt() geordnet beenden,
 * egal ob sie gerade in wait() oder sleep() hängen.
 */
class SystemTest {

    private static final long RUNTIME_MS = 1000;
    private static final long JOIN_TIMEOUT_MS = 2000;

    @Test
    void allThreadsTerminateAfterInterrupt() throws InterruptedException {
        Buffer buffer = new Buffer();
        Thread[] threads = {
                new Producer("Producer-1", buffer),
                new Producer("Producer-2", buffer),
                new Consumer("Consumer-1", buffer),
                new Consumer("Consumer-2", buffer),
                new Viewer("Viewer", buffer)
        };

        for (Thread thread : threads) {
            thread.start();
        }
        Thread.sleep(RUNTIME_MS);

        for (Thread thread : threads) {
            thread.interrupt();
        }
        for (Thread thread : threads) {
            thread.join(JOIN_TIMEOUT_MS);
            assertFalse(thread.isAlive(), thread.getName() + " hat sich nicht beendet");
        }
    }
}
