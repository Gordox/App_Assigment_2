package gg.gordoxgaming.googlemapsapp;

import java.util.LinkedList;

/**
 * Created by Gordox on 2016-12-13.
 */

public class ThreadManager {

    public class Buffer<T> {
        private LinkedList<T> buffer = new LinkedList<T>();

        public synchronized void put(T obj) {
            buffer.addLast(obj);
            notifyAll();
        }

        public synchronized T get() throws InterruptedException {
            while(buffer.isEmpty()) {
                wait();
            }
            return buffer.removeFirst();
        }
    }

    private Buffer<Runnable> tasks = new Buffer<>();





    void addTask(Runnable runnable) {
        tasks.put(runnable);
    }




    private class myThread extends Thread{
        public void run(){

        }
    }


}
