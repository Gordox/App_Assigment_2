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

    private class TaskWorker extends Thread {
        public void run() {
            Runnable runnable;
            while (taskWorker != null) {
                try {
                    runnable = tasks.get();
                    runnable.run();
                } catch (InterruptedException e) {
                    taskWorker = null;
                }
            }
        }
    }

    private Buffer<Runnable> tasks = new Buffer<>();
    private TaskWorker taskWorker;


    void start() {
        if(taskWorker ==null) {
            taskWorker = new TaskWorker();
            taskWorker.start();
        }
    }

    void stop() {
        if(taskWorker !=null) {
            taskWorker.interrupt();
            taskWorker =null;
        }
    }



    void addTask(Runnable runnable) {
        tasks.put(runnable);
    }



}
