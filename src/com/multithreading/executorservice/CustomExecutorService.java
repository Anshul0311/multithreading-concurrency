package com.multithreading.executorservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;

public class CustomExecutorService<t> {

    private static int DEFAULT_POOL_SIZE = 5;
    private static int DEFAULT_QUEUE_SIZE = 5;

    private int poolSize;
    private List<WorkerThread> workerThreads;
    private Queue<Runnable> taskQueue;

    public CustomExecutorService() {
        this(DEFAULT_POOL_SIZE, DEFAULT_POOL_SIZE);
    }

    public CustomExecutorService(int poolSize, int maxQueueSize) {
        this.poolSize = poolSize;
        this.taskQueue = new LinkedBlockingQueue<>(maxQueueSize);
        this.workerThreads = new ArrayList<>();

        for (int i = 0; i < poolSize; i++) {
            WorkerThread worker = new WorkerThread();
            workerThreads.add(worker);
            worker.start();
        }
    }

    public void execute(Runnable command) {
        if (!isShutdown()) {
            try {
                taskQueue.add(command);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        } else {
            throw new RejectedExecutionException("ExecutorService is shut down");
        }
    }

    public boolean isShutdown() {
        return workerThreads.isEmpty();
    }

    private class WorkerThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    while (!Thread.currentThread().isInterrupted() && !taskQueue.isEmpty()) {
                        Runnable task = taskQueue.poll();
                        task.run();
                    }
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
