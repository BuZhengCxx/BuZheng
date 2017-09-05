package com.bu.zheng.x;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by BuZheng on 2017/8/3.
 */

public class WaitTest {

    class Account {
        private String account;
        private int balance;

        private boolean flag;

        public Account(String account, int balance) {
            this.account = account;
            this.balance = balance;
        }

        public synchronized void draw(int drawAmount) {
            try {
                while (!flag) {
                    wait();
                }
                System.out.println(Thread.currentThread().getName() + " draw " + drawAmount);
                flag = false;
                balance -= drawAmount;
                notifyAll();

            } catch (InterruptedException e) {

            }
        }

        public synchronized void deposit(int depositAmount) {
            try {
                while (flag) {
                    wait();
                }
                System.out.println(Thread.currentThread().getName() + " deposit " + depositAmount);
                flag = true;
                balance += depositAmount;
                notifyAll();

            } catch (InterruptedException e) {

            }
        }

        @Override
        public int hashCode() {
            return account.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj != null && obj.getClass() == this.getClass()) {
                return ((Account) obj).account.equals(account);
            }
            return false;
        }
    }

    class DrawThread extends Thread {
        private Account account;
        private int drawAmount;

        public DrawThread(String name, Account account, int drawAmount) {
            super(name);
            this.account = account;
            this.drawAmount = drawAmount;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                account.draw(drawAmount);
            }
        }
    }

    class DepositThread extends Thread {
        private Account account;
        private int depositAmount;

        public DepositThread(Account account, int depositAmount) {
            this.account = account;
            this.depositAmount = depositAmount;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                account.deposit(depositAmount);
            }
        }
    }

    @Test
    public void test1() {
        Account account = new Account("123", 0);
        DepositThread depositThread = new DepositThread(account, 800);
        DrawThread drawThread1 = new DrawThread("thread1", account, 800);
        //DrawThread drawThread2 = new DrawThread("thread2", account, 800);
        //DrawThread drawThread3 = new DrawThread("thread3", account, 800);

        depositThread.start();
        drawThread1.start();
        //drawThread2.start();
        //drawThread3.start();
    }

    class Producer implements Runnable {

        private final List<Integer> taskQueue;
        private final int MAX_CAPACITY;

        public Producer(List<Integer> sharedQueue, int size) {
            this.taskQueue = sharedQueue;
            this.MAX_CAPACITY = size;
        }

        @Override
        public void run() {
            AtomicInteger count = new AtomicInteger();
            while (true) {
                try {
                    produce(count.incrementAndGet());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void produce(int i) throws InterruptedException {
            synchronized (taskQueue) {
                while (taskQueue.size() == MAX_CAPACITY) {
                    System.out.println("Queue is full " + Thread.currentThread().getName() + " is waiting , size: " + taskQueue.size());
                    taskQueue.wait();
                }

                Thread.sleep(10);
                taskQueue.add(i);
                System.out.println("Produced: " + i);
                taskQueue.notifyAll();
            }
        }
    }

    class Consumer implements Runnable {

        private final List<Integer> taskQueue;

        public Consumer(List<Integer> sharedQueue) {
            this.taskQueue = sharedQueue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    consume();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void consume() throws InterruptedException {
            synchronized (taskQueue) {
                while (taskQueue.isEmpty()) {
                    System.out.println("Queue is empty " + Thread.currentThread().getName() + " is waiting , size: " + taskQueue.size());
                    taskQueue.wait();
                }
                Thread.sleep(10);
                int i = taskQueue.remove(0);
                System.out.println("Consumed: " + i);
                taskQueue.notifyAll();
            }
        }
    }

    @Test
    public void test2() {
        List<Integer> taskQueue = new ArrayList<>();
        Thread tProducer = new Thread(new Producer(taskQueue, 5), "Producer");
        Thread tConsumer = new Thread(new Consumer(taskQueue), "Consumer");
        tProducer.start();
        tConsumer.start();
    }
}
