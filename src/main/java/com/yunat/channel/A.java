package com.yunat.channel;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * @author hewei
 * 
 * @date 2015/4/10  16:41
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class A {

    private static CyclicBarrier cb = new CyclicBarrier(4);

    private static Queue<String> aaa = new LinkedBlockingQueue<>();

    private static Queue<String> bbb = new LinkedBlockingQueue<>();

    private static Queue<String> ccc = new LinkedBlockingQueue<>();

    private static Queue<String> ddd = new LinkedBlockingQueue<>();

    static Queue<String>[] aaaa = new Queue[]{aaa, bbb, ccc, ddd};

    static boolean isOver=false;

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        new AA(0, "A").start();
        new AA(1, "D").start();
        new AA(2, "C").start();
        new AA(3, "B").start();
        Thread.sleep(50);
        isOver = true;
        for (Queue<String> q : aaaa) {
            System.out.println(Arrays.toString(q.toArray(new String[q.size()])));
        }
    }

    static class AA extends Thread {

        private int m;

        private String n;

        public AA(int m, String n) {
            this.m = m;
            this.n = n;
        }

        @Override
        public void run() {

            while (true) {

                m = m >= 4 ? 0 : m;

                try {
                    cb.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (isOver) {
                    break;
                }

                aaaa[m].add(n);

                m++;
            }
        }
    }

}


