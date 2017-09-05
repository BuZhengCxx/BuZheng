package com.bu.zheng.x;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BuZheng on 2017/8/2.
 */

public class ThreadTest {

    class PrintThread extends Thread {

        public PrintThread(String name) {
            super(name);
        }

        private int i = 0;

        @Override
        public void run() {
            for (; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + ":" + i);
            }
        }
    }

    class YieldThread extends Thread {

        public YieldThread(String name) {
            super(name);
        }

        private int i = 0;

        @Override
        public void run() {
            for (; i < 100; i++) {
                if (i == 20) {
                    Thread.yield();
                }
                System.out.println(Thread.currentThread().getName() + ":" + i);
            }
        }
    }

    @Test
    public void test1() {
        int i = 0;
        for (; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
            if (i == 20) {
//                new TestThread("test1").run();
//                new TestThread("test2").run();

                new PrintThread("test1").start();
                new PrintThread("test2").start();
            }
        }
    }

    @Test
    public void test2() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            if (i == 20) {
                PrintThread joinThread = new PrintThread("join thread");
                joinThread.start();
                joinThread.join();
            }
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }

    @Test
    public void test3() {
        PrintThread daemonThread = new PrintThread("daemon thread");
        daemonThread.setDaemon(true);
        daemonThread.start();
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }

    @Test
    public void test4() throws InterruptedException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd日hh时mm分ss秒");
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + sdf.format(new Date()));
            Thread.sleep(1000);
        }
    }

    @Test
    public void test5() {
        Thread thread1 = new YieldThread("高优先级");
        thread1.setPriority(Thread.MAX_PRIORITY);
        thread1.start();

        Thread thread2 = new YieldThread("低优先级");
        thread2.setPriority(Thread.MIN_PRIORITY);
        thread2.start();
    }
}
