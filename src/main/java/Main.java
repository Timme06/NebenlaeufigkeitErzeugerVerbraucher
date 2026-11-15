/**
 * Startet das Erzeuger-Verbraucher-System, lässt es eine feste Zeit laufen
 * und beendet anschließend alle Threads geordnet über interrupt() und join().
 */

private static final int RUNTIME_MS = 5000;

void main() throws InterruptedException {
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
    System.out.println("--- Laufzeit vorbei, beende Threads ---");

    for (Thread thread : threads) {
        thread.interrupt();
    }
    for (Thread thread : threads) {
        thread.join();
    }
    System.out.println("--- Alle Threads beendet ---");
}
