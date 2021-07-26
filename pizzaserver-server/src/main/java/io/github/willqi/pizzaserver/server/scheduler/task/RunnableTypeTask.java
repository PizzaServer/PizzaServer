package io.github.willqi.pizzaserver.server.scheduler.task;

import io.github.willqi.pizzaserver.commons.utils.Check;

public final class RunnableTypeTask extends BaseSchedulerTask {

    private final Runnable taskRunnable;

    public RunnableTypeTask(Runnable task) {
        this.taskRunnable = Check.nullParam(task, "task");
    }

    @Override
    public void run() {
        taskRunnable.run();
    }
}
