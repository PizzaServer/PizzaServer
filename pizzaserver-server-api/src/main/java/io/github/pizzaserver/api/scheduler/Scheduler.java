package io.github.pizzaserver.api.scheduler;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.scheduler.task.RunnableTypeTask;
import io.github.pizzaserver.api.scheduler.task.SchedulerTask;
import io.github.pizzaserver.commons.utils.Check;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Scheduler {

    private final UUID schedulerID;
    protected final Server server;

    protected long syncedTick;    // The last received server tick.
    protected long schedulerTick; // The scheduler's actual tick. This depends on the tickrate

    protected int tickDelay; // The amount of server ticks between each scheduler tick.

    private final ExecutorService threadPool = Executors.newCachedThreadPool(runnable -> new Thread(runnable) {
        @Override
        public void interrupt() {
            synchronized (Scheduler.this.activeThreads) {
                Scheduler.this.activeThreads.remove(this);
            }
            super.interrupt();
        }
    });
    protected final Set<Thread> activeThreads;

    protected ArrayList<SchedulerTaskEntry> schedulerTasks;
    protected boolean isRunning;

    public Scheduler(Server server, int tickDelay) {
        this.schedulerID = UUID.randomUUID();

        this.server = server;

        this.syncedTick = 0;
        this.schedulerTick = 0;
        this.tickDelay = Math.max(1, tickDelay);

        this.schedulerTasks = new ArrayList<>();
        this.activeThreads = Collections.synchronizedSet(new HashSet<>());
        this.isRunning = false;
    }

    // -- Control --

    /** Enables ticking on the scheduler. */
    public synchronized boolean startScheduler() {
        return this.startScheduler(true);
    }

    public synchronized boolean startScheduler(boolean tickWithServer) {
        if (!this.isRunning) {
            this.isRunning = true;
            if (tickWithServer) {
                this.server.syncScheduler(this);
            }
            return true;
        }
        return false;
    }

    /**
     * Removes scheduler's hook to the server tick whilst clearing the queue.
     * @return true if the scheduler was initially running and then stopped.
     */
    public synchronized boolean stopScheduler() {
        if (this.isRunning) {
            this.pauseScheduler();
            this.clearQueuedSchedulerTasks();
            return true;
        }
        return false;
    }

    /** Removes scheduler's hook to the server tick whilst clearing the queue. */
    public synchronized void pauseScheduler() {
        this.isRunning = false;
        this.server.desyncScheduler(this);
    }

    /** Clears all the tasks queued in the scheduler. */
    public synchronized void clearQueuedSchedulerTasks() {
        for (SchedulerTaskEntry entry : new ArrayList<>(this.schedulerTasks)) {
            entry.getTask().cancel(); // For the runnable to use? idk
            this.schedulerTasks.remove(entry);
        }
    }


    // -- Ticking --
    // Methods used to tick a scheduler should only be triggered by the
    // main scheduler thread.

    /**
     * Ran to indicate a server tick has occurred, potentially triggering a server tick.
     * @return true is a scheduler tick is triggered as a result.
     */
    public synchronized boolean serverTick() { // Should only be done on the main thread
        this.syncedTick++;

        // Check if synced is a multiple of the delay
        if ((this.syncedTick % this.tickDelay) == 0) {
            this.schedulerTick();
            return true;
        }
        return false;
    }

    /** Executes a scheduler tick, running any tasks due to run on this tick. */
    public synchronized void schedulerTick() {
        if (this.isRunning) {

            // To avoid stopping the scheduler from inside a task making it scream, use ArrayList wrapping
            for (SchedulerTaskEntry task : new ArrayList<>(this.schedulerTasks)) {
                long taskTick = task.getNextTick();

                if (taskTick == this.schedulerTick) {

                    // Cancelled tasks shouldn't be in the scheduler queue anyway.
                    if (!task.getTask().isCancelled()) {

                        if (task.isAsynchronous()) {
                            this.threadPool.submit(() -> {
                                synchronized (this.activeThreads) {
                                    this.activeThreads.add(Thread.currentThread());
                                }

                                try {
                                    task.getTask().run();

                                } catch (Exception err) {
                                    Scheduler.this.server.getLogger().error("Error thrown in a scheduler (asynchronous) task:", err);
                                }

                                synchronized (this.activeThreads) {
                                    this.activeThreads.remove(Thread.currentThread());
                                }
                            }); // Start async task and move on.

                        } else {
                            // Run as sync. This task must complete before the next one
                            // is ran.
                            try {
                                task.getTask().run();

                            } catch (Exception err) {
                                this.server.getLogger().error("Error thrown in a scheduler (synchronous) task:");
                                err.printStackTrace();
                            }
                        }


                        // Not cancelled by the call of #run() + it's a repeat task.
                        if (task.isRepeating() && (!task.getTask().isCancelled())) {
                            long targetTick = taskTick + task.getRepeatInterval();

                            SchedulerTaskEntry newTask = new SchedulerTaskEntry(task.getTask(), task.getRepeatInterval(), targetTick, task.isAsynchronous());
                            this.queueTaskEntry(newTask);
                        }
                    }

                } else if (taskTick > this.schedulerTick) {
                    // Upcoming task, do not remove from queue! :)
                    break;
                }

                this.schedulerTasks.remove(0); // Operate like a queue.
                // Remove from the start as long as it isn't an upcoming task.
                // If a task is somehow scheduled *before* the current tick, it should
                // be removed anyway.
            }
            this.schedulerTick++; // Tick after so tasks can be ran without a delay.
        }
    }


    // -- Task Control --

    protected synchronized void queueTaskEntry(SchedulerTaskEntry entry) {
        if (entry.getNextTick() <= this.schedulerTick) {
            throw new IllegalStateException("Task cannot be scheduled before the current tick.");
        }

        int size = this.schedulerTasks.size();
        for (int i = 0; i < size; i++) {
            // Entry belongs before task? Insert into it's position
            if (this.schedulerTasks.get(i).getNextTick() > entry.getNextTick()) {
                this.schedulerTasks.add(i, entry);
                return;
            }
        }

        // Not added in loop. Append to the end.
        this.schedulerTasks.add(entry);
    }

    // -- Task Registering --

    public PendingEntryBuilder prepareTask(Runnable task) {
        SchedulerTask runnableTask = new RunnableTypeTask(task);
        return new PendingEntryBuilder(this, runnableTask);
    }


    public PendingEntryBuilder prepareTask(SchedulerTask task) {
        return new PendingEntryBuilder(this, task);
    }

    // -- Getters --

    /** The unique ID of this scheduler. */
    public UUID getSchedulerID() {
        return this.schedulerID;
    }

    /**
     * Retrieve amount of server ticks this scheduler has been running for.
     * @return the amount of server ticks this scheduler has been running for.
     */
    public long getSyncedTick() {
        return this.syncedTick;
    }

    /**
     * Retrieve the amount of ticks this scheduler has executed.
     * @return the amount of ticks this scheduler has executed.
     */
    public long getSchedulerTick() {
        return this.schedulerTick;
    }

    /**
     * Retrieve the amount of server ticks between each scheduler tick.
     * @return the amount of server ticks between each scheduler tick.
     */
    public int getTickDelay() {
        return this.tickDelay;
    }

    /**
     * Retrieve a set of active async task threads.
     * @return a set of active async task threads
     */
    public Set<Thread> getActiveThreads() {
        return new HashSet<>(this.activeThreads);
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    // -- Setters --

    /** Sets the amount of server ticks that should pass between each scheduler tick. */
    public void setTickDelay(int tickDelay) {
        this.tickDelay = tickDelay;
    }


    public static class PendingEntryBuilder {

        protected final Scheduler scheduler;
        protected final SchedulerTask task;

        protected int interval;
        protected int delay;
        protected boolean isAsynchronous;

        protected PendingEntryBuilder(Scheduler scheduler, SchedulerTask task) {
            this.scheduler = Check.nullParam(scheduler, "scheduler");
            this.task = Check.nullParam(task, "task");

            this.interval = 0;
            this.delay = 0;
            this.isAsynchronous = false;
        }

        public SchedulerTask schedule() {
            synchronized (this.scheduler) {
                long nextTick = this.scheduler.schedulerTick + this.delay + 1;
                SchedulerTaskEntry entry = new SchedulerTaskEntry(this.task, this.interval, nextTick, this.isAsynchronous);
                this.scheduler.queueTaskEntry(entry);
            }
            return this.task;
        }


        public int getInterval() {
            return this.interval;
        }

        public int getDelay() {
            return this.delay;
        }

        public boolean isAsynchronous() {
            return this.isAsynchronous;
        }

        public PendingEntryBuilder setInterval(int interval) {
            this.interval = Check.inclusiveLowerBound(interval, 0, "interval");
            return this;
        }

        public PendingEntryBuilder setDelay(int delay) {
            this.delay = Check.inclusiveLowerBound(delay, 0, "delay");
            return this;
        }

        public PendingEntryBuilder setAsynchronous(boolean asynchronous) {
            this.isAsynchronous = asynchronous;
            return this;
        }
    }
}
