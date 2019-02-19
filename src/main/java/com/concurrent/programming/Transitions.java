package com.concurrent.programming;

public class Transitions {
    /**
     * Nombre del thread
     */
    String id;
    /**
     * Ciclo de disparos a realizar
     */
    int[] sequence;
    /**
     * Tiempo a dormir despues de la secuencia
     */
    long sleepTime;
    /**
     * Cantidad de veces antes de terminar el ciclo
     */
    int numberOfTimes;
}
