package io.github.pizzaserver.commons.utils;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class ReadWriteKeyLockTest {

    @Test
    public void shouldThrowWhenReleasingLockWeDoNotOwn() throws InterruptedException {
        ReadWriteKeyLock<Integer> lock = new ReadWriteKeyLock<>();

        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            lock.writeLock(1);
            latch.countDown();
        }).start();
        latch.await();

        assertThrows(IllegalStateException.class, () -> lock.writeUnlock(1));
    }

    @Test
    public void shouldThrowWhenReleasingLockNotCreated() {
        ReadWriteKeyLock<Integer> lock = new ReadWriteKeyLock<>();
        assertThrows(IllegalArgumentException.class, () -> lock.writeUnlock(1));
    }

    @Test
    public void shouldNotLetReadLockBeAcquiredWhileHoldingWriteLock() throws InterruptedException {
        ReadWriteKeyLock<Integer> lock = new ReadWriteKeyLock<>();

        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            lock.writeLock(1);
            latch.countDown();
        }).start();
        latch.await();

        assertFalse(lock.tryReadLock(1), "acquired read lock despite write lock being held");
    }

    @Test
    public void shouldNotLetWriteLockBeAcquiredWhileHoldingReadLock() throws InterruptedException {
        ReadWriteKeyLock<Integer> lock = new ReadWriteKeyLock<>();

        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            lock.readLock(1);
            latch.countDown();
        }).start();
        latch.await();

        assertFalse(lock.tryWriteLock(1));
    }

    @Test
    public void shouldLetMultipleThreadsHoldReadLock() throws InterruptedException {
        ReadWriteKeyLock<Integer> lock = new ReadWriteKeyLock<>();

        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            lock.readLock(1);
            latch.countDown();
        }).start();
        latch.await();

        assertTrue(lock.tryReadLock(1),
                   "Was unable to acquire read lock on another thread while holding one on the main thread despite no write locks");
    }

    @Test
    public void shouldOnlyLetOneThreadHoldWriteLock() throws InterruptedException {
        ReadWriteKeyLock<Integer> lock = new ReadWriteKeyLock<>();

        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            lock.writeLock(1);
            latch.countDown();
        }).start();
        latch.await();

        assertFalse(lock.tryWriteLock(1), "was able to retrieve write lock despite another thread holding it");
    }

    @Test
    public void shouldAcquireWriteLocksOfMultipleKeys() throws InterruptedException {
        ReadWriteKeyLock<Integer> lock = new ReadWriteKeyLock<>();

        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            lock.writeLock(1);
            latch.countDown();
        }).start();
        latch.await();

        assertTrue(lock.tryWriteLock(2), "was unable to acquire a free write key when another write key was held");
    }

    @Test
    public void shouldBeAbleToAcquireSameLockOverAndOver() {
        ReadWriteKeyLock<Integer> lock = new ReadWriteKeyLock<>();

        lock.readLock(1);
        assertTrue(lock.tryReadLock(1), "was unable to acquire read lock twice on the same thread");

        lock.writeLock(2);
        assertTrue(lock.tryWriteLock(2), "was unable to acquire write lock twice on the same thread");
    }
}
