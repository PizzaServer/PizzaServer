package io.github.pizzaserver.api.scheduler.task;

import io.github.pizzaserver.commons.utils.Check;

public final class RunnableTypeTask extends SchedulerTask {

    private final Runnable taskRunnable;

    public RunnableTypeTask(Runnable task) {
        this.taskRunnable = Check.nullParam(task, "task");
    }

    @Override
    public void run() {
        this.taskRunnable.run();
    }
}
