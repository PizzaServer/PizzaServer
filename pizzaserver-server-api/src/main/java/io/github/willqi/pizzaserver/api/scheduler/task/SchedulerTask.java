package io.github.willqi.pizzaserver.api.scheduler.task;

import java.util.UUID;

public interface SchedulerTask {

    /**
     * Executes the task.
     */
    void run();

    /**
     * Sets the task as cancelled.
     */
    void cancel();

    /**
     * Retrieve the unique id of the task.
     * @return the unique id of the task.
     */
    UUID getTaskID();

    /**
     * Retrieves if the task was cancelled.
     * @return true if the task has been cancelled.
     */
    boolean isCancelled();

}
