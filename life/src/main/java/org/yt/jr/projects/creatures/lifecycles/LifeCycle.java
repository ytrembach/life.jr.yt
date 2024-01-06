package org.yt.jr.projects.creatures.lifecycles;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.CreatureList;
import org.yt.jr.projects.creatures.Plant;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.CreaturesTypes;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LifeCycle {

    final private CreaturesTypes type;
    final private HashMap<Creature, ScheduledFuture<?>> liveCreatures;

    final private ScheduledExecutorService executorService;

    public LifeCycle(CreaturesTypes type) {
        this.type = type;
        liveCreatures = new HashMap<>();

        int poolSize = Config.CONFIG.getPoolSize(type);
        executorService = Executors.newScheduledThreadPool(poolSize);
    }

    public void addCreature(Creature creature) {
        long turnDuration = Config.CONFIG.getTurnDurationInSecond(type);
        ScheduledFuture<?> future = executorService.scheduleAtFixedRate(creature, turnDuration, turnDuration, TimeUnit.SECONDS);
        synchronized (liveCreatures) {
            liveCreatures.put(creature, future);
        }
    }

    public void removeCreature(Creature creature) {
        ScheduledFuture<?> future = liveCreatures.get(creature);
        future.cancel(false);
        synchronized (liveCreatures) {
            liveCreatures.remove(creature);
        }
    }
}
