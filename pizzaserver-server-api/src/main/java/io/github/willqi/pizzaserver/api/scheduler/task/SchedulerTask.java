package io.github.willqi.pizzaserver.api.scheduler.task;

import java.util.UUID;

public interface SchedulerTask {

    /** Executes the task. */
    void run();

    /** Sets the task as cancelled. */
    void cancel();

    /** @return the unique id of the task. */
    UUID getTaskID();

    /** @return true if the task has been cancelled. */
    boolean isCancelled();

}
