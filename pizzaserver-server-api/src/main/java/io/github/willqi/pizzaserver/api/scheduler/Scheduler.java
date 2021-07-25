package io.github.willqi.pizzaserver.api.scheduler;

import io.github.willqi.pizzaserver.api.scheduler.task.SchedulerTask;

public interface Scheduler {

    PendingEntryBuilder prepareTask(Runnable task);

    PendingEntryBuilder prepareTask(SchedulerTask task);

    /** @return true if the scheduler is currently active. */
    boolean isRunning();


    interface PendingEntryBuilder {

        SchedulerTask schedule();

        int getInterval();

        PendingEntryBuilder setInterval(int interval);

        int getDelay();

        PendingEntryBuilder setDelay(int delay);

        boolean isAsynchronous();

        PendingEntryBuilder setAsynchronous(boolean asynchronous);

    }

}
