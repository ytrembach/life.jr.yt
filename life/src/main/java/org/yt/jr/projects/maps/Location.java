package org.yt.jr.projects.maps;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.CreatureList;
import org.yt.jr.projects.utils.CreaturesTypes;

import java.util.HashMap;

public class Location {
    final private int row;
    final private int col;
    // neighboring locations
    private Location up, down, left, right;
    final private CreatureList habitants;

    final private HashMap<CreaturesTypes,Integer> creaturesCount;

    Location(int row, int col) {
        this.row = row;
        this.col = col;
        habitants = new CreatureList();

        creaturesCount = new HashMap<>();
    }

    void setUp(Location up) {
        this.up = up;
    }

    void setDown(Location down) {
        this.down = down;
    }

    void setLeft(Location left) {
        this.left = left;
    }

    void setRight(Location right) {
        this.right = right;
    }

    public CreatureList getHabitants() {
        return habitants;
    }

    public void addCreature(Creature creature) {
        synchronized (creature) {

        }
    }
}
