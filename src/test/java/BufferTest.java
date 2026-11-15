import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für den Buffer.
 * Die genaue Reihenfolge der Threads ist nicht vorhersagbar. Getestet wird deshalb nur,
 * was immer gelten muss: Zustände, Blockieren und Beenden per interrupt().
 */
class BufferTest {

    /** Wie lange wir einem Thread Zeit geben, bevor wir sein Verhalten prüfen. */
    private static final long WAIT_MS = 300;

    @Test
    void newBufferIsEmpty() {
        Buffer buffer = new Buffer();

        assertNull(buffer.peek());
    }

    @Test
    void getReturnsPutValueAndEmptiesBuffer() throws InterruptedException {
        Buffer buffer = new Buffer();

        buffer.put(42);
        assertEquals(42, buffer.peek());   // peek liest nur und verändert nichts
        assertEquals(42, buffer.get());
        assertNull(buffer.peek());         // nach get() ist der Puffer wieder leer
    }

    @Test
    void putBlocksWhileBufferIsFull() throws InterruptedException {
        Buffer buffer = new Buffer();
        buffer.put(1);

        Thread producer = new Thread(() -> {
            try {
                buffer.put(2);
            } catch (InterruptedException ignored) {
            }
        });
        producer.start();
        producer.join(WAIT_MS);
        assertTrue(producer.isAlive(), "put() muss warten, solange der Puffer voll ist");

        assertEquals(1, buffer.get());     // Platz schaffen ...
        producer.join(WAIT_MS);
        assertFalse(producer.isAlive(), "... dann muss put() weiterlaufen");
        assertEquals(2, buffer.peek());
    }

    @Test
    void waitingGetCanBeEndedByInterrupt() throws InterruptedException {
        Buffer buffer = new Buffer();
        boolean[] interrupted = {false};

        Thread consumer = new Thread(() -> {
            try {
                buffer.get();              // Puffer ist leer -> wartet
            } catch (InterruptedException e) {
                interrupted[0] = true;
            }
        });
        consumer.start();
        consumer.join(WAIT_MS);
        assertTrue(consumer.isAlive(), "get() muss warten, solange der Puffer leer ist");

        consumer.interrupt();
        consumer.join(WAIT_MS);
        assertFalse(consumer.isAlive(), "interrupt() muss den wartenden Thread beenden");
        assertTrue(interrupted[0], "get() muss die InterruptedException weiterreichen");
    }
}
