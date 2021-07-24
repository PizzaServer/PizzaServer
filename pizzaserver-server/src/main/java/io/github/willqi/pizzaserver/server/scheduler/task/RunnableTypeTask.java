package io.github.willqi.pizzaserver.server.scheduler.task;

import io.github.willqi.pizzaserver.commons.utils.Check;

public final class RunnableTypeTask extends SchedulerTask {

    private final Runnable taskRunnable;

    public RunnableTypeTask(Runnable task) {
        Check.nullParam(task, "task");
        this.taskRunnable = task;
    }

    @Override
    public void run() {
        taskRunnable.run();
    }
}
