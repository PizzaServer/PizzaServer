package io.github.willqi.pizzaserver.server.scheduler.task;

import io.github.willqi.pizzaserver.api.scheduler.task.SchedulerTask;

import java.util.UUID;

public abstract class BaseSchedulerTask implements SchedulerTask {

    //TODO: Add plugin so all plugin tasks can be disabled when the plugin is disabled.
    private UUID taskID;
    private boolean isCancelled;

    public BaseSchedulerTask() {
        this.taskID = UUID.randomUUID();
        this.isCancelled = false;
    }

    @Override
    public final void cancel() {
        this.isCancelled = true;
    }

    @Override
    public UUID getTaskID() {
        return this.taskID;
    }

    @Override
    public final boolean isCancelled() {
        return this.isCancelled;
    }
}
