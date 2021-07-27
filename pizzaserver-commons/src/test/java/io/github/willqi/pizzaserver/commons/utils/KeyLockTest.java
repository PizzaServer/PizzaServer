package io.github.willqi.pizzaserver.commons.utils;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class KeyLockTest extends Thread {

    @Test
    public void shouldNotAllowMeToUnlockIfObtainedNoLocks() throws InterruptedException {
        KeyLock<Integer> keyLock = new KeyLock<>();
        keyLock.lock(1);
        keyLock.unlock(1);

        assertThrows(IllegalArgumentException.class, () -> keyLock.unlock(1), "unlocked non-existant key");

        keyLock.lock(2);
        Thread testThread = new Thread(() -> assertThrows(IllegalStateException.class, () -> keyLock.unlock(2)));

        testThread.start();
        testThread.join();
    }

    @RepeatedTest(50)
    public void shouldBlockOtherThreadsWhenLockObtained() throws InterruptedException {
        KeyLock<Integer> keyLock = new KeyLock<>();
        keyLock.lock(1);

        AtomicBoolean failed = new AtomicBoolean(false);    // If the inner thread acquired the lock too early
        CountDownLatch latch = new CountDownLatch(1);

        Thread testThread = new Thread(() -> {
            if (keyLock.tryLock(1)) {
                failed.set(true);
            }
            latch.countDown();
        });
        testThread.start();
        latch.await();

        if (failed.get()) {
            fail("testThread obtained lock before it should have been able to.");
        }

    }

}
