package com.app.threadpoolexecutordemo;


/**
 * PriorityRunnable Created on 2016/7/13-17:47
 * Description:
 * Created by DongHao
 */
public abstract class PriorityRunnable implements Runnable,Comparable<PriorityRunnable> {
    private int priority;

    public PriorityRunnable(int priority) {
        if(priority<0){
            throw new IllegalArgumentException();
        }
        this.priority = priority;
    }

    @Override
    public void run() {
        doSth();
    }

    public abstract void doSth();

    @Override
    public int compareTo(PriorityRunnable priorityRunnable) {
        int my=getPrioity();
        int other=priorityRunnable.getPrioity();
        return my<other?1:my>other?-1:0;
    }

    private int getPrioity() {
        return priority;
    }
}
