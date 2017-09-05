package com.bu.zheng.x;

import org.junit.Test;

/**
 * Created by BuZheng on 2017/8/4.
 */

public class DeadTest {

    class A {
        public synchronized void firstA(B b) {
            System.out.println("first a");
            try {
                Thread.currentThread().sleep(200);
            } catch (InterruptedException e) {

            }
            b.lastB();
        }

        public synchronized void lastA() {
            System.out.println("last a");
        }
    }

    class B {
        public synchronized void firstB(A a) {
            System.out.println("first b");
            try {
                Thread.currentThread().sleep(200);
            } catch (InterruptedException e) {

            }
            a.lastA();
        }

        public synchronized void lastB() {
            System.out.println("last b");
        }
    }

    class BThread extends Thread {

        private A a;
        private B b;

        public BThread(A a, B b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            setName("son thread");
            b.firstB(a);
        }
    }

    @Test
    public void test7() {
        Thread.currentThread().setName("main thread");
        A a = new A();
        B b = new B();

        BThread bThread = new BThread(a, b);
        bThread.start();

        a.firstA(b);
    }
}
