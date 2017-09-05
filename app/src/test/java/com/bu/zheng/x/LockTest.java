package com.bu.zheng.x;

import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by BuZheng on 2017/8/4.
 */

public class LockTest {

    class Account {
        private ReentrantLock lock = new ReentrantLock();

        private String accountNo;
        private int balance;

        public Account(String accountNo, int balance) {
            this.accountNo = accountNo;
            this.balance = balance;
        }

        public void draw(int drawAmount) {
            lock.lock();
            try {
                if (balance >= drawAmount) {
                    System.out.println(Thread.currentThread().getName() + " draw success:" + drawAmount);

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        System.out.println(e.getStackTrace());
                    }
                    balance = balance - drawAmount;
                    System.out.println(Thread.currentThread().getName() + " balance:" + balance);

                } else {
                    System.out.println(Thread.currentThread().getName() + " draw fail");
                }
            } finally {
                lock.unlock();
            }
        }

        @Override
        public int hashCode() {
            return accountNo.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj != null && obj.getClass() == Account.class) {
                return ((Account) obj).accountNo.equals(accountNo);
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
            account.draw(drawAmount);
        }
    }

    @Test
    public void test6() {
        Account account = new Account("123", 1000);
        Thread thread1 = new DrawThread("thread1", account, 500);
        Thread thread2 = new DrawThread("thread2", account, 500);
        Thread thread3 = new DrawThread("thread2", account, 500);
        Thread thread4 = new DrawThread("thread2", account, 500);
        Thread thread5 = new DrawThread("thread2", account, 500);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
    }
}
