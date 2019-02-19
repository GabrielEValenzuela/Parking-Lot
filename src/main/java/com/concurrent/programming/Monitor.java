package com.concurrent.programming;

import com.concurrent.programming.customexceptions.*;
import com.concurrent.programming.loaders.LoadPetriNetwork;
import com.concurrent.programming.loaders.LoadQueues;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;


public class Monitor implements Observable {

    private static Log logger = new Log();
    private PetriNetwork petrinet;
    private Semaphore semaphore;
    private QueueMonitorManagment queues;
    private Status state;
    private Policy policy;
    private ArrayList<Observer> observers;

    /**
     * According to Pattern-Oriented Software Architecture, Patterns for Concurrent and Networked Objects, Volume 2
     * by Douglas Schmidt, Michael Stal, Hans Rohnert and Frank Buschmann, the Monitor Object
     * design patter is relying on the Active Object design pattern wich decouples method execution
     * from method invocation to enhance concurrency and simplify synchronized access to object that reside in
     * their threads of control.
     * The Monitor object design patter syncrhonizes concurrent method execution to ensure that only one method
     * at a time runs within an object. It also allows an object's method to schedule ther execution sequences cooperatively
     * Clients can only access the functions defined by a monitor object via its syncrhonized methods.
     *
     * @param configFile Path of the .json file
     * @param primary    Primary type of policy
     * @param secundary  Secundary type of policy
     * @throws ConfigException
     * @throws NoPlaceInvariants
     */

    public Monitor(String configFile, policyType primary, policyType secundary) throws ConfigException, NoPlaceInvariants {
        AcquireData data = new AcquireData(configFile);
        petrinet = new PetriNetwork(data.getData(LoadPetriNetwork.class));
        queues = new QueueMonitorManagment(data.getData(LoadQueues.class));
        semaphore = new Semaphore(1, true);
        state = new Status();
        policy = new Policy(queues, primary, secundary, data.getData(PolicyStaticRAW.class));
        observers = new ArrayList<>();
        addObserver(logger);

    }

    /**
     * Synchronized method. Using the core method of the Petri Network class and the Queue Managment
     * this method operates in the following way:
     * Semaphores are another data structure that provides mutual exclusion to critical sections, they were
     * described by Dijkstra in THE system in 1968. Semaphores support two operations:
     * wait() in Java is acquiere() wich decrement, block until semaphore is open also know it
     * by P() (Dutch word) and the signal(), wich in Java is release(). Increment, allow another
     * thread to enter (V() in Dutch word).
     * Associated with each semaphore is a queue of waitingprocesses.
     * When acquiere() is called by a thread:
     * -- If semaphore is open, thread continues
     * -- If semaphore is closed, thread blocks on queue
     * When release() opens the semaphore:
     * -- If a thread is waiting on the queue, the thread is unblocked
     * -- If no threads are waiting on the queue, the signal isremembered for the next thread
     * Once that thread enter to the Monitor, will try to make the fire calling to method makeShot, if
     * the conditional flag is true, wich it's assigment by the result of makeShot, the method sets
     * new times to sleep on the threads using the long vector of the Petri Network.
     * After that, it ask for the conjuntion of the transitions that are sensitized and
     * are waiting in a queue to make fire. The result of this conjuntion if it's true,
     * call the thread sleep on the queue in accordance with the policy and return the control of the method
     * to the thread wake.
     * If the flag it's False, it call to method getSpecificWaitTime to see how much remainding time it's necessary, if the
     * result it's -1 it come back to control the method.
     * After that, the semaphore it's realease, the thread it's add to the queue where this method toSleep ask for the remaining
     * time for the transition and if it's possible to make fire, give the possibility to the thread.
     * All it's possible if the flag it's True, and allways it's finally release the semaphore.
     * @param fire Number of transition to fire
     * @throws InterruptedException
     * @throws FiringException
     * @throws NoPlaceInvariants
     * @throws NullMatrixException
     * @throws IllegalDimensionException
     */


    public void adquireAndFire(int fire) throws InterruptedException, FiringException, NoPlaceInvariants, NullMatrixException, IllegalDimensionException {
        this.semaphore.acquire(); // >> InterruptExcp, Si se lo saca de la cola del monitor antes que entre

        boolean[] whoIsThere;
        boolean autoWakeUp;
        long sleeptime;


        do {

            this.state.loopIn = petrinet.makeShot(fire);
            if (this.state.loopIn) {
                queues.setNewTimesToSleep(petrinet.getWaitTime());
                whoIsThere = ArithmeticCalculations.vectorANDvector(petrinet.getSensitizedArray(), queues.whoIsWaiting());
                if (this.thereIsAnybody(whoIsThere)) {
                    queues.wakeUp(policy.tellMeWho(whoIsThere));
                    return;
                } else
                    this.state.loopIn = false;
            } else {
                sleeptime = petrinet.getSpecificWaitTime(fire);
                if (sleeptime == -1) {
                    this.state.loopIn = true;
                    continue;
                }

                this.semaphore.release();
                autoWakeUp = this.queues.toSleep(fire, sleeptime);
                if (autoWakeUp) {
                    this.semaphore.acquire();
                    this.state.loopIn = true;
                }
            }
            String msg = "Thread [" + Thread.currentThread().getId() + "].Shot: " + fire + ".Mark: " + Arrays.toString(petrinet.getSigma_MarkVector());
            notifyObserver(msg, 2);
        } while (this.state.loopIn);

        this.semaphore.release();

    }

    /**
     * Return <code>True</code> if there is at least one element true
     * @param v vector
     * @return
     */

    public boolean thereIsAnybody(boolean v[]) {
        if (v == null) {
            return false;
        }
        for (boolean element : v) {
            if (element) {
                return true;
            }
        }
        return false;
    }

    public void forceUpPol(int[] forceUp) {
        this.policy.setForceUp(forceUp);
    }

    public void forceDownPol(int[] forceDown) {
        this.policy.setForceDown(forceDown);
    }

    /*
     * ***********************************************************************************************
     *                                                                                               *
     *                           Getters & methods innerents to class                                *
     *                                                                                               *
     * ***********************************************************************************************
     */


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

    @Contract(pure = true)
    public static Log getLogger() {
        return logger;
    }

}

class Status {
    /**
     * Utilizada para saber si tiene debe ejecutar el algoritmo del monitor para los procedimientos
     */
    boolean loopIn;

    @SuppressWarnings("unused")
    Status() {
        this.loopIn = true;
    }
}