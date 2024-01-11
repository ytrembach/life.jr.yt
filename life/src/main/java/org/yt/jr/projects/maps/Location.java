package org.yt.jr.projects.maps;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.creatures.CreaturesTypes;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import java.net.Inet4Address;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Location {
    public static Location NOWHERE = new Location();
    final private static AtomicInteger lastId = new AtomicInteger(1);
    final private int id;
    final private int row;
    final private int col;

    // neighboring locations
    final private Map<LocationNeighbors, Location> neighbors;
    final private ArrayList<Creature> habitants;

    final private Map<CreaturesTypes, Integer> creaturesCount;

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

    public Integer getCreaturesCount(CreaturesTypes type) {
        return creaturesCount.get(type);
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

    void setNeighbor(LocationNeighbors direction, Location neighbor) {
        this.neighbors.put(direction, neighbor);
    }

    public void addCreature(Creature creature) {
        CreaturesTypes type = creature.getType();
        habitants.add(creature);
        creature.setLocation(this);
        creaturesCount.put(type, creaturesCount.getOrDefault(type, 0) + 1);
        Logger.Log(LogSources.CREATURE, LogLevels.INFO, String.format("%s added to %s", creature, this));
    }

    public void removeCreature(Creature creature) {
        CreaturesTypes type = creature.getType();
        habitants.remove(creature);
        creaturesCount.put(type, creaturesCount.get(type) - 1);
        Logger.Log(LogSources.CREATURE, LogLevels.INFO, String.format("%s removed from %s", creature, this));
        creature.setLocation(NOWHERE);
    }

    public boolean checkSpaceAvailable(CreaturesTypes type, boolean writeLogOnError) {
        if (this == NOWHERE) {
            return false;
        }
        if (creaturesCount.get(type) >= Config.CONFIG.getMaxCreaturePerLocation(type)) {
            if (writeLogOnError) {
                Logger.Log(LogSources.CREATURE, LogLevels.DEBUG,
                        String.format("No slots for %s on %s", type, this));
            }
            return false;
        }
        return true;
    }
}
