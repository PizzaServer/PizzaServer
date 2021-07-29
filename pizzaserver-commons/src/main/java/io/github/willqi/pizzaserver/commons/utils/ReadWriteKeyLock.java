package io.github.willqi.pizzaserver.commons.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

public class ReadWriteKeyLock<K> {

    private final Map<K, ReadWriteLock> locks = new HashMap<>();

    private final Map<K, Map<Long, Integer>> readLockCount = new HashMap<>();
    private final Map<K, Map<Long, Integer>> writeLockCount = new HashMap<>();


    public void readLock(K key) {

    }

    public boolean tryReadLock(K key) {
        return false;
    }

    public void readUnlock(K key) {

    }

    public void writeLock(K key) {

    }

    public boolean tryWriteLock(K key) {
        return false;
    }

    public void writeUnlock(K key) {

    }

}
