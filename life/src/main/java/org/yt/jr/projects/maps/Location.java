package org.yt.jr.projects.maps;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.creatures.CreatureType;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Location {
    public static Location NOWHERE = new Location();
    final private static AtomicInteger lastId = new AtomicInteger(0);
    final private int id;
    final private int row;
    final private int col;

    final private Map<NeighborDirection, Location> neighbors;

    final private ArrayList<Creature> habitants;

    final private Map<CreatureType, Integer> creaturesCount;

    private Location() {
        this.id = 0;
        this.row = Integer.MAX_VALUE;
        this.col = Integer.MAX_VALUE;
        habitants = new ArrayList<>();
        neighbors = new HashMap<>();
        creaturesCount = new HashMap<>();
    }

    Location(int row, int col) {
        this.id = lastId.addAndGet(1);
        this.row = row;
        this.col = col;
        habitants = new ArrayList<>();
        neighbors = new HashMap<>();
        creaturesCount = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public ArrayList<Creature> getHabitants() {
        return habitants;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", row=" + row +
                ", col=" + col +
                ", creaturesCount=" + creaturesCount +
                '}';
    }

    public Optional<Location> getNeighbor(final NeighborDirection direction) {
        final Location neighbor = neighbors.get(direction);
        return neighbor != null ? Optional.of(neighbor) : Optional.empty();
    }

    void setNeighbor(NeighborDirection direction, Location neighbor) {
        this.neighbors.put(direction, neighbor);
    }

    public void addCreature(Creature creature) {
        if (this == NOWHERE) {
            return;
        }
        CreatureType type = creature.getType();
        habitants.add(creature);
        creature.setLocation(this);
        creaturesCount.put(type, creaturesCount.getOrDefault(type, 0) + 1);
        Logger.Log(LogSources.CREATURE, LogLevels.INFO, String.format("%s added to %s", creature, this));
    }

    public void removeCreature(Creature creature) {
        if (this == NOWHERE) {
            return;
        }
        CreatureType type = creature.getType();
        habitants.remove(creature);
        creaturesCount.put(type, creaturesCount.get(type) - 1);
        Logger.Log(LogSources.CREATURE, LogLevels.INFO, String.format("%s deleted from %s", creature, this));
        creature.setLocation(NOWHERE);
    }

    public boolean checkSpaceAvailable(CreatureType type, boolean writeLogOnError) {
        if (this == NOWHERE) {
            return false;
        }
        if (creaturesCount.get(type) >= Config.getConfig().maxCreaturePerLocation(type)) {
            if (writeLogOnError) {
                Logger.Log(LogSources.CREATURE, LogLevels.DEBUG,
                        String.format("No slots for %s on %s", type, this));
            }
            return false;
        }
        return true;
    }

    // static comparison - to lock in order
    public static Location more(Location first, Location second) {
        return first.getId() > second.getId() ? first : second;
    }

    public static Location less(Location first, Location second) {
        return first.getId() < second.getId() ? first : second;
    }

}
