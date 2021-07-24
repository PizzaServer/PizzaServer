package io.github.willqi.pizzaserver.server.scheduler.task;

import io.github.willqi.pizzaserver.commons.utils.Check;

public final class RunnableTypeTask extends SchedulerTask {

    private final Runnable taskRunnable;

    public RunnableTypeTask(Runnable taskRunnable) {
        Check.nullParam(taskRunnable, "taskRunnable");
        this.taskRunnable = taskRunnable;
    }

    @Override
    public void run() {
        taskRunnable.run();
    }
}
