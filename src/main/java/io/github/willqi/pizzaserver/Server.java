package io.github.willqi.pizzaserver;

import io.github.willqi.pizzaserver.utils.TimeUtils;

public class Server {

    private int targetTps;
    private int currentTps;
    private boolean running;

    public Server(String rootDirectory) {

    }

    public void boot() {
        this.running = true;

        this.targetTps = 20;

        long initNanoTime = System.nanoTime();
        long nextTpsRecording = initNanoTime + TimeUtils.secondsToNanoSeconds(1);
        long nanoSecondsPerTick = TimeUtils.secondsToNanoSeconds(1) / this.targetTps;
        long sleepTime = TimeUtils.nanoSecondsToMilliseconds(nanoSecondsPerTick);
        long nextPredictedNanoTimeTick = initNanoTime + nanoSecondsPerTick;
        int currentTps = 0;
        while (this.running) {

            // TODO: tick stuff

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
                this.stop();
                return;
            }
            long completedNanoTime = System.nanoTime();

            nanoSecondsPerTick = TimeUtils.secondsToNanoSeconds(1) / this.targetTps;
            long diff = nextPredictedNanoTimeTick - completedNanoTime;
            nextPredictedNanoTimeTick = completedNanoTime + nanoSecondsPerTick + diff;
            sleepTime = TimeUtils.nanoSecondsToMilliseconds(Math.max(nanoSecondsPerTick + diff, 0));

            if (completedNanoTime > nextTpsRecording) {
                this.currentTps = currentTps;
                currentTps = 0;
                nextTpsRecording = System.nanoTime() + TimeUtils.secondsToNanoSeconds(1);
            }
            currentTps++;

        }
    }

    public void stop() {
        this.running = false;
    }

    public String getIp() {
        return null;
    }

    public int getPort() {
        return 0;
    }

    public String getName() {
        return null;
    }

    public void setTargetTps(int newTps) {
        this.targetTps = newTps;
    }

    public int getTargetTps() {
        return this.targetTps;
    }

    public int getCurrentTps() {
        return this.currentTps;
    }

}
