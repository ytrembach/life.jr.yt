package org.yt.jr.projects.creatures;

import org.yt.jr.projects.creatures.actions.Action;
import org.yt.jr.projects.creatures.actions.Check;
import org.yt.jr.projects.creatures.actions.die.DieAction;
import org.yt.jr.projects.creatures.actions.die.DieCheck;
import org.yt.jr.projects.maps.Location;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;


public abstract class Creature implements Runnable {
    //
    final private static AtomicInteger lastId = new AtomicInteger(0);
    final protected CreatureType type;
    //
    final protected int id;
    protected int health;
    protected int age = 0;
    protected Location location;
    //
    final private Function<Creature, Check> dieCheck = DieCheck::new;
    final private Function<Creature, Action> dieAction = DieAction::new;
    //
    private int turnsToReproduce;
    protected Function<Creature, Check> reproduceCheck;
    protected Function<Creature, Action> reproduceAction;

    public Creature(CreatureType type, int health) {
        this.id = lastId.addAndGet(1);
        this.type = type;
        this.health = health;
        turnsToReproduce = ThreadLocalRandom.current().nextInt(0, Config.getConfig().turnsToReproduce(type));
    }

    public CreatureType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAge() {
        return age;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public Function<Creature, Action> getDieAction() {
        return dieAction;
    }

    public void resetTurnsToReproduce() {
        this.turnsToReproduce = Config.getConfig().turnsToReproduce(type);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Creature creature = (Creature) o;

        return id == creature.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public void run() {
        try {
            if (!isAlive()) {
                return;
            }

            increaseAge();
            decreaseHealth();

            tryToDie();

            if (isAlive()) {
                decreaseTurnsToReproduce();
                tryToReproduce();
            }
        } catch (Exception e) {
            Logger.Log(LogSources.CREATURE, LogLevels.ERROR, String.format("Exception %s, message: %s", e, e.getMessage()));
            for (StackTraceElement el : e.getStackTrace()) {
                Logger.Log(LogSources.CREATURE, LogLevels.ERROR, el.toString());
            }
        }
    }

    // general
    private void increaseAge() {
        synchronized (this) {
            age += Config.getConfig().turnsPerMove(type.getLifeCycleType());
        }
    }

    private void decreaseHealth() {
        synchronized (this) {
            health = Math.max(health -
                    Config.getConfig().decreaseHealthPerMove(type) *
                            Config.getConfig().turnsPerMove(type.getLifeCycleType()), 0);
        }
    }

    public boolean isAlive() {
        return health >= 0;
    }

    // die
    private void tryToDie() {
        if (dieCheck.apply(this).checkReadyToAct().isPresent()) {
            dieAction.apply(this).doAction(this);
        }
    }

    // reproduce
    protected abstract void tryToReproduce();

    private void decreaseTurnsToReproduce() {
        synchronized (this) {
            turnsToReproduce = Math.max(turnsToReproduce - Config.getConfig().turnsPerMove(type.getLifeCycleType()), 0);
        }
    }

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

    // static comparison - to lock in order
    public static Creature more(Creature first, Creature second) {
        return first.getId() > second.getId() ? first : second;
    }

    public static Creature less(Creature first, Creature second) {
        return first.getId() < second.getId() ? first : second;
    }

}
