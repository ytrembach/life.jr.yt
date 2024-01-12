package org.yt.jr.projects.creatures.lifecycles;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.creatures.CreaturesTypes;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LifeCycle implements AutoCloseable {
    final private AtomicInteger count = new AtomicInteger(0);
    final public static Map<CreaturesTypes, LifeCycle> LIFECYCLES = new HashMap<>();
    final private LifeCycleTypes type;
    final private Map<Creature, ScheduledFuture<?>> liveCreatures;

    final private ScheduledExecutorService executorService;

    public LifeCycle(LifeCycleTypes type) {
        this.type = type;
        liveCreatures = new HashMap<>();

        int poolSize = Config.CONFIG.getPoolSize(type);
        executorService = Executors.newScheduledThreadPool(poolSize,
                (r) -> new Thread(r, type.name().charAt(0) + "="));
    }

    public LifeCycleTypes getType() {
        return type;
    }

    @Override
    public String toString() {
        return "LifeCycle{" +
                "type=" + type +
                '}';
    }

    public void addCreature(Creature creature) {
        long turnsPerMove = Config.CONFIG.getTurnsPerMove(type);
        synchronized (liveCreatures) {
            ScheduledFuture<?> future = executorService.scheduleAtFixedRate(creature,
                    turnsPerMove,
                    ThreadLocalRandom.current().nextInt(900,1100) * turnsPerMove,
                    TimeUnit.MILLISECONDS);
            liveCreatures.put(creature, future);
            count.addAndGet(1);
        }
        Logger.Log(LogSources.LIFECYCLE, LogLevels.INFO, String.format("%s added to %s", creature, this));
    }

    public void removeCreature(Creature creature) {
        ScheduledFuture<?> future = liveCreatures.get(creature);
        synchronized (liveCreatures) {
            if (!future.cancel(true) || !future.isDone()) {
                Logger.Log(LogSources.LIFECYCLE,LogLevels.DEBUG,
                        String.format("task %s wasn't successfully cancelled from %s",future,this));
            }
            liveCreatures.remove(creature);
            count.addAndGet(-1);
        }
    }

    public void showStatus() {
        System.out.printf("%s: count: %d ", this, count.get());
        Map<CreaturesTypes, Integer> countByTypes = new HashMap<>();
        for (Creature creature : liveCreatures.keySet()) {
            CreaturesTypes type = creature.getType();
            countByTypes.put(type, countByTypes.getOrDefault(type, 0) + 1);
        }
        System.out.println(countByTypes);
    }

    @Override
    public void close() {
        executorService.shutdown();
    }
}
