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
        turnsToReproduce = ThreadLocalRandom.current().nextInt(0, Config.CONFIG.turnsToReproduce(type));
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

    public int getAge() {
        return age;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public void resetTurnsToReproduce() {
        this.turnsToReproduce = Config.CONFIG.turnsToReproduce(type);
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
        increaseAge();
        decreaseHealth();

        tryToDie();

        decreaseTurnsToReproduce();
        tryToReproduce();
    }

    // general
    private void increaseAge() {
        synchronized (this) {
            age += Config.CONFIG.turnsPerMove(type.getLifeCycleType());
        }
    }

    private void decreaseHealth() {
        synchronized (this) {
            health = Math.max(health -
                    Config.CONFIG.decreaseHealthPerMove(type) *
                            Config.CONFIG.turnsPerMove(type.getLifeCycleType()), 0);
        }
    }

    // die
    private void tryToDie() {
        if (dieCheck.apply(this).checkPartyToAct().isPresent()) {
            dieAction.apply(this).doAction(this);
            age = 0;
            health = 0;
        }
    }

    // reproduce
    protected abstract void tryToReproduce();

    private void decreaseTurnsToReproduce() {
        synchronized (this) {
            turnsToReproduce = Math.max(turnsToReproduce - Config.CONFIG.turnsPerMove(type.getLifeCycleType()), 0);
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
        if (type.equals(CreatureType.NONEXISTENT)) {
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

    // static comparison - to lock in order
    public static Creature more(Creature first, Creature second) {
        return first.getId() > second.getId() ? first : second;
    }

    public static Creature less(Creature first, Creature second) {
        return first.getId() < second.getId() ? first : second;
    }

}
