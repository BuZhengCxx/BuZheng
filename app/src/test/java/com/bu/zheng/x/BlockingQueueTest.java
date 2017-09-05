package com.bu.zheng.x;

import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by BuZheng on 2017/8/4.
 */

public class BlockingQueueTest {

    class Producer implements Runnable {

        private volatile boolean isRunning = true;

        private BlockingQueue<String> blockingQueue;

        private AtomicInteger count = new AtomicInteger();

        public Producer(BlockingQueue<String> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            String data;
            try {
                while (isRunning) {
                    data = "data " + count.incrementAndGet();
                    blockingQueue.put(data);
                    System.out.println("producer put data:" + data);
                }
            } catch (InterruptedException e) {

            } finally {
                isRunning = false;
            }
        }

        public void stop() {
            isRunning = false;
        }
    }

    class Consumer implements Runnable {

        private BlockingQueue<String> queue;

        private volatile boolean isRunning = true;

        public Consumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        public void run() {

            try {
                while (isRunning) {
                    System.out.println("consumer get data:" + queue.take());
                }

            } catch (InterruptedException e) {

            } finally {
                isRunning = false;
            }
        }

        public void stop() {
            isRunning = false;
        }
    }

    @Test
    public void test() throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(5);

        Producer producer1 = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(producer1);
        service.execute(consumer);

        Thread.sleep(100);

        producer1.stop();
        consumer.stop();
        service.shutdown();
    }
}
