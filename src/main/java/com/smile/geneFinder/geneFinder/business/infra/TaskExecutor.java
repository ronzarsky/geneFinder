package com.smile.geneFinder.geneFinder.business.infra;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class TaskExecutor {

    final int numThreads;

    protected CountDownLatch latch;

    public TaskExecutor(final int numThreads) {
        this.numThreads = numThreads;
        this.latch = new CountDownLatch(numThreads);
    }

    public void executeTask(Runnable task) {
        int numThreads = this.numThreads;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        IntStream.range(0, numThreads).forEach(item -> executor.execute(task));
        try {
            this.latch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
