package com.concurrent.programming;


import com.concurrent.programming.loaders.LoadQueues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

@SuppressWarnings("all")
public class QueueMonitorManagment implements Observable {

    private List<List<ThreadNode>> queues;
    private boolean[] autoWakeUp;
    private long[] timeToWakeup;
    private Semaphore lock;
    private ArrayList<Observer> observers;

    /**
     * This class managment all queues of the Monitor class
     *
     * @param queue
     * @throws IllegalArgumentException
     */

    public QueueMonitorManagment(LoadQueues queue) throws IllegalArgumentException {

        if (queue.temporalQueue.length < 0) {
            throw new IllegalArgumentException("An empty queue can not be created");
        }
        this.observers = new ArrayList<>();
        this.queues = new ArrayList<>();
        this.autoWakeUp = new boolean[queue.temporalQueue.length];
        this.timeToWakeup = new long[queue.temporalQueue.length];
        this.lock = new Semaphore(1, true);
        for (int i = 0; i < queue.temporalQueue.length; i++) {
            this.queues.add(new ArrayList<ThreadNode>());
            this.autoWakeUp[i] = queue.temporalQueue[i] == 1;
        }

        this.observers.add(Monitor.getLogger());
    }

    public boolean[] whoIsWaiting() {
        boolean[] who = new boolean[this.queues.size()];

        for (int i = 0; i < who.length; i++) {
            who[i] = !this.queues.get(i).isEmpty();
        }
        return who;
    }

    /**
     * Add the thread who called
     *
     * @param numberOfQueue Number of queue to add
     * @param time          Time that must sleep the thread, if it's alone on the queue
     *                      0 for not take into account the time. It will take the time
     *                      for the queues who can wake up alone the threads
     * @return <code>True</code> if the Thread wake up alone
     */
    public boolean toSleep(int numberOfQueue, long time) {
        if (numberOfQueue < 1 || numberOfQueue > this.queues.size()) {
            throw new IndexOutOfBoundsException("The queue do not exist");
        }

        numberOfQueue--;

        ThreadNode tn = new ThreadNode();
        try {
            //Add to end of queue
            this.queues.get(numberOfQueue).add(tn);
            //If it has to wait a time and it's alone
            if (this.autoWakeUp[numberOfQueue]) {
                //If the queue if empty it's a thread that should sleep for a while
                boolean whoHaveToSleep = this.queues.get(numberOfQueue).size() == 1;
                if (whoHaveToSleep) {
                    this.timeToWakeup[numberOfQueue] = System.currentTimeMillis() + time;
                    String msg = "Thread [" + Thread.currentThread().getId() + "].Add to queue in: " + (numberOfQueue + 1) + ".To sleep: " + time;
                    notifyObserver(msg, 2);
                }
                do {
                    tn.waitNode(time, whoHaveToSleep);
                    this.lock.acquire();
                    whoHaveToSleep = this.timeToWakeup[numberOfQueue] != 0;
                    time = this.timeToWakeup[numberOfQueue] - System.currentTimeMillis();
                    this.lock.release();
                    String msg = "Thread [" + Thread.currentThread().getId() + "].In the queue: " + (numberOfQueue + 1) + ".WakeUp at: " + System.currentTimeMillis();
                    notifyObserver(msg, 2);
                }
                while (!whoHaveToSleep || (time > 0));
            }
            this.queues.get(numberOfQueue).remove(tn);
        } catch (java.lang.InterruptedException e) {
            /* Exception occurr -> Get out of the Monitor */
            this.queues.get(numberOfQueue).remove(tn);
        }
        return this.autoWakeUp[numberOfQueue];
    }

    public void wakeUp(int numberOfQueue) throws InterruptedException {
        if (numberOfQueue < 1 || numberOfQueue > this.queues.size()) {
            throw new IndexOutOfBoundsException("The queue do not exist");
        }

        numberOfQueue--;

        ThreadNode tn = this.queues.get(numberOfQueue).get(0);
        this.queues.get(numberOfQueue).remove(tn);
        tn.notifyNode();

    }

    public void setNewTimesToSleep(long[] newTimes) {
        long cronometer = System.currentTimeMillis();
        try {
            this.lock.acquire();
            for (int i = 0; i < newTimes.length; i++) {
                if (newTimes[i] != 0 && this.autoWakeUp[i]) {
                    if (!this.queues.get(i).isEmpty()) {
                        this.queues.get(i).get(0).notifyNode();
                        timeToWakeup[i] = cronometer + newTimes[i];
                    }
                } else {
                    timeToWakeup[i] = 0;
                }
                String msg = "New time set: " + Arrays.toString(timeToWakeup);
                notifyObserver(msg, 1);
            }
            this.lock.release();
        } catch (Exception e) {
            Log.logSevere("Error in setNewTime" + e.toString());
            System.exit(-1);
        }
    }


    public int[] relativeTimeOnQueue() {
        int[] sizes = new int[this.queues.size()];
        long actualTime = System.currentTimeMillis();
        List<ThreadNode> cola;
        for (int i = 0; i < this.queues.size(); i++) {
            cola = this.queues.get(i);
            sizes[i] = cola.isEmpty() ? 0 : (int) (actualTime - cola.get(0).getTimeDay0());
        }
        return sizes;

    }

    public int[] sizeOfQueues() {
        int[] sizes = new int[this.queues.size()];
        for (int i = 0; i < this.queues.size(); i++) {
            sizes[i] = this.queues.get(i).size();
        }
        return sizes;

    }

    public int size() {
        return this.queues.size();
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObserver(String msg, int type) {
        for (Observer o : observers) {
            o.update(msg, type);
        }
    }
}