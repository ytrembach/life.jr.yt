package org.yt.jr.projects.creatures;

import org.yt.jr.projects.creatures.lifecycles.CreatureFactory;
import org.yt.jr.projects.creatures.lifecycles.LifeCycle;
import org.yt.jr.projects.maps.Location;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.CreaturesTypes;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Creature implements Runnable {
    final public static int MAX_HEALTH = 100;
    final public static int CHILD_HEALTH = 10;
    final private static AtomicInteger lastId = new AtomicInteger(0);
    final protected CreaturesTypes type;
    final protected int id;
    protected int health;
    protected int age;
    protected int turnsToReproduce;
    protected Location location;

    public Creature(CreaturesTypes type, int health) {
        this.type = type;
        this.id = lastId.addAndGet(1);

        this.health = health;
        this.age = 0;
        resetTurnsToReproduce();

        Logger.Log(LogSources.CREATURE, LogLevels.INFO, String.format("%s created", this));
    }

    public CreaturesTypes getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getTurnsToReproduce() {
        return turnsToReproduce;
    }

    public void resetTurnsToReproduce() {
        this.turnsToReproduce = Config.CONFIG.getTurnsToReproduceDefault(type);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Creature{" +
                "type=" + type +
                ", id=" + id +
                ", health=" + health +
                ", age=" + age +
                ", location=" + location +
                '}';
    }

    @Override
    public void run() {
        // Logger.Log(LogSources.CREATURE, LogLevels.DEBUG, String.format("%s running",this));
        age++;
        reproduce();
    }

    public abstract void reproduce();

    protected void bornChild() {
        final Creature creature = CreatureFactory.CREATURE_FACTORY.getCreature(type).apply(CHILD_HEALTH);
        synchronized (creature) {
            synchronized (location) {
                location.addCreature(creature);
            }

            LifeCycle lifeCycle = LifeCycle.LIFECYCLES.get(type);
            synchronized (lifeCycle) {
                lifeCycle.addCreature(creature);
            }
        }
    }
}
