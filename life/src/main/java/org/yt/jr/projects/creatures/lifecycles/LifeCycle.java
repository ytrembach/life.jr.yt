package org.yt.jr.projects.creatures.lifecycles;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.CreaturesTypes;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LifeCycle implements AutoCloseable {

    final public static Map<CreaturesTypes,LifeCycle> LIFECYCLES = new HashMap<>();

    final private CreaturesTypes type;
    final private Map<Creature, ScheduledFuture<?>> liveCreatures;

    final private ScheduledExecutorService executorService;

    public LifeCycle(CreaturesTypes type) {
        this.type = type;
        liveCreatures = new HashMap<>();

        int poolSize = Config.CONFIG.getPoolSize(type);
        executorService = Executors.newScheduledThreadPool(poolSize);
    }

    @Override
    public String toString() {
        return "LifeCycle{" +
                "type=" + type +
                '}';
    }

    public void addCreature(Creature creature) {
        long turnDuration = Config.CONFIG.getTurnDurationInSecond(type);
        ScheduledFuture<?> future = executorService.scheduleAtFixedRate(creature, turnDuration, turnDuration, TimeUnit.SECONDS);
        synchronized (liveCreatures) {
            liveCreatures.put(creature, future);
        }
        Logger.Log(LogSources.LIFECYCLE, LogLevels.INFO,String.format("%s added to %s",creature,this));
    }

    public void removeCreature(Creature creature) {
        ScheduledFuture<?> future = liveCreatures.get(creature);
        future.cancel(false);
        synchronized (liveCreatures) {
            liveCreatures.remove(creature);
        }
    }

    @Override
    public void close() throws Exception {
        executorService.shutdown();
    }
}
