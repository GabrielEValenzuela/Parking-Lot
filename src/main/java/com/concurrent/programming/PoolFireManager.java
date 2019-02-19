package com.concurrent.programming;

import com.concurrent.programming.loaders.LoadTransitions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class PoolFireManager {

    private ArrayList<FireManager> listOfTransitions;
    private ExecutorService executor;

    public PoolFireManager(@NotNull LoadTransitions transitions, Monitor monitor) {
        listOfTransitions = new ArrayList<>();
        for (int i = 0; i < transitions.transitions.length; i++) {
            listOfTransitions.add(new FireManager(transitions.transitions[i], monitor));
        }
    }

    public ArrayList<FireManager> init() {
        return this.listOfTransitions;
    }
}
