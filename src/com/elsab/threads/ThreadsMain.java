package com.elsab.threads;

public class ThreadsMain {

    public static void main(String[] args){
        System.out.println("Starting threads....");
        Thread t1 = new Thread(new Task(), "Thread 1");
        Thread t2 = new Thread(new Task(), "Thread 2");

        t1.start();
        t2.start();
    }
}
