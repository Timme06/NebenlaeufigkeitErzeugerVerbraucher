/**
 * Puffer der Größe 1, über den Erzeuger und Verbraucher Werte austauschen.
 * put() und get() sind synchronized und koordinieren sich über wait()/notifyAll();
 * peek() erlaubt dem Viewer einen nur-lesenden Blick ohne Blockieren.
 */
public class Buffer {

    // Der gesamte Zustand steckt in einer einzigen volatile-Variable:
    // null = leer, jeder andere Wert = voll (mit diesem Wert).
    // So kann der Viewer ohne synchronized immer einen stimmigen Zustand lesen.
    private volatile Integer data = null;

    public synchronized void put(int value) throws InterruptedException {
        if (data != null) {
            log("wartet mit " + value + " (Puffer voll)");
        }
        while (data != null) {   // while statt if: Bedingung nach jedem Aufwachen neu prüfen
            wait();
        }
        data = value;
        log("legt " + value + " ab");
        notifyAll();             // notifyAll statt notify: sonst kann ein Erzeuger einen Erzeuger wecken
    }

    public synchronized int get() throws InterruptedException {
        if (data == null) {
            log("wartet (Puffer leer)");
        }
        while (data == null) {
            wait();
        }
        int value = data;
        data = null;
        log("entnimmt " + value);
        notifyAll();
        return value;
    }

    /**
     * Nur-lesender Zugriff: liest genau eine volatile-Variable,
     * belegt den Monitor nicht und blockiert daher niemanden.
     *
     * @return aktueller Wert oder null, wenn der Puffer leer ist
     */
    public Integer peek() {
        return data;
    }

    // Die Ausgabe passiert bewusst INNERHALB des Monitors: Nur so steht sie in derselben
    // Reihenfolge auf der Konsole, in der die Aktionen tatsächlich passiert sind.
    private void log(String action) {
        System.out.println(Thread.currentThread().getName() + " " + action);
    }
}
