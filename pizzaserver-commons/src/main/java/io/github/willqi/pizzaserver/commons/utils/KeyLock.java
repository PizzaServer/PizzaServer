package io.github.willqi.pizzaserver.commons.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Locks a key preventing other threads from acquiring that key until the Thread holding that lock unlocks it.
 */
public class KeyLock<K> {

    private final Map<K, Lock> locks = new HashMap<>();

    // Stores how many locks a key has before it can be removed from the lock cache
    // key: [threadId : locksObtained]
    private final Map<K, Map<Long, Integer>> lockCount = new HashMap<>();

    /**
     * Obtain a lock on a key, blocking any other Thread from obtaining this lock until it is unlocked by the capturing Thread.
     * @param key Key to assign the lock under
     */
    public void lock(K key) {
        Check.nullParam(key, "key");
        Lock lock;
        // synchronized so that we can increment the lock count and avoid race conditions where unlock is
        // triggered before the lock count is incremented.
        synchronized (this.locks) {
            lock = this.getLockForLocking(key);
            this.incrementLockCount(key);
        }
        lock.lock();
    }

    /**
     * Attempt to obtain a lock on a key. Blocking any other Thread from obtaining this lock until it is unlocked by the capturing Thread.
     * @param key Key to assign the lock under
     * @return whether of not acquiring the lock was successful
     */
    public boolean tryLock(K key) {
        Check.nullParam(key, "key");
        // synchronized so that we can increment the lock count and avoid race conditions where unlock is
        // triggered before the lock count is incremented.
        synchronized (this.locks) {
            Lock lock = this.getLockForLocking(key);
            if (lock.tryLock()) {
                this.incrementLockCount(key);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Retrieve the lock used for LOCKING.
     * If no lock exists, a new lock is created.
     * @param key the key of the lock
     * @return the {@link Lock}
     */
    private Lock getLockForLocking(K key) {
        if (this.locks.containsKey(key)) {
            return this.locks.get(key);
        } else {
            Lock lock = new ReentrantLock();
            this.locks.put(key, lock);
            return lock;
        }
    }

    /**
     * Increment the amount of locks held on a key
     * This should be called while holding a synchronization lock on the this.locks property
     * @param key the key of the lock to be incremented
     */
    private void incrementLockCount(K key) {
        long threadId = Thread.currentThread().getId();

        if (!this.lockCount.containsKey(key)) {
            this.lockCount.put(key, new HashMap<>());
        }
        int currentLockCount = this.lockCount.get(key).getOrDefault(threadId, 0);
        this.lockCount.get(key).put(threadId, currentLockCount + 1);
    }


    /**
     * Unlock a lock the current Thread has obtained using the key given.
     * @param key key the lock was assigned under
     */
    public void unlock(K key) {
        Check.nullParam(key, "key");
        Lock lock;
        synchronized (this.locks) {
            lock = this.getLockForUnlocking(key);
            this.decrementLockCount(key);
        }
        lock.unlock();
    }

    /**
     * Retrieve the lock associated with a key if one exists.
     * Otherwise an exception will be thrown.
     * @param key the key associated with the lock
     * @return the {@link Lock} under this key
     * @throws IllegalArgumentException if no lock exists under this key
     * @throws IllegalStateException if the thread trying to unlock this key does not have a lock on it
     */
    private Lock getLockForUnlocking(K key) {
        long threadId = Thread.currentThread().getId();

        // Check to make sure this key can be unlocked
        if (!this.locks.containsKey(key)) {
            throw new IllegalArgumentException("No lock exists with the key " + key);
        }
        if (!this.lockCount.get(key).containsKey(threadId)) {
            throw new IllegalStateException("This thread does not hold any locks by the key of " + key);
        }

        return this.locks.get(key);
    }

    /**
     * Decrement the amount of holds on a lock
     * This should be called while holding a synchronization lock on the this.locks property
     * @param key the key associated with the lock
     */
    private void decrementLockCount(K key) {
        long threadId = Thread.currentThread().getId();

        // Decrement lock count to clear data as needed
        int currentLockCount = this.lockCount.get(key).get(threadId);
        if (currentLockCount > 1) {
            this.lockCount.get(key).put(threadId, currentLockCount - 1);
        } else {
            this.lockCount.get(key).remove(threadId);
            if (this.lockCount.get(key).size() == 0) {
                // Nothing is holding a lock on this key
                this.lockCount.remove(key);
                this.locks.remove(key);
            }
        }
    }

}
