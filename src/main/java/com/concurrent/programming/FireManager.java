package com.concurrent.programming;

import java.util.ArrayList;
import java.util.Arrays;

public class FireManager implements Runnable, Observable {

    private static Monitor monitor;
    private long sleepAfterCicle;
    private int[] shootingSequence;
    private int numberOfCicles;
    private String id;
    private ArrayList<Observer> observers;

    public FireManager(Transitions transition, Monitor monitor) {
        this.id = transition.id;
        this.shootingSequence = transition.sequence;
        this.numberOfCicles = transition.numberOfTimes;
        this.sleepAfterCicle = transition.sleepTime;
        this.monitor = monitor;
        observers = new ArrayList<>();
        addObserver(monitor.getLogger());
    }

    @Override
    public void run() {
        for (int n = 0; n < numberOfCicles || numberOfCicles == 0; n++) {
            for (int f = 0; f < shootingSequence.length; f++) {
                try {
                    this.monitor.adquireAndFire(shootingSequence[f]);
                    String msg = "Created Thread [" + Thread.currentThread().getId() + "]. ID: " + this.id + ". Fire sequence: " + Arrays.toString(shootingSequence);
                    notifyObserver(msg, 1);
                } catch (Exception e) {
                    notifyObserver(e.toString() + ". Causa:" + e.getLocalizedMessage(), 3);
                }
            }
            if (this.sleepAfterCicle != 0)
                try {
                    Thread.sleep(this.sleepAfterCicle);
                } catch (InterruptedException e) {
                    notifyObserver(e.getMessage(), 3);
                    System.exit(-1);
                }
        }
    }

    @Override
    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        this.observers.remove(o);
    }

    @Override
    public void notifyObserver(String msg, int type) {
        for (Observer o : observers) {
            o.update(msg, type);
        }
    }
}
