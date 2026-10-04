package ar.edu.unc.fcefyn.pcp.tp1.solution;

import java.util.ArrayDeque;
import java.util.Queue;

public class WorkQueue<T> {

    private final Queue<T> queue = new ArrayDeque<>();
    private final int capacity;
    private boolean closed = false;

    public WorkQueue() {
        this(Integer.MAX_VALUE);
    }

    public WorkQueue(int capacity) {
        this.capacity = capacity;
    }

    /** Inserta un elemento en la cola. Si la cola está llena, bloquea hasta que haya espacio. */
    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() >= capacity && !closed) {
            wait();
        }
        if (closed) {
            throw new IllegalStateException("No se pueden agregar elementos. Cola cerrada");
        }
        queue.add(item);
        notifyAll(); // Avisa a los hilos en espera
    }

    /**
     * Extrae un elemento. Si está vacía, bloquea hasta que haya uno o hasta que la cola se
     * cierre.
     * @return El elemento extraído o null si la cola fue cerrada y no quedan elementos.
     */
    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty() && !closed) {
            wait();
        }
        if (queue.isEmpty() && closed) {
            return null; // Indicador para que el hilo termine suavemente
        }
        T item = queue.poll();
        notifyAll(); // Avisa a los hilos en espera si había límite de capacidad
        return item;
    }

    /**
     * Cierra la cola e interrumpe la espera de los hilos bloqueados para permitir
     * la terminación.
     */
    public synchronized void close() {
        this.closed = true;
        notifyAll(); // Despierta a todos los hilos dormidos en wait()
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized int size() {
        return queue.size();
    }
}
