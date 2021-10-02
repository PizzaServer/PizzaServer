package io.github.willqi.pizzaserver.api.scheduler;

import io.github.willqi.pizzaserver.api.scheduler.task.SchedulerTask;
import io.github.willqi.pizzaserver.commons.utils.Check;

public final class SchedulerTaskEntry {

    private final SchedulerTask task;
    private final int repeatInterval;
    private final long nextTick;
    private final boolean isAsynchronous;

    protected SchedulerTaskEntry(SchedulerTask task, int repeatInterval, long nextTick, boolean isAsynchronous) {
        this.task = Check.nullParam(task, "task");
        this.repeatInterval = repeatInterval;
        this.nextTick = nextTick;
        this.isAsynchronous = isAsynchronous;
    }

    public SchedulerTask getTask() {
        return this.task;
    }

    public boolean isRepeating() {
        return this.repeatInterval > 0;
    }

    public boolean isAsynchronous() {
        return this.isAsynchronous;
    }

    public int getRepeatInterval() {
        return this.repeatInterval;
    }

    public long getNextTick() {
        return this.nextTick;
    }

}
