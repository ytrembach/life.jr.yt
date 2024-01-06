package org.yt.jr.projects.maps;

import org.yt.jr.projects.creatures.CreatureList;

public class Location {
    private int row;
    private int col;
    // neighboring locations
    private Location up, down, left, right;
    private CreatureList habitants;

    public CreatureList getHabitants() {
        return habitants;
    }

    public void setHabitants(CreatureList habitants) {
        this.habitants = habitants;
    }

    Location(int row, int col) {
        this.row = row;
        this.col = col;
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

}
