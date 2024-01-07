package org.yt.jr.projects.maps;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.CreaturesTypes;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Location {
    final private int row;
    final private int col;
    // neighboring locations
    final private Map<LocationNeighbors,Location> neighbors;
    final private ArrayList<Creature> habitants;

    final private Map<CreaturesTypes,Integer> creaturesCount;

    Location(int row, int col) {
        this.row = row;
        this.col = col;
        habitants = new ArrayList<>();
        neighbors = new HashMap<>();

        creaturesCount = new HashMap<>();
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
                row + "x" + col +
                ", creaturesCount=" + creaturesCount +
                '}';
    }

    void setNeighbor(LocationNeighbors direction, Location neighbor) {
        this.neighbors.put(direction,neighbor);
    }

    public void addCreature(Creature creature) {
        CreaturesTypes type = creature.getType();
        habitants.add(creature);
        creature.setLocation(this);
        creaturesCount.put(type,creaturesCount.getOrDefault(type,0)+1);
        Logger.Log(LogSources.CREATURE, LogLevels.INFO,String.format("%s added to %s",creature, this));
    }

    public boolean isSpaceAvailable(CreaturesTypes type) {
        return creaturesCount.get(type) < Config.CONFIG.getMaxCreaturePerLocation(type);
    }
}
