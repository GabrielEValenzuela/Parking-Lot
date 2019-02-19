package com.concurrent.programming;

public interface Observable {
    public void addObserver(Observer o);

    public void removeObserver(Observer o);

    public void notifyObserver(String msg, int type);
}
