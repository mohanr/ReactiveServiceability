package com.memory.probe;

import java.util.concurrent.CountDownLatch;

public class SleepingVM {

    public static void main(String... argv) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        
        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }

}
