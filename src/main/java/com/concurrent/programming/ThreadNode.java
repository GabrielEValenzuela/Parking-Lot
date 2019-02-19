package com.concurrent.programming;

final class ThreadNode {

    final long day0;
    final private Thread t;
    final private Object lockObj;

    /**
     * Crea nodo de informacion del thread
     */
    ThreadNode() {
        super();
        this.t = Thread.currentThread();
        this.day0 = System.currentTimeMillis();
        this.lockObj = new Object();
    }

    /**
     * Retorna el thread que creo el nodo
     *
     * @return Thread creador del nodo
     */
    Thread getT() {
        return t;
    }

    /**
     * Pone al thread en wait state con un objeto propio del nodo
     *
     * @throws InterruptedException Producida por el wait al thread
     */
    synchronized void waitNode(long timeToSleep, boolean queueWithTime) throws InterruptedException {
        synchronized (this.lockObj) {
            if (queueWithTime && timeToSleep > 0) {
                lockObj.wait(timeToSleep);
            } else {
                lockObj.wait();
            }
        }
    }

    /**
     * Levanta al thread de wait state a ready con un objeto propio del nodo
     */
    synchronized void notifyNode() {
        synchronized (this.lockObj) {
            lockObj.notify();
        }
    }

    synchronized void waitNode() throws InterruptedException {
        synchronized (this.lockObj) {
            lockObj.wait();

        }
    }

    /**
     * Obtiene el tiempo de creacion del nodo
     *
     * @return TimeStamp de la creacion del nodo en ms formato unix
     */
    long getTimeDay0() {
        return day0;
    }

}