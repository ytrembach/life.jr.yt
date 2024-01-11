package org.yt.jr.projects.creatures;

import org.yt.jr.projects.creatures.actions.die.DieAction;
import org.yt.jr.projects.maps.Location;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Creature implements Runnable {
    //
    final private static AtomicInteger lastId = new AtomicInteger(0);
    final protected CreaturesTypes type;
    //
    final protected int id;
    protected int health;
    protected int age;
    protected Location location;
    //
    protected int turnsToReproduce;

    public Creature(CreaturesTypes type, int health) {
        this.type = type;
        this.id = lastId.addAndGet(1);
        this.health = health;
        this.age = 0;
        resetTurnsToReproduce();
    }

    public CreaturesTypes getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public long getTurnsToReproduce() {
        return turnsToReproduce;
    }

    public void resetTurnsToReproduce() {
        this.turnsToReproduce = Config.CONFIG.getTurnsToReproduceDefault(type);
    }

    @Override
    public String toString() {
        return "Creature{" +
                "type=" + type +
                ", id=" + id +
                ", health=" + health +
                ", age=" + age +
                ", location=" + location.getId() +
                ", turnsToReproduce=" + turnsToReproduce +
                '}';
    }

    @Override
    public void run() {
        age += Config.CONFIG.getTurnsPerMove(type.getLifeCycleType());
        tryToDie();

        turnsToReproduce -= Config.CONFIG.getTurnsPerMove(type.getLifeCycleType());
        turnsToReproduce = (turnsToReproduce < 0) ? 0 : turnsToReproduce;
        tryToReproduce();
    }

    public void tryToDie() {
        final double maxAge = Config.CONFIG.getMaxAge(type);
        final double deathProbability = -1 / Math.exp(1) * Math.log((maxAge - age) / maxAge);
        if (age >= maxAge || ThreadLocalRandom.current().nextDouble() < deathProbability) {
            Logger.Log(LogSources.CREATURE, LogLevels.DEBUG, String.format("%s died", this));
            new DieAction(this).die();
        }
    }

    public abstract void tryToReproduce();

    //
    public boolean isReadyToReproduce(boolean writeLog) {
        if (!checkTurnsToReproduce(writeLog)) {
            Logger.Log(LogSources.CREATURE, LogLevels.DEBUG,
                    String.format("%s does not ready no reproduce",
                            this));
            return false;
        }
        if (!location.checkSpaceAvailable(type, writeLog)) {
            Logger.Log(LogSources.LOCATION, LogLevels.DEBUG,
                    String.format("No slots for %s on location=%d %s", type, location.getId(), location));
            return false;
        }
        return true;
    }

    private boolean checkTurnsToReproduce(boolean writeLogOnError) {
        if (type.equals(CreaturesTypes.NONEXISTENT)) {
            return false;
        }
        if (turnsToReproduce > 0) {
            if (writeLogOnError) {
                Logger.Log(LogSources.CREATURE, LogLevels.ERROR,
                        String.format("attempt to clone failed: %d turns left for %s ready",
                                turnsToReproduce, this));
            }
            return false;
        } else {
            return true;
        }
    }


    //
    public static Creature more(Creature first, Creature second) {
        return first.getId() > second.getId() ? first : second;
    }

    public static Creature less(Creature first, Creature second) {
        return first.getId() < second.getId() ? first : second;
    }

}
