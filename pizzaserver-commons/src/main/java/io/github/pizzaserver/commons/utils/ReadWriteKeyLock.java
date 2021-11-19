package io.github.pizzaserver.commons.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Similar to the {@link ReadWriteLock}, however, this lock locks based off of keys rather than a singular object.
 * @param <K> key type
 */
public class ReadWriteKeyLock<K> {

    private final Map<K, ReadWriteLock> locks = new HashMap<>();

    private final Map<K, Map<Long, Integer>> readLockCount = new HashMap<>();
    private final Map<K, Map<Long, Integer>> writeLockCount = new HashMap<>();


    /**
     * Obtain a read lock.
     * @param key key of the lock
     */
    public void readLock(K key) {
        ReadWriteLock readWriteLock;
        synchronized (this.locks) {
            this.incrementReadCount(key);
            this.locks.computeIfAbsent(key, ignored -> new ReentrantReadWriteLock());
            readWriteLock = this.locks.get(key);
        }
        readWriteLock.readLock().lock();
    }

    /**
     * Try to obtain a read lock.
     * @param key key of the lock
     * @return if the lock was obtained
     */
    public boolean tryReadLock(K key) {
        synchronized (this.locks) {
            this.locks.computeIfAbsent(key, ignored -> new ReentrantReadWriteLock());
            if (this.locks.get(key).readLock().tryLock()) {
                this.incrementReadCount(key);
                return true;
            }
        }
        return false;
    }

    /**
     * Release a hold on a read lock.
     * @param key key of the lock
     */
    public void readUnlock(K key) {
        ReadWriteLock readWriteLock;
        synchronized (this.locks) {
            this.checkUnlockConditions(this.readLockCount, key);

            readWriteLock = this.locks.get(key);
        }
        readWriteLock.readLock().unlock();
        synchronized (this.locks) {
            this.decrementReadCount(key);
        }
    }

    /**
     * Increment the amount of holds on the read lock of a key.
     * @param key key of the lock
     */
    private void incrementReadCount(K key) {
        this.readLockCount.computeIfAbsent(key, ignored -> new HashMap<>());

        long threadId = Thread.currentThread().getId();
        if (this.readLockCount.get(key).containsKey(threadId)) {
            int newLockCount = this.readLockCount.get(key).get(threadId) + 1;
            this.readLockCount.get(key).put(threadId, newLockCount);
        } else {
            this.readLockCount.get(key).put(threadId, 1);
        }
    }

    /**
     * Decrement the amount of holds on the read lock by a key.
     * It is required to ensure that the key exists in readLockCount and also in locks
     * @param key key of the lock
     */
    private void decrementReadCount(K key) {
        long threadId = Thread.currentThread().getId();
        int newLockCount = this.readLockCount.get(key).get(threadId) - 1;
        if (newLockCount == 0) {
            this.readLockCount.get(key).remove(threadId);

            if (this.readLockCount.get(key).isEmpty()) {
                this.readLockCount.remove(key);
                this.tryRemovingLock(key);
            }
        } else {
            this.readLockCount.get(key).put(threadId, newLockCount);
        }
    }

    /**
     * Obtain write lock.
     * @param key the key to obtain the lock on
     */
    public void writeLock(K key) {
        ReadWriteLock readWriteLock;
        synchronized (this.locks) {
            this.incrementWriteCount(key);
            this.locks.computeIfAbsent(key, ignored -> new ReentrantReadWriteLock());
            readWriteLock = this.locks.get(key);
        }
        readWriteLock.writeLock().lock();
    }

    /**
     * Try to obtain write lock.
     * @param key the key to obtain the lock on
     * @return if the lock was obtained
     */
    public boolean tryWriteLock(K key) {
        synchronized (this.locks) {
            this.locks.computeIfAbsent(key, ignored -> new ReentrantReadWriteLock());
            if (this.locks.get(key).writeLock().tryLock()) {
                this.incrementWriteCount(key);
                return true;
            }
        }
        return false;
    }

    /**
     * Unlock write lock held under a key.
     * @param key the key of the lock being released
     */
    public void writeUnlock(K key) {
        ReadWriteLock readWriteLock;
        synchronized (this.locks) {
            this.checkUnlockConditions(this.writeLockCount, key);

            readWriteLock = this.locks.get(key);
        }
        readWriteLock.writeLock().unlock();
        synchronized (this.locks) {
            this.decrementWriteCount(key);
        }
    }

    /**
     * Increment the amount of holds on a write lock of a key.
     * @param key key of the lock
     */
    private void incrementWriteCount(K key) {
        this.writeLockCount.computeIfAbsent(key, ignored -> new HashMap<>());

        long threadId = Thread.currentThread().getId();
        if (this.writeLockCount.get(key).containsKey(threadId)) {
            int newLockCount = this.writeLockCount.get(key).get(threadId) + 1;
            this.writeLockCount.get(key).put(threadId, newLockCount);
        } else {
            this.writeLockCount.get(key).put(threadId, 1);
        }
    }

    /**
     * Decrement the amount of holds on the write lock by a key.
     * It is required to ensure that the key exists in writeLockCount and also in locks
     * @param key key of the lock
     */
    private void decrementWriteCount(K key) {
        long threadId = Thread.currentThread().getId();

        int newLockCount = this.writeLockCount.get(key).get(threadId) - 1;
        if (newLockCount == 0) {
            this.writeLockCount.get(key).remove(threadId);

            if (this.writeLockCount.get(key).isEmpty()) {
                this.writeLockCount.remove(key);
                this.tryRemovingLock(key);
            }
        } else {
            this.writeLockCount.get(key).put(threadId, newLockCount);
        }
    }

    /**
     * Remove the lock from the locks cache if the lock count for both read and writes are 0.
     * @param key key of the lock
     */
    private void tryRemovingLock(K key) {
        boolean writeIsEmpty = !this.writeLockCount.containsKey(key) || this.writeLockCount.get(key).isEmpty();
        boolean readIsEmpty = !this.readLockCount.containsKey(key) || this.readLockCount.get(key).isEmpty();
        if (writeIsEmpty && readIsEmpty) {
            this.locks.remove(key);
        }
    }

    private void checkUnlockConditions(Map<K, Map<Long, Integer>> map, K key) {
        this.checkIfLockExists(key);
        this.checkIsHoldingLock(map, key);
    }

    private void checkIfLockExists(K key) {
        if (!this.locks.containsKey(key)) {
            throw new IllegalArgumentException("The key provided does not have any locks held");
        }
    }

    private void checkIsHoldingLock(Map<K, Map<Long, Integer>> map, K key) {
        long threadId = Thread.currentThread().getId();
        if (!map.get(key).containsKey(threadId)) {
            throw new IllegalStateException("This thread does not hold any locks on this key");
        }
    }

}
